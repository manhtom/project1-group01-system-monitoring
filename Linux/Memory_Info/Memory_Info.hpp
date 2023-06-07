#ifndef _GET_MEMORY_LINUX_
#define _GET_MEMORY_LINUX_

#include <iostream>
#include <fstream>
#include <sstream>
#include <sys/sysinfo.h>
#include <string.h>
#include <cstring>

namespace get_memory_linux
{
    void Memory_Specific_Info(){
        struct sysinfo sys_info;

        if (sysinfo(&sys_info) == -1) {
            std::cerr << "Failed to retrieve system information." << std::endl;
            return;
        }

        long long totalRAM = sys_info.totalram * sys_info.mem_unit;
        long long freeRAM = sys_info.freeram * sys_info.mem_unit;
        long long usedRAM = totalRAM - freeRAM;
        long long totalSwap = sys_info.totalswap * sys_info.mem_unit;
        long long freeSwap = sys_info.freeswap * sys_info.mem_unit;

        std::cout << "Memory Information" << std::endl;
        std::cout << "------------------" << std::endl;
        std::cout << "Total RAM Installed: " << totalRAM / (1024 * 1024) << " MB" << std::endl;
        std::cout << "Available RAM: " << freeRAM / (1024 * 1024) << " MB" << std::endl;
        std::cout << "In Use RAM: " << usedRAM / (1024 * 1024) << " MB" << std::endl;
        std::cout << "Total Swap Space: " << totalSwap / (1024 * 1024) << " MB" << std::endl;
        std::cout << "Used Swap Space: " << (totalSwap - freeSwap) / (1024 * 1024) << " MB" << std::endl;
        std::cout << "Available Swap Space: " << freeSwap / (1024 * 1024) << " MB" << std::endl;
    }
}
#endif