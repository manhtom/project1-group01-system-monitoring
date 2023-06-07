#ifndef _GET_SYSTEM_LINUX_
#define _GET_SYSTEM_LINUX_

#include <iostream>
#include <fstream>
#include <sstream>
#include <sys/sysinfo.h>
#include <sys/utsname.h>
#include <unistd.h>
#include <vector>
#include <algorithm>
#include <iterator>
#include <string.h>
#include <memory>
#include <stdexcept>
namespace get_system_linux
{
    std::string getOSName() {
    struct utsname os_info;
    if (uname(&os_info) != 0) {
        return "Unknown";
    }
    return os_info.sysname;
    }

    std::string getOSVersion() {
        struct utsname os_info;
        if (uname(&os_info) != 0) {
            return "Unknown";
        }
        return os_info.release;
    }

    std::string getUptime() {
        struct sysinfo sys_info;
        if (sysinfo(&sys_info) != 0) {
            return "Unknown";
        }
        int uptimeSeconds = sys_info.uptime;
        int days = uptimeSeconds / (60 * 60 * 24);
        int hours = (uptimeSeconds % (60 * 60 * 24)) / (60 * 60);
        int minutes = (uptimeSeconds % (60 * 60)) / 60;
        int seconds = uptimeSeconds % 60;
        char uptime[128];
        snprintf(uptime, sizeof(uptime), "%d days, %02d:%02d:%02d", days, hours, minutes, seconds);
        return uptime;
    }

    std::string getInstalledMemory() {
        struct sysinfo sys_info;
        if (sysinfo(&sys_info) != 0) {
            return "Unknown";
        }
        unsigned long long totalMemory = sys_info.totalram * sys_info.mem_unit;
        char memory[128];
        snprintf(memory, sizeof(memory), "%.2f GB", static_cast<double>(totalMemory) / (1024 * 1024 * 1024));
        return memory;
    }
    std::string getCPUtype(std::string type){
        std::string substr1 = "Intel";
        std::string substr2 = "AMD";
        std::string substr3 = "ARM";
        std::string substr4 = "IBM";
        if (type.find(substr1) != std::string::npos){
            std::cout << "CPU Type: " << substr1 << "\n";
        }
        else if (type.find(substr2) != std::string::npos){
            std::cout << "CPU Type: " << substr2 << "\n";
        }
        else if (type.find(substr3) != std::string::npos){
            std::cout << "CPU Type: " << substr3 << "\n";
        }
        else {
            std::cout << "CPU Type: " << substr4 << "\n";
        }
        return "";
    }
    std::string getCPUInfo() {
        std::ifstream cpuinfoFile("/proc/cpuinfo");
        if (!cpuinfoFile.is_open()) {
            return "Unknown";
        }
        std::string line;
        while (std::getline(cpuinfoFile, line)) {
            if (line.find("model name") != std::string::npos) {
                size_t colonIndex = line.find(":");
                if (colonIndex != std::string::npos) {
                    std::string type = line.substr(colonIndex + 2);
                    getCPUtype(type);
                    return "CPU: " + type;
                }
            }
        }
        return "Unknown";
    }

    std::string getArchitecture() {
        struct utsname os_info;
        if (uname(&os_info) != 0) {
            return "Unknown";
        }
        return os_info.machine;
    }

    std::string getComputerName() {
        char computerName[128];
        if (gethostname(computerName, sizeof(computerName)) != 0) {
            return "Unknown";
        }
        return computerName;
    }
    std::string getFirmwareType(const std::string& command) {
        std::string result;
        std::shared_ptr<FILE> pipe(popen(command.c_str(), "r"), pclose);
        if (!pipe) {
            throw std::runtime_error("popen() failed!");
        }
        constexpr int bufferSize = 128;
        char buffer[bufferSize];
        while (fgets(buffer, bufferSize, pipe.get()) != nullptr) {
            result += buffer;
        }
        return result;
    }


    void SystemInfo() {
        std::cout << "System Information" << std::endl;
        std::cout << "------------------" << std::endl;
        std::cout << "OS Name: " << getOSName() << std::endl;
        std::cout << "OS Version: " << getOSVersion() << std::endl;
        std::cout << "Uptime: " << getUptime() << std::endl;
        std::cout << "Installed Memory: " << getInstalledMemory() << std::endl;
        std::cout << getCPUInfo() << std::endl;
        std::cout << "Architecture: " << getArchitecture() << std::endl;
        std::cout << "Computer Name: " << getComputerName() << std::endl;
        std::string command = "lsb_release -a";
        std::string output = getFirmwareType(command);
        std::size_t pos = output.find("Description:");
        if (pos != std::string::npos) {
            std::string firmwareType = output.substr(pos + 12); // Skip "Description:"
            std::cout << "Firmware Type: " << firmwareType << std::endl;
        } else {
            std::cerr << "Failed to retrieve firmware type." << std::endl;
        }
    }
}
#endif