#ifndef _GET_USAGE_LINUX_
#define _GET_USAGE_LINUX_

#include <iostream>
#include <fstream>
#include <sstream>
#include <ctime>
#include <iomanip>
#include <sys/statvfs.h>
#include <sys/sysinfo.h>
#include <sys/utsname.h>
#include <sys/resource.h>
#include <sys/socket.h>
#include <dirent.h>
#include <unistd.h>
#include <vector>
#include <algorithm>
#include <iterator>
#include <string.h>
#include <cstring>
#include <pwd.h>
#include <net/if.h>
#include <ifaddrs.h>
#include <memory>
#include <stdexcept>
#include <cstdlib>
#include <cstdio>
namespace get_general_information_linux
{

    struct CPU_stats
    {
        int user;
        int nice;
        int system;
        int idle;
        int iowait;
        int irq;
        int softirq;
        int steal;
        int guest;
        int guest_nice;

        int get_total_idle()
        const {
            return idle + iowait;
        }

        int get_total_active()
        const {
            return user + nice + system + irq + softirq + steal + guest + guest_nice;
        }

    };

    struct Memory_stats
    {
        int total_memory;
        int available_memory;
        int total_swap;
        int free_swap;

        float get_memory_usage() {
            const float result	= static_cast<float>(total_memory - available_memory) / total_memory;
            return result;
        }

        float get_swap_usage() {
            const float result	= static_cast<float>(total_swap - free_swap) / total_swap;
            return result;
        }
    };

    inline CPU_stats read_cpu_data()
    {
        CPU_stats result;
        std::ifstream proc_stat("/proc/stat");

        if (proc_stat.good())
        {
            std::string line;
            getline(proc_stat, line);

            unsigned int *stats_p = (unsigned int *)&result;
            std::stringstream iss(line);
            std::string cpu;
            iss >> cpu;
            while (iss >> *stats_p)
            {
                stats_p++;
            };
        }

        proc_stat.close();

        return result;
    }

    inline int get_val(const std::string &target, const std::string &content) {
        int result = -1;
        std::size_t start = content.find(target);
        if (start != std::string::npos) {
            int begin = start + target.length();
            std::size_t end = content.find("kB", start);
            std::string substr = content.substr(begin, end - begin);
            result = std::stoi(substr);
        }
        return result;
    }

    inline Memory_stats read_memory_data()
    {
        Memory_stats result;
        std::ifstream proc_meminfo("/proc/meminfo");

        if (proc_meminfo.good()){
            std::string content((std::istreambuf_iterator<char>(proc_meminfo)),
                    std::istreambuf_iterator<char>());

            result.total_memory = get_val("MemTotal:", content);
            result.total_swap = get_val("SwapTotal:", content);
            result.free_swap = get_val("SwapFree:", content);
            result.available_memory = get_val("MemAvailable:", content);

        }

        proc_meminfo.close();

        return result;
    }

    inline float get_cpu_usage(const CPU_stats &first, const CPU_stats &second) {
        const float active_time	= static_cast<float>(second.get_total_active() - first.get_total_active());
		const float idle_time	= static_cast<float>(second.get_total_idle() - first.get_total_idle());
		const float total_time	= active_time + idle_time;
        return active_time / total_time;
    }

    inline float get_disk_usage(const std::string & disk) {
        struct statvfs diskData;

        statvfs(disk.c_str(), &diskData);

        auto total = diskData.f_blocks;
        auto free = diskData.f_bfree;
        auto diff = total - free;

        float result = static_cast<float>(diff) / total;

        return result;
    }
    std::string getProcessNetStats() {
        std::ostringstream path;
        path << "/proc/net/dev";
        int cnt = 0;
        std::ifstream netFile(path.str());
        std::string line;
        std::string processStats;
        std::string NumofAdapterInstalled;
        if (netFile) {
            // Skip the first two lines as they contain headers
            std::getline(netFile, line);
            std::getline(netFile, line);

            while (std::getline(netFile, line)) {
                std::istringstream iss(line);
                //std::string interface;
                //long long int receiveBytes, transmitBytes, receivePacket, transmitPacket;
                std::istream_iterator<std::string> iter(iss), end;
                std::vector<std::string> stats(iter, end);
                processStats += "Interface: " + stats[0] + "\n";
                cnt += 1;
                processStats += "Upload: " + stats[1] + " (bytes/s) " + "\n";
                processStats += "Download: " + stats[9] + " (bytes/s)" + "\n";
            }
         else {
            processStats = "Error: Could not open file.";
        }

        return processStats;
        }
    }

    std::string getCurrentUser() {
        uid_t uid = geteuid();
        struct passwd* pw = getpwuid(uid);
        if (!pw) {
            std::cerr << "Failed to retrieve current user information." << std::endl;
            return "";
        }

        return pw->pw_name;
    }
    std::string getCurrentLocalTime() {
        // Get the current time
        time_t currentTime = time(nullptr);

        // Convert the current time to the local time
        struct tm* localTime = localtime(&currentTime);

        // Format the local time
        std::stringstream ss;
        ss << std::put_time(localTime,"%A, %B %d %Y %I:%M:%S %p");
        std::string timeString = ss.str();

        return timeString;
    }
}
