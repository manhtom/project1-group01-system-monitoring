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
            processStats += "Data received (bytes/s): " + stats[1] + "\n";
            processStats += "Data sent (bytes/s): " + stats[9] + "\n";
            processStats += "Packet received (bytes/s): " + stats[2] + "\n";
            processStats += "Packet sent (bytes/s): " + stats[10] + "\n";
        
        }
    } else {
        processStats = "Error: Could not open file.";
    }
    NumofAdapterInstalled += "Total Number of Adapters installed: " + std::to_string(cnt);

    return NumofAdapterInstalled + "\n" + processStats;
}


int main(){
    std::string Network = getProcessNetStats();
    std::cout << "Network statistics: \n" << Network << "\n";
}