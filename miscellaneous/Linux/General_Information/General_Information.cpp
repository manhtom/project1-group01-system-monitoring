#include "General_Information.hpp"
#include <chrono>
#include <thread>
#include <cmath>
using namespace get_general_information_linux;
int main() {
    // CPU usage
    CPU_stats t1 = read_cpu_data();

    std::this_thread::sleep_for(std::chrono::milliseconds(1000));

    CPU_stats t2 = read_cpu_data();

    std::cout << "CPU usage is " << std::round(100.0f * get_cpu_usage(t1, t2)) << "%\n";
    //Display General Information
    // Memory usage
    auto memory_data = read_memory_data();
    //std::cout << memory_data.available_memory << "\n";
    std::cout << "Swap usage is " << std::round(100.0f * memory_data.get_swap_usage()) << "%\n";

    std::cout << "Memory usage is " << std::round(100.0f * memory_data.get_memory_usage()) << "%\n";

    // Disk usage
    std::cout << "Disk usage is " << std::round(100.0f * get_disk_usage("/")) << "%\n";

    // Network usage
    std::string Network = getProcessNetStats();
    std::cout << "Network statistics: \n" << Network << "\n";
    //User logged in
    std::string currentuser = getCurrentUser();
    std::cout << "User logged in: " << currentuser << "\n";
    //Current local time
    std::string currentTime = getCurrentLocalTime();
    std::cout << "Current local time: " << currentTime << std::endl;
}