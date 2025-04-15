#include <windows.h>
#pragma comment(lib, "Ole32")

// Export the function using extern "C" to avoid name mangling
extern "C" __declspec(dllexport) void testGuid(void * callback) {
    GUID guid;
    HRESULT hr = CoCreateGuid(&guid);
    if (FAILED(hr)) {
        OutputDebugStringA("Failed to create GUID");
        return;
    }
    OutputDebugStringA("Created GUID successfully, calling callback");
    auto ret = ((HRESULT(__stdcall*)(GUID))callback)(guid);
    if (FAILED(ret)) {
        OutputDebugStringA("Callback failed");
    } else {
        OutputDebugStringA("Callback succeeded");
    }
}

// Optional: Provide a DllMain entry point for the DLL
BOOL APIENTRY DllMain(HMODULE hModule,
                      DWORD  ul_reason_for_call,
                      LPVOID lpReserved) {
    switch (ul_reason_for_call) {
        case DLL_PROCESS_ATTACH:
        case DLL_THREAD_ATTACH:
        case DLL_THREAD_DETACH:
        case DLL_PROCESS_DETACH:
            break;
    }
    return TRUE;
}