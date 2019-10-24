set path=c:\cygwin\bin;%path%
z:

cd \diktamen\jna
call "C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\vcvarsall.bat"  x86
set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.8.0_181
ant native -DUSE_MSVC=true
