#include "Driver_Letter.hpp"

using namespace get_driver_linux;

int main() {
    //Drive letter(+Label)
    std::vector<DriveInfo> driveList = getDriveInfo();
    displayDriveInfo(driveList);
    return 0;
}