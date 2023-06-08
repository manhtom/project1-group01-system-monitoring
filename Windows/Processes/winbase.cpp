#include <Windows.h>
#include <windef.h>
#include <stdio.h>
#include <tchar.h>
#include <psapi.h>
#include <comdef.h>
#include <iostream>

using namespace std;

#ifdef _UNICODE
  #define tcout wcout
  #define tcerr wcerr
#else
  #define tcout cout
  #define tcerr cerr
#endif


void getIOInfo( DWORD processID )
{
    IO_COUNTERS IOInfo = {0};
    // Get a handle to the process.

    HANDLE hProcess = OpenProcess( PROCESS_QUERY_INFORMATION |
                                   PROCESS_VM_READ,
                                   FALSE, processID );

    GetProcessIoCounters(hProcess, &IOInfo);
    printf(" ReadOperationCount: %u\n",IOInfo.ReadOperationCount);
    printf(" WriteOperationCount: %u\n",IOInfo.WriteOperationCount);
    printf(" OtherOperationCount: %u\n",IOInfo.OtherOperationCount);
    printf(" ReadTransferCount: %u\n",IOInfo.ReadTransferCount);
    printf(" WriteTransferCount: %u\n",IOInfo.WriteTransferCount);
    printf(" OtherTransferCount: %u\n",IOInfo.OtherTransferCount);

}

void getProcessPriority( DWORD processID ) {
    // Get a handle to the process.

    HANDLE hProcess = OpenProcess( PROCESS_QUERY_INFORMATION |
                                   PROCESS_VM_READ,
                                   FALSE, processID );

    printf(" Priority: %u\n", GetPriorityClass(hProcess));

}

void _tgetUser() {
    TCHAR user[256] = TEXT("");
    DWORD dwSize = _countof(user);
    GetUserName(user, &dwSize);
    _tprintf(TEXT("User Name: %s\n"), user);
    ZeroMemory(user, dwSize);
}

int main(void)

{   
    _tgetUser();

    DWORD i; 
    printf("Enter the PID to display info about%s\n","");
    std::cin >> std::dec >> i;
    getIOInfo(i);
    getProcessPriority(i);
    }
