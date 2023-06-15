#include <iostream>
#include <windows.h>
#include <tchar.h>
#include <pdh.h>
#include <pdhmsg.h>
#include <Psapi.h>
#include <string>
#include <vector>

void getCPU() {
    // Get the handle to the processor performance counter
    PDH_HQUERY queryHandle;
    PDH_HCOUNTER counterHandle;
    PdhOpenQuery(NULL, NULL, &queryHandle);
    PdhAddCounter(queryHandle, _T("\\Processor(_Total)\\% Processor Time"), NULL, &counterHandle);
    PdhCollectQueryData(queryHandle);

    // Get the initial CPU utilization value
    PDH_FMT_COUNTERVALUE counterValue;
    PdhGetFormattedCounterValue(counterHandle, PDH_FMT_DOUBLE, NULL, &counterValue);

    // Wait for a short duration (e.g., 1 second)
    Sleep(1000);

    // Collect the CPU utilization value again
    PdhCollectQueryData(queryHandle);
    PdhGetFormattedCounterValue(counterHandle, PDH_FMT_DOUBLE, NULL, &counterValue);

    // Output the CPU utilization percentage
    printf("CPU Utilization: %.2f %\n", counterValue.doubleValue);

    // Close the performance counter handle
    PdhCloseQuery(queryHandle);

}

void getDiskActiveTime() {
    // Get the handle to the disk performance counter
    PDH_HQUERY queryHandle;
    PDH_HCOUNTER counterHandle;
    PdhOpenQuery(NULL, NULL, &queryHandle);
    PdhAddCounter(queryHandle, _T("\\PhysicalDisk(_Total)\\% Disk Time"), NULL, &counterHandle);
    PdhCollectQueryData(queryHandle);

    // Get the initial value
    PDH_FMT_COUNTERVALUE counterValue;
    PdhGetFormattedCounterValue(counterHandle, PDH_FMT_DOUBLE, NULL, &counterValue);

    // Wait for a short duration (e.g., 1 second)
    Sleep(1000);

    // Collect the value again
    PdhCollectQueryData(queryHandle);
    PdhGetFormattedCounterValue(counterHandle, PDH_FMT_DOUBLE, NULL, &counterValue);

    // Output the  percentage
    printf("Disk Active Time: %.2f %\n", counterValue.doubleValue);

    // Close the performance counter handle
    PdhCloseQuery(queryHandle);

}

int getDiskRWSpeed() {
    PDH_STATUS pdhStatus;
    PDH_HQUERY queryHandle;

    // Initialize the PDH library
    pdhStatus = PdhOpenQuery(NULL, NULL, &queryHandle);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to initialize the PDH library. Error code: " << pdhStatus << std::endl;
        return 1;
    }

    // Add the disk performance counters to the query
    PDH_HCOUNTER hCounterRead;
    pdhStatus = PdhAddCounterW(queryHandle, L"\\PhysicalDisk(_Total)\\Disk Read Bytes/sec", 0, &hCounterRead);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to add the Disk Read Bytes/sec counter. Error code: " << pdhStatus << std::endl;
        PdhCloseQuery(queryHandle);
        return 1;
    }

    PDH_HCOUNTER hCounterWrite;
    pdhStatus = PdhAddCounterW(queryHandle, L"\\PhysicalDisk(_Total)\\Disk Write Bytes/sec", 0, &hCounterWrite);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to add the Disk Write Bytes/sec counter. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterRead);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Collect the disk performance data
    pdhStatus = PdhCollectQueryData(queryHandle);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to collect the disk performance data. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterRead);
        PdhRemoveCounter(hCounterWrite);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Sleep for a while to get new data
    Sleep(1000);

    // Collect the updated disk performance data
    pdhStatus = PdhCollectQueryData(queryHandle);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to collect the updated disk performance data. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterRead);
        PdhRemoveCounter(hCounterWrite);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Get the counter values
    PDH_FMT_COUNTERVALUE counterValueRead, counterValueWrite;
    pdhStatus = PdhGetFormattedCounterValue(hCounterRead, PDH_FMT_DOUBLE, NULL, &counterValueRead);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to get the formatted counter value for Disk Read Bytes/sec. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterRead);
        PdhRemoveCounter(hCounterWrite);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    pdhStatus = PdhGetFormattedCounterValue(hCounterWrite, PDH_FMT_DOUBLE, NULL, &counterValueWrite);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to get the formatted counter value for Disk Write Bytes/sec. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterRead);
        PdhRemoveCounter(hCounterWrite);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Print the disk read and write speeds
    std::cout << "Disk Read Speed: " << counterValueRead.doubleValue << " bytes/sec" << std::endl;
    std::cout << "Disk Write Speed: " << counterValueWrite.doubleValue << " bytes/sec" << std::endl;

    // Clean up the PDH resources
    PdhRemoveCounter(hCounterRead);
    PdhRemoveCounter(hCounterWrite);
    PdhCloseQuery(queryHandle);

    return 0;
}


int getDiskRWOperation() {
    PDH_STATUS pdhStatus;
    PDH_HQUERY queryHandle;

    // Initialize the PDH library
    pdhStatus = PdhOpenQuery(NULL, NULL, &queryHandle);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to initialize the PDH library. Error code: " << pdhStatus << std::endl;
        return 1;
    }

    // Add the disk performance counters to the query
    PDH_HCOUNTER hCounterReads;
    pdhStatus = PdhAddCounterW(queryHandle, L"\\PhysicalDisk(_Total)\\Disk Reads/sec", 0, &hCounterReads);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to add the Disk Reads/sec counter. Error code: " << pdhStatus << std::endl;
        PdhCloseQuery(queryHandle);
        return 1;
    }

    PDH_HCOUNTER hCounterWrites;
    pdhStatus = PdhAddCounterW(queryHandle, L"\\PhysicalDisk(_Total)\\Disk Writes/sec", 0, &hCounterWrites);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to add the Disk Writes/sec counter. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterReads);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Collect the disk performance data
    pdhStatus = PdhCollectQueryData(queryHandle);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to collect the disk performance data. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterReads);
        PdhRemoveCounter(hCounterWrites);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Sleep for a while to get new data
    Sleep(1000);

    // Collect the updated disk performance data
    pdhStatus = PdhCollectQueryData(queryHandle);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to collect the updated disk performance data. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterReads);
        PdhRemoveCounter(hCounterWrites);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Get the counter values
    PDH_FMT_COUNTERVALUE counterValueReads, counterValueWrites;
    pdhStatus = PdhGetFormattedCounterValue(hCounterReads, PDH_FMT_LONG, NULL, &counterValueReads);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to get the formatted counter value for Disk Reads/sec. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterReads);
        PdhRemoveCounter(hCounterWrites);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    pdhStatus = PdhGetFormattedCounterValue(hCounterWrites, PDH_FMT_LONG, NULL, &counterValueWrites);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to get the formatted counter value for Disk Writes/sec. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterReads);
        PdhRemoveCounter(hCounterWrites);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Print the disk read and write operations
    std::cout << "Disk Read Operations: " << counterValueReads.longValue << " per sec" << std::endl;
    std::cout << "Disk Write Operations: " << counterValueWrites.longValue << " per sec" << std::endl;

    // Clean up the PDH resources
    PdhRemoveCounter(hCounterReads);
    PdhRemoveCounter(hCounterWrites);
    PdhCloseQuery(queryHandle);

    return 0;
}


int getNetSpeed() {
    PDH_STATUS pdhStatus;
    PDH_HQUERY queryHandle;

    // Initialize the PDH library
    pdhStatus = PdhOpenQuery(NULL, NULL, &queryHandle);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to initialize the PDH library. Error code: " << pdhStatus << std::endl;
        return 1;
    }

    // Add the network performance counters to the query
    PDH_HCOUNTER hCounterSent;
    pdhStatus = PdhAddCounterW(queryHandle, L"\\Network Interface(*)\\Bytes Sent/sec", 0, &hCounterSent);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to add the Bytes Sent/sec counter. Error code: " << pdhStatus << std::endl;
        PdhCloseQuery(queryHandle);
        return 1;
    }

    PDH_HCOUNTER hCounterReceived;
    pdhStatus = PdhAddCounterW(queryHandle, L"\\Network Interface(*)\\Bytes Received/sec", 0, &hCounterReceived);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to add the Bytes Received/sec counter. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterSent);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Collect the network performance data
    pdhStatus = PdhCollectQueryData(queryHandle);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to collect the network performance data. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterSent);
        PdhRemoveCounter(hCounterReceived);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Sleep for a while to get new data
    Sleep(1000);

    // Collect the updated network performance data
    pdhStatus = PdhCollectQueryData(queryHandle);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to collect the updated network performance data. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterSent);
        PdhRemoveCounter(hCounterReceived);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Get the counter values
    PDH_FMT_COUNTERVALUE counterValueSent, counterValueReceived;
    pdhStatus = PdhGetFormattedCounterValue(hCounterSent, PDH_FMT_DOUBLE, NULL, &counterValueSent);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to get the formatted counter value for Bytes Sent/sec. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterSent);
        PdhRemoveCounter(hCounterReceived);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    pdhStatus = PdhGetFormattedCounterValue(hCounterReceived, PDH_FMT_DOUBLE, NULL, &counterValueReceived);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to get the formatted counter value for Bytes Received/sec. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounterSent);
        PdhRemoveCounter(hCounterReceived);
        PdhCloseQuery(queryHandle);
        return 1;
    }

    // Print the network sent and received speeds
    std::cout << "Network Sent Speed: " << counterValueSent.doubleValue << " bytes/sec" << std::endl;
    std::cout << "Network Received Speed: " << counterValueReceived.doubleValue << " bytes/sec" << std::endl;

    // Clean up the PDH resources
    PdhRemoveCounter(hCounterSent);
    PdhRemoveCounter(hCounterReceived);
    PdhCloseQuery(queryHandle);

    return 0;
}



int getProcessCPUUsage(DWORD processId) {
    PDH_STATUS pdhStatus;
    HQUERY hQuery;
    HCOUNTER hCounter;

    // Initialize the PDH library
    pdhStatus = PdhOpenQuery(NULL, NULL, &hQuery);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to initialize the PDH library. Error code: " << pdhStatus << std::endl;
        return 1;
    }

    // Add the process CPU usage counter to the query
    std::wstring counterPath = L"\\Process(" + std::to_wstring(processId) + L")\\% Processor Time";
    pdhStatus = PdhAddCounterW(hQuery, counterPath.c_str(), 0, &hCounter);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to add the process CPU usage counter. Error code: " << pdhStatus << std::endl;
        PdhCloseQuery(hQuery);
        return 1;
    }

    // Collect the process CPU usage data
    pdhStatus = PdhCollectQueryData(hQuery);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to collect the process CPU usage data. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounter);
        PdhCloseQuery(hQuery);
        return 1;
    }

    // Wait for a while
    Sleep(1000);

    // Collect the updated process CPU usage data
    pdhStatus = PdhCollectQueryData(hQuery);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to collect the updated process CPU usage data. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounter);
        PdhCloseQuery(hQuery);
        return 1;
    }

    // Get the counter value
    PDH_FMT_COUNTERVALUE counterValue;
    pdhStatus = PdhGetFormattedCounterValue(hCounter, PDH_FMT_DOUBLE, NULL, &counterValue);
    if (pdhStatus != ERROR_SUCCESS) {
        std::cerr << "Failed to get the formatted counter value. Error code: " << pdhStatus << std::endl;
        PdhRemoveCounter(hCounter);
        PdhCloseQuery(hQuery);
        return 1;
    }

    // Print the process CPU usage
    std::cout << "Process CPU Usage: " << counterValue.doubleValue << "%" << std::endl;

    // Clean up the PDH resources
    PdhRemoveCounter(hCounter);
    PdhCloseQuery(hQuery);

    return 0;
}


int main(int argc, char **argv) {
    DWORD PID;

    // getCPU();
    // /getDiskActiveTime();
    // getDiskRWSpeed();
    // getDiskRWOperation();
    // getNetSpeed();
    std::cin >> std::dec >> PID;
    printf("%u\n",PID);
    getProcessCPUUsage(PID);
}