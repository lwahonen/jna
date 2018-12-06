call "C:\Program Files (x86)\Microsoft Visual Studio\2017\Professional\VC\Auxiliary\Build\vcvarsall.bat"  x86
set path=%path%;C:\cygwin64\bin
cd \asemat\jna
ant -DUSE_MSVC=true
