call "C:\Program Files\Microsoft Visual Studio\2022\Professional\VC\Auxiliary\Build\vcvarsall.bat" x86
cd win32-x86
cl /LD /Zi /Od ..\callback.cpp /link /DEBUG /MACHINE:x86 /Fe:callback.dll
cd ..

call "C:\Program Files\Microsoft Visual Studio\2022\Professional\VC\Auxiliary\Build\vcvarsall.bat" amd64
cd win32-x86-64
cl /LD /Zi /Od ..\callback.cpp /link /DEBUG /MACHINE:x64 /Fe:callback.dll
cd ..

call "C:\Program Files\Microsoft Visual Studio\2022\Professional\VC\Auxiliary\Build\vcvarsall.bat" arm64
cd win32-aarch64
cl /LD /Zi /Od ..\callback.cpp /link /DEBUG /MACHINE:ARM64 /Fe:callback.dll
cd ..
