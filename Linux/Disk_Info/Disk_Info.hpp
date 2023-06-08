#ifndef _GET_DISK_LINUX_
#define _GET_DISK_LINUX_

#include <iostream>
#include <fstream>
#include <sstream>
#include <sys/statvfs.h>
namespace get_disk_linux
{
    void DiskInfo() {
        struct statvfs disk_info;
        
        if (statvfs("/", &disk_info) == -1) {
            std::cerr << "Failed to retrieve disk information." << std::endl;
            return;
        }
        
        unsigned long long totalIO = disk_info.f_files;
        unsigned long long readIO = disk_info.f_files - disk_info.f_ffree  ;
        unsigned long long writeIO = disk_info.f_ffree;
        unsigned long long dataRead = disk_info.f_blocks - disk_info.f_bfree;
        unsigned long long dataWrite = disk_info.f_bfree;
        
        std::cout << "Disk Information" << std::endl;
        std::cout << "----------------" << std::endl;
        std::cout << "Total I/O Operations: " << totalIO << std::endl;
        std::cout << "Read Operations: " << readIO  << std::endl;
        std::cout << "Write Operations: " << writeIO << std::endl;
        std::cout << "Data Read: " << dataRead << " bytes" << std::endl;
        std::cout << "Data Write: " << dataWrite << " bytes" << std::endl;
    }

}
#endif