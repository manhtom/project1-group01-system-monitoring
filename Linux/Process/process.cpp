#include <iostream>
#include <fstream>
#include <sstream>
#include <string>
#include <cstring>
#include <dirent.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/sysinfo.h>
#include <pwd.h>
#include <iterator>
#include <vector>
#include <grp.h>
#include <proc/readproc.h>
#include <exception>
std::string getProcessName(pid_t pid) {
    std::string name;
    std::ifstream file("/proc/" + std::to_string(pid) + "/status");
    std::string line;
    while (std::getline(file, line)) {
        if (line.substr(0, 5) == "Name:") {
            name = line.substr(6);
            break;
        }
    }
    file.close();
    return name;
}

std::string getProcessPath(pid_t pid) {
    std::string path;
    char buffer[1024];
    ssize_t len = readlink(("/proc/" + std::to_string(pid) + "/exe").c_str(), buffer, sizeof(buffer) - 1);
    if (len != -1) {
        buffer[len] = '\0';
        path = buffer;
    }
    if (path != " ") return path;
    else return "N/A";
}

std::string getProcessStatus(pid_t pid) {
    std::string status;
    std::ifstream file("/proc/" + std::to_string(pid) + "/status");
    std::string line;
    while (std::getline(file, line)) {
        if (line.substr(0, 6) == "State:") {
            int pos = line.find("(");
            status = line.substr(pos + 1, line.find(")") - pos - 1);
            break;
        }
    }
    file.close();
    return status;
}


std::string getProcessOwner(int pid) {
    std::ostringstream oss;
    oss << "ps -o user= -p " << pid;
    
    FILE* pipe = popen(oss.str().c_str(), "r");
    if (!pipe) {
        throw std::runtime_error("popen() failed!");
    }
    
    char buffer[128];
    std::string result;
    
    while (!feof(pipe)) {
        if (fgets(buffer, 128, pipe) != NULL) {
            result += buffer;
        }
    }
    
    pclose(pipe);
    
    return result;
}

double getProcessCPUUsage(pid_t pid) {
    struct sysinfo info;
    if (sysinfo(&info) != 0) {
        return -1.0; // Error occurred
    }
    
    double totalTime = info.uptime * sysconf(_SC_CLK_TCK);
    std::ifstream file("/proc/" + std::to_string(pid) + "/stat");
    std::string line;
    std::getline(file, line);
    std::istringstream iss(line);
    std::istream_iterator<std::string> iter(iss), end;
    std::vector<std::string> stats(iter, end);
    file.close();
    
    double startTime = std::stod(stats[21]) / sysconf(_SC_CLK_TCK);
    double processTime = std::stod(stats[13]) + std::stod(stats[14]) + std::stod(stats[15]) + std::stod(stats[16]);
    double cpuUsage = (processTime / (totalTime - startTime)) * 100.0;
    return cpuUsage;
}

unsigned long long getProcessSharedMemory(pid_t pid) {
    std::ifstream file("/proc/" + std::to_string(pid) + "/statm");
    std::string line;
    std::getline(file,line);
    std::istringstream iss(line);
    std::istream_iterator<std::string> iter(iss), end;
    std::vector<std::string> stats(iter, end);
    unsigned long long memory = std::stoull(stats[1]);
    memory *= 1024;
    file.close();
    return memory/(1024*1024);
    //return 'N/A'; // Unable to retrieve shared memory size
}

unsigned long long getProcessPrivateMemory(pid_t pid) {
    std::ifstream file("/proc/" + std::to_string(pid) + "/status");
    std::string line;
    while (std::getline(file, line)) {
        if (line.substr(0, 7) == "VmSize:") {
            std::istringstream iss(line);
            std::string label, size, unit;
            iss >> label >> size >> unit;
            unsigned long long memory = std::stoull(size);
            if (unit == "kB") {
                memory *= 1024;
            }
            file.close();
            return memory/(1024*1024);
        }
    }
    file.close();
    return 'N/A'; // Unable to retrieve private memory size
}

unsigned long long getProcessResidentMemory(pid_t pid) {
    std::ifstream file("/proc/" + std::to_string(pid) + "/status");
    std::string line;
    while (std::getline(file, line)) {
        if (line.substr(0, 6) == "VmRSS:") {
            std::istringstream iss(line);
            std::string label, size, unit;
            iss >> label >> size >> unit;
            unsigned long long memory = std::stoull(size);
            if (unit == "kB") {
                memory *= 1024;
            }
            file.close();
            return memory/(1024*1024);
        }
    }
    file.close();
    return 'N/A'; // Unable to retrieve private memory size
}

unsigned long long getProcessDiskReadTotal(pid_t pid) {
    std::ifstream file("/proc/" + std::to_string(pid) + "/io");
    std::string line;
    while (std::getline(file, line)) {
        if (line.substr(0, 10) == "read_bytes") {
            std::istringstream iss(line);
            std::string label;
            unsigned long long bytesRead;
            iss >> label >> bytesRead;
            file.close();
            return bytesRead;
        }
    }
    file.close();
    return 'N/A'; // Unable to retrieve disk read total
}
unsigned long long getProcessDataWriteTotal(pid_t pid) {
    std::ifstream file("/proc/" + std::to_string(pid) + "/io");
    std::string line;
    while (std::getline(file, line)) {
        if (line.substr(0, 11) == "write_bytes") {
            std::istringstream iss(line);
            std::string label;
            unsigned long long bytesWritten;
            iss >> label >> bytesWritten;
            file.close();
            return bytesWritten;
        }
    }
    file.close();
    return 'N/A'; // Unable to retrieve data write total
}
std::string getProcessPriority(pid_t pid) {
    std::ifstream file("/proc/" + std::to_string(pid) + "/stat");
    std::string line;
    std::getline(file, line);
    std::istringstream iss(line);
    std::istream_iterator<std::string> iter(iss), end;
    std::vector<std::string> stats(iter, end);
    file.close();

    int priority = std::stoi(stats[17]);
    if (priority > 0 && priority < 20) {
        return "Very Low";
    } else if (priority >= 20 && priority <= 100) {
        return "Normal";
    } else if (priority > 100) {
        return "Very High";
    } else {
        return "Unknown";
    }
}
void display(int pid) {
    //pid_t pid = getpid();
    //pid_t pid = 1087;
    std::string processName = getProcessName(pid);
    std::string processPath = getProcessPath(pid);
    std::string processStatus = getProcessStatus(pid);
    std::string processUser = getProcessOwner(pid);
    double cpuUsage = getProcessCPUUsage(pid);
    unsigned long long sharedMemory = getProcessSharedMemory(pid);
    unsigned long long privateMemory = getProcessPrivateMemory(pid);
    unsigned long long residentMemory = getProcessResidentMemory(pid);
    unsigned long long diskReadTotal = getProcessDiskReadTotal(pid);
    unsigned long long dataWriteTotal = getProcessDataWriteTotal(pid);
    std::string priority = getProcessPriority(pid);
    std::cout << "Process Name: " << processName << std::endl;
    std::cout << "Process Path: " << processPath << std::endl;
    std::cout << "Process Status: " << processStatus << std::endl;
    std::cout << "PID: " << pid << std::endl;
    try {
            std::string owner = getProcessOwner(pid);
            if (!owner.empty()) {
                std::cout << "Owner: " << owner;
            } else {
                std::cout << "Unable to find owner for PID: " << pid;
            }
        } 
    catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << std::endl;
    }
    std::cout << "CPU %: " << cpuUsage << std::endl;
    std::cout << "Shared Memory: " << sharedMemory << " MB" << std::endl;
    std::cout << "Virtual Memory: " << privateMemory << " MB" << std::endl;
    std::cout << "Resident Memory: " << residentMemory << " MB" << std::endl;
    std::cout << "Disk Read Total: " << diskReadTotal/(1024*1024) << " MB" << std::endl;
    std::cout << "Data Write Total: " << dataWriteTotal/(1024*1024) << " MB" << std::endl;
    std::cout << "----------------------\n";
}

void iterateProcesses() {
    DIR* dir;
    struct dirent* entry;

    dir = opendir("/proc");
    if (dir == nullptr) {
        // Handle error
        return;
    }

    while ((entry = readdir(dir)) != nullptr) {
        if (entry->d_type == DT_DIR) {
            // Check if the directory name is a process ID
            bool isPID = true;
            for (size_t i = 0; i < strlen(entry->d_name); ++i) {
                if (!isdigit(entry->d_name[i])) {
                    isPID = false;
                    break;
                }
            }

            if (isPID) {
                int PID = std::stoi(entry->d_name);
                display(PID);
            }
        }
    }

    closedir(dir);
}

int main(){
    iterateProcesses();
    return 0;
}