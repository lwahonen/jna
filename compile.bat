call "C:\Program Files (x86)\Microsoft Visual Studio\2017\Professional\VC\Auxiliary\Build\vcvarsall.bat"  x64  
set path=%path%;F:\cygwin64\bin
cd \asemat\jna
ant native -DUSE_MSVC=true
