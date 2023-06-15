#ifndef _GET_CPU_LINUX_
#define _GET_CPU_LINUX_

#include <iostream>
#include <fstream>
#include <sstream>
#include <dirent.h>
#include <unistd.h>
#include <vector>
#include <algorithm>
#include <iterator>
#include <string.h>
#include <cstring>
namespace get_CPU_linux
{

    std::string getCPUName(){
        std::ifstream cpuinfoFile("/proc/cpuinfo");
        if (!cpuinfoFile.is_open()){
            std::cerr << "Failed to open CPU info file. " << std::endl;
            return "";
        }

        std::string line;
        while (std::getline(cpuinfoFile, line)){
            if (line.substr(0,10) == "model name"){
                cpuinfoFile.close();
                return line.substr(line.find(":") + 2);
            }
        }

        cpuinfoFile.close();
        return " ";
    }
    
    int getCPUUtilization(){
        std::ifstream statFile("/proc/stat");
        if (!statFile.is_open()){
            std::cerr << "Fail to retrieve stat file" << std::endl;
            return -1;
        }

        std::string line;
        std::getline(statFile, line);
        statFile.close();

        line = line.substr(line.find("cpu") + 3);
        std::vector<std::string> tokens;
        std::istringstream tokenStream(line);
        std::copy(std::istream_iterator<std::string>(tokenStream),std::istream_iterator<std::string>(),std::back_inserter(tokens));
        int total = 0;
        for (const::std::string& token : tokens){
            total += std::stoi(token);
        }

        int idle = std::stoi(tokens[3]);
        int utilization = 100 - ((idle * 100)/total);
        return utilization;
    }

    int getNumberOfProcesses() {
        int count = 0;
        DIR* procDir = opendir("/proc");
        if (procDir == nullptr) {
            std::cerr << "Failed to open /proc directory." << std::endl;
            return count;
        }

        struct dirent* entry;
        while ((entry = readdir(procDir)) != nullptr) {
            if (entry->d_type == DT_DIR) {
                std::string procName(entry->d_name);
                if (std::all_of(procName.begin(), procName.end(), ::isdigit)) {
                    count++;
                }
            }
    }

    closedir(procDir);
    return count;
    }

    int getNumberOfThreads() {
        std::ifstream statFile("/proc/stat");
        if (!statFile.is_open()) {
            std::cerr << "Failed to open stat file." << std::endl;
            return -1;
        }
        std::string line;
        while (std::getline(statFile, line)) {
            if (line.substr(0, 9) == "processes") {
                statFile.close();
                return std::stoi(line.substr(line.find(" ") + 1)) - getNumberOfProcesses();
            }
        }

        statFile.close();
    }

    int getNumberOfHandles() {
        std::ifstream file("/proc/sys/fs/file-nr");
        if (!file.is_open()) {
            std::cerr << "Failed to open file-nr." << std::endl;
            return -1;
        }

        int handles;
        file >> handles;
        file.close();
        return handles;
    }

    int getNumberOfLogicalProcessors() {
        return sysconf(_SC_NPROCESSORS_ONLN);
    }

    int getNumberOfCores() {
        std::ifstream cpuinfoFile("/proc/cpuinfo");
        if (!cpuinfoFile.is_open()) {
            std::cerr << "Failed to open CPU info file." << std::endl;
            return -1;
        }

        std::string line;
        int count = 0;
        while (std::getline(cpuinfoFile, line)) {
            if (line.substr(0, 9) == "processor") {
                count++;
            }
        }

        cpuinfoFile.close();
        return count;
    }

    std::string getCPUFrequency() {
        std::ifstream cpuinfoFile("/proc/cpuinfo");
        if (!cpuinfoFile.is_open()) {
            std::cerr << "Failed to open CPU info file." << std::endl;
            return "";
        }

        std::string line;
        while (std::getline(cpuinfoFile, line)) {
            if (line.substr(0, 7) == "cpu MHz") {
                cpuinfoFile.close();
                return line.substr(line.find(":") + 2);
            }
        }

        cpuinfoFile.close();
        return "";
    }

    void displayCPUInformation() {
        std::string cpuName = getCPUName();
        int cpuUtilization = getCPUUtilization();
        int numberOfProcesses = getNumberOfProcesses();
        int numberOfThreads = getNumberOfThreads();
        int numberOfHandles = getNumberOfHandles();
        int numberOfLogicalProcessors = getNumberOfLogicalProcessors();
        int numberOfCores = getNumberOfCores();
        std::string cpuFrequency = getCPUFrequency();

        std::cout << "CPU Information" << std::endl;
        std::cout << "---------------" << std::endl;
        std::cout << "CPU Name: " << cpuName << std::endl;
        std::cout << "CPU Utilization: " << cpuUtilization << "%" << std::endl;
        std::cout << "Number of Processes: " << numberOfProcesses << std::endl;
        std::cout << "Number of Threads: " << numberOfThreads << std::endl;
        std::cout << "Number of Handles: " << numberOfHandles << std::endl;
        std::cout << "Number of Logical Processors: " << numberOfLogicalProcessors << std::endl;
        std::cout << "Number of Cores: " << numberOfCores << std::endl;
        std::cout << "CPU Frequency: " << cpuFrequency << " MHz" << std::endl;
    }

}
#endif