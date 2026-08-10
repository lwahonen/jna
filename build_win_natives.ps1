# Build JNA native bits (jnidispatch.dll) for Windows x86, x64, and aarch64.
# Run from the JNA root directory in PowerShell.
# powershell.exe -noprofile -executionpolicy bypass -file .\buidl_win_natives.ps1
#
# Prerequisites:
#   - Visual Studio with C++ build tools (x86, x64, arm64)
#   - Cygwin with: make, automake, libtool, gcc-g++,
#     mingw64-x86_64-gcc-g++, mingw64-x86_64-gcc-core,
#     mingw64-i686-gcc-g++, mingw64-i686-gcc-core
#   - Apache Ant
#   - JDK 8 (64-bit)

param(
    [string]$JavaHome = "C:\Program Files\Amazon Corretto\jdk1.8.0_482",
    [string]$AntHome = "C:\ant",
    [string]$CygwinHome = "C:\cygwin64"
)

$ErrorActionPreference = "Stop"

# Find Visual Studio via vswhere
$vswhere = "${env:ProgramFiles(x86)}\Microsoft Visual Studio\Installer\vswhere.exe"
if (-not (Test-Path $vswhere)) {
    Write-Error "vswhere.exe not found. Is Visual Studio installed?"
    exit 1
}
$vsPath = & $vswhere -latest -property installationPath
$vcvarsall = "$vsPath\VC\Auxiliary\Build\vcvarsall.bat"
if (-not (Test-Path $vcvarsall)) {
    Write-Error "vcvarsall.bat not found at $vcvarsall"
    exit 1
}

Write-Host "Visual Studio: $vsPath"
Write-Host "JAVA_HOME:     $JavaHome"
Write-Host "Ant:           $AntHome"
Write-Host "Cygwin:        $CygwinHome"
Write-Host ""

# Architectures: vcvarsall arg, ant os.prefix
$targets = @(
    @{ Name = "x86";     VcArch = "x64_x86";  OsPrefix = "win32-x86" },
    @{ Name = "x86_64";  VcArch = "x64";      OsPrefix = "win32-x86-64" },
    @{ Name = "aarch64"; VcArch = "x64_arm64"; OsPrefix = "win32-aarch64" }
)

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path

foreach ($target in $targets) {
    Write-Host "============================================"
    Write-Host "Building $($target.Name) ($($target.OsPrefix))"
    Write-Host "============================================"

    # PATH order matters: cygwin provides make/sh, vcvarsall is called LAST
    # so MSVC tools (cl.exe, link.exe, ml64.exe) take precedence over
    # cygwin's link command.
    $buildCmd = @"
set "JAVA_HOME=$JavaHome"
set "PATH=$AntHome\bin;$CygwinHome\bin;%PATH%"
call "$vcvarsall" $($target.VcArch)
cd /d "$scriptDir"
ant -Dos.prefix=$($target.OsPrefix) native
"@

    $tmpBat = "$env:TEMP\jna_build_$($target.Name).bat"
    $buildCmd | Out-File -FilePath $tmpBat -Encoding ascii
    cmd /c $tmpBat
    $exitCode = $LASTEXITCODE
    Remove-Item $tmpBat -ErrorAction SilentlyContinue

    if ($exitCode -ne 0) {
        Write-Error "Build failed for $($target.Name) with exit code $exitCode"
        exit $exitCode
    }

    Write-Host ""
    Write-Host "$($target.Name) build succeeded."
    Write-Host ""
}

Write-Host "============================================"
Write-Host "All builds completed successfully."
Write-Host "============================================"
Write-Host ""
Write-Host "Output:"
foreach ($target in $targets) {
    $dll = "$scriptDir\build\native-$($target.OsPrefix)\jnidispatch.dll"
    $pdb = "$scriptDir\build\native-$($target.OsPrefix)\jnidispatch.pdb"
    if (Test-Path $dll) {
        $size = (Get-Item $dll).Length / 1KB
        Write-Host "  $($target.OsPrefix): $dll ($([math]::Round($size, 1)) KB)"
        if (Test-Path $pdb) {
            $pdbSize = (Get-Item $pdb).Length / 1KB
            Write-Host "               PDB: $pdb ($([math]::Round($pdbSize, 1)) KB)"
        }
    } else {
        Write-Host "  $($target.OsPrefix): NOT FOUND"
    }
}
