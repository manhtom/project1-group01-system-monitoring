#include <windows.h>
#include <windef.h>
#include <stdio.h>
#include <tchar.h>
#include <stdlib.h>

void _tgetVolInfo(char driveName){
    char        drivePath[] = "?:\\";
    TCHAR  FileSystem[32] = TEXT("");
    DWORD fsSize = _countof(FileSystem);
    TCHAR VolName[32] = TEXT("");
    DWORD vnSize = _countof(VolName);
    drivePath[0] = driveName;
     int dtype = GetDriveTypeA(drivePath);
    GetVolumeInformationA(drivePath,VolName, vnSize, NULL, NULL, NULL, FileSystem, fsSize);
    printf(" Volume Name: %s\n", VolName);
    printf(" File System: %s\n", FileSystem);
    }

void getDriveInfo()
{
    // Disk Usage
    char        drivePath[] = "?:\\";
    char        driveName;
    for( driveName = 'A'; driveName <= 'Z'; driveName++ ) 
    {
        drivePath[0] = driveName;
        int dtype = GetDriveTypeA(drivePath);

        if(dtype == DRIVE_FIXED || dtype == DRIVE_RAMDISK || dtype == DRIVE_REMOVABLE || dtype == DRIVE_CDROM) 
        {
            ULARGE_INTEGER diskAvailStruct;
            ULARGE_INTEGER diskTotalStruct;
            diskAvailStruct.QuadPart = 0;
            diskTotalStruct.QuadPart = 0;
            GetDiskFreeSpaceExA(drivePath, &diskAvailStruct, &diskTotalStruct, 0);
            double DiskSize = diskTotalStruct.QuadPart / 1024.0; 
            double FreeSize = diskAvailStruct.QuadPart / 1024.0;

            printf("%s:\n", drivePath);
            printf(" Total disk space: %.0f\n", DiskSize);
            printf(" Disk space used: %.0f\n", DiskSize-FreeSize);
            printf(" Disk space available: %.0f\n", FreeSize);
            printf(" Disk Type: %u\n", GetDriveTypeA(drivePath));
            _tgetVolInfo(driveName);

        }
    }
}



int main(void) {
    getDriveInfo();
}