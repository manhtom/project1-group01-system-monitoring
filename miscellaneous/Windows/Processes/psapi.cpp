#include <iostream>
#include <windows.h>
#include <windef.h>
#include <stdio.h>
#include <tchar.h>
#include <psapi.h>
#include <chrono>
#include <math.h>

void getProcessPath(DWORD processID)
{
  TCHAR filename[MAX_PATH];

  HANDLE processHandle = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, FALSE,  processID);
  if (processHandle != NULL) {
        GetModuleFileNameEx(processHandle, NULL, filename, MAX_PATH);
        printf(" Module path: %s\n",filename);
        CloseHandle(processHandle);
    }
    
    else {
        printf("Failed to open process.%s\n");
  }
}


void timerSleep(double seconds) {
    using namespace std::chrono;

    static HANDLE timer = CreateWaitableTimer(NULL, FALSE, NULL);
    static double estimate = 5e-3;
    static double mean = 5e-3;
    static double m2 = 0;
    static int64_t count = 1;

    while (seconds - estimate > 1e-7) {
        double toWait = seconds - estimate;
        LARGE_INTEGER due;
        due.QuadPart = -int64_t(toWait * 1e7);
        auto start = high_resolution_clock::now();
        SetWaitableTimerEx(timer, &due, 0, NULL, NULL, NULL, 0);
        WaitForSingleObject(timer, INFINITE);
        auto end = high_resolution_clock::now();

        double observed = (end - start).count() / 1e9;
        seconds -= observed;

        ++count;
        double error = observed - toWait;
        double delta = error - mean;
        mean += delta / count;
        m2   += delta * (error - mean);
        double stddev = sqrt(m2 / (count - 1));
        estimate = mean + stddev;
    }

    // spin lock
    auto start = high_resolution_clock::now();
    auto spinNs = int64_t(seconds * 1e9);
    auto delay = nanoseconds(spinNs);
    while (high_resolution_clock::now() - start < delay);
}



void getProcessInfo( DWORD processID )
{
    TCHAR szProcessName[MAX_PATH] = TEXT("<unknown>");
    PROCESS_MEMORY_COUNTERS pmc;

    // Get a handle to the process.

    HANDLE hProcess = OpenProcess( PROCESS_QUERY_INFORMATION |
                                   PROCESS_VM_READ,
                                   FALSE, processID );
        
    // Get the process info.
    DWORD exitCode;
    PULONG64 CycleTime;

    if (hProcess != NULL)
    {
        HMODULE hMod;
        DWORD cbNeeded;

        if ( EnumProcessModules( hProcess, &hMod, sizeof(hMod), 
             &cbNeeded) )
        {
            GetModuleBaseName( hProcess, hMod, szProcessName, 
                               sizeof(szProcessName)/sizeof(TCHAR) );
            GetExitCodeProcess(hProcess, &exitCode);
            QueryProcessCycleTime(hProcess, CycleTime);
            CloseHandle(hProcess);

        }
    }
        


    // Print the process info.

    _tprintf( TEXT("%s - PID: %u - Status: %u"), szProcessName, processID, exitCode);

    // Print the process memory info.
    if ( GetProcessMemoryInfo( hProcess, &pmc, sizeof(pmc)) )
    {
        printf( "\tPageFaultCount: 0x%08X\n", pmc.PageFaultCount );
        printf( "\tPeakWorkingSetSize: 0x%08X\n", 
                pmc.PeakWorkingSetSize );
        printf( "\tWorkingSetSize: 0x%08X\n", pmc.WorkingSetSize );
        printf( "\tQuotaPeakPagedPoolUsage: 0x%08X\n", 
                pmc.QuotaPeakPagedPoolUsage );
        printf( "\tQuotaPagedPoolUsage: 0x%08X\n", 
                pmc.QuotaPagedPoolUsage );
        printf( "\tQuotaPeakNonPagedPoolUsage: 0x%08X\n", 
                pmc.QuotaPeakNonPagedPoolUsage );
        printf( "\tQuotaNonPagedPoolUsage: 0x%08X\n", 
                pmc.QuotaNonPagedPoolUsage );
        printf( "\tPagefileUsage: 0x%08X\n", pmc.PagefileUsage ); 
        printf( "\tPeakPagefileUsage: 0x%08X\n", 
                pmc.PeakPagefileUsage );
    }
    // Release the handle to the process.

    CloseHandle( hProcess );
}

void getPerfInfo(){
    PERFORMANCE_INFORMATION PerfInfo;
    DWORD cb = 1024;
    GetPerformanceInfo(&PerfInfo, cb);
    printf("CommitTotal: %u\n", PerfInfo.CommitTotal);
    printf("CommitPeak: %u\n", PerfInfo.CommitPeak);

    printf("PhysicalTotal: %u\n", PerfInfo.PhysicalTotal);
    printf("PhysicalAvailable: %u\n", PerfInfo.PhysicalAvailable);
    printf("SystemCache: %u\n", PerfInfo.SystemCache);
    printf("PageSize: %u\n", PerfInfo.PageSize);
    printf("HandleCount: %u\n", PerfInfo.HandleCount);
    printf("ProcessCount: %u\n", PerfInfo.ProcessCount);
    printf("ThreadCount: %u\n", PerfInfo.ThreadCount);
}
int main(void)
{
    getPerfInfo();
    // DWORD Process[1024], cbNeeded, cProcesses; // array can contain maximum 1024 processes
    // unsigned int i;

    // bool test = EnumProcesses( Process, sizeof(Process), &cbNeeded);

    // if ( !test )
    // {
    //     return 1;
    // }


    // Calculate how many process identifiers were returned.

    // cProcesses = cbNeeded / sizeof(DWORD); // current number of processes
    DWORD i; 
    printf("Enter the PID to display info about%s\n","");
    std::cin >> std::dec >> i;
    getProcessInfo(i);
    getProcessPath(i);
}
    // Print the name and process identifier for each process
