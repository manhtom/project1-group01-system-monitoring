#ifndef _GET_DRIVER_LINUX_
#define _GET_DRIVER_LINUX_

#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <iterator>
#include <string.h>
#include <cstring>
namespace get_driver_linux
{

   struct DriveInfo {
        std::string mountPoint;
        std::string fileSystem;
        std::string usedSpace;
        std::string totalSpace;
        std::string activeTime;
    };

    std::vector<DriveInfo> getDriveInfo() {
        std::ifstream mountsFile("/proc/mounts");
        if (!mountsFile.is_open()) {
            std::cerr << "Failed to open /proc/mounts." << std::endl;
            return {};
        }
        
        std::vector<DriveInfo> driveList;
        std::string line;
        
        while (std::getline(mountsFile, line)) {
            std::istringstream lineStream(line);
            std::string device, mountPoint, fileSystem, options;
            lineStream >> device >> mountPoint >> fileSystem >> options;
            
            // Skip pseudo file systems and non-readable entries
            if (fileSystem.substr(0, 5) != "rootfs" && options.find("ro") == std::string::npos) {
                DriveInfo driveInfo;
                driveInfo.mountPoint = mountPoint;
                driveInfo.fileSystem = fileSystem;
                
                // Execute 'df' command to get drive space information
                std::string dfCommand = "df --output=used,size " + mountPoint;
                FILE* pipe = popen(dfCommand.c_str(), "r");
                
                if (pipe) {
                    char buffer[128];
                    std::fgets(buffer, sizeof(buffer), pipe); // Skip the header
                    
                    // Read the used space and total space
                    std::fgets(buffer, sizeof(buffer), pipe);
                    std::istringstream spaceStream(buffer);
                    spaceStream >> driveInfo.usedSpace >> driveInfo.totalSpace;
                    
                    pclose(pipe);
                }
                
                // Execute 'stat' command to get drive active time information
                std::string statCommand = "stat -c %X " + mountPoint;
                pipe = popen(statCommand.c_str(), "r");
                
                if (pipe) {
                    char buffer[128];
                    std::fgets(buffer, sizeof(buffer), pipe);
                    driveInfo.activeTime = buffer;
                    pclose(pipe);
                }
                
                driveList.push_back(driveInfo);
            }
        }
        
        mountsFile.close();
        return driveList;
    }

    void displayDriveInfo(const std::vector<DriveInfo>& driveList) {
        std::cout << "Drive Information" << std::endl;
        std::cout << "----------------" << std::endl;
        
        for (const DriveInfo& drive : driveList) {
            std::cout << "Mount Point: " << drive.mountPoint << std::endl;
            std::cout << "File System: " << drive.fileSystem << std::endl;
            std::cout << "Used Space: " << drive.usedSpace << std::endl;
            std::cout << "Total Space: " << drive.totalSpace << std::endl;
            std::cout << "Active Time: " << drive.activeTime;
            std::cout << "----------------" << std::endl;
        }
    }


}

#endif