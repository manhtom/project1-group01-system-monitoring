#include <stdio.h>
#include <windows.h>

typedef LONG NTSTATUS, *PNTSTATUS;
#define STATUS_SUCCESS (0x00000000)

typedef NTSTATUS (WINAPI* RtlGetVersionPtr)(PRTL_OSVERSIONINFOEXW);

RTL_OSVERSIONINFOEXW GetRealOSVersion() {
    HMODULE hMod = ::GetModuleHandleW(L"ntdll.dll");
    if (hMod) {
        RtlGetVersionPtr fxPtr = (RtlGetVersionPtr)::GetProcAddress(hMod, "RtlGetVersion");
        if (fxPtr != nullptr) {
            RTL_OSVERSIONINFOEXW rovi = { 0 };
            rovi.dwOSVersionInfoSize = sizeof(rovi);
            if ( STATUS_SUCCESS == fxPtr(&rovi) ) {
                return rovi;
            }
        }
    }
    RTL_OSVERSIONINFOEXW rovi = { 0 };
    return rovi;
}

int main(void) {
    RTL_OSVERSIONINFOEXW osversion = GetRealOSVersion();
    printf("%u.%u.%u.%u",osversion.dwMajorVersion, osversion.dwMinorVersion, osversion.dwBuildNumber, osversion.szCSDVersion[128]);
}