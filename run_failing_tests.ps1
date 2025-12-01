# Run local COM tests for JNA platform
# Usage: .\Run local tests.ps1

Write-Host "=== Compiling platform classes ===" -ForegroundColor Cyan
Push-Location $PSScriptRoot
ant compile
if ($LASTEXITCODE -ne 0) {
    Write-Host "Compile failed!" -ForegroundColor Red
    Pop-Location
    exit 1
}

Write-Host "`n=== Compiling test classes ===" -ForegroundColor Cyan
ant compile-tests
if ($LASTEXITCODE -ne 0) {
    Write-Host "Test compile failed!" -ForegroundColor Red
    Pop-Location
    exit 1
}
Pop-Location

$jnaJar = "Z:/jna/dist/jna.jar"
$platformClasses = "Z:/jna/contrib/platform/build/classes"
$testClasses = "Z:/jna/contrib/platform/build/test-classes"
$classpath = "$jnaJar;$platformClasses;$testClasses"

Write-Host "`n=== Running VarDescBug ===" -ForegroundColor Cyan
java -cp $classpath com.sun.jna.platform.win32.COM.VarDescBug
$varDescResult = $LASTEXITCODE

Write-Host "`n=== Running InvalidMemoryAccessBug ===" -ForegroundColor Cyan
java -cp $classpath com.sun.jna.platform.win32.COM.InvalidMemoryAccessBug
$invalidMemResult = $LASTEXITCODE

Write-Host "`n=== Results ===" -ForegroundColor Cyan
if ($varDescResult -eq 0) {
    Write-Host "VarDescBug: PASSED" -ForegroundColor Green
} else {
    Write-Host "VarDescBug: FAILED (exit code $varDescResult)" -ForegroundColor Red
}

if ($invalidMemResult -eq 0) {
    Write-Host "InvalidMemoryAccessBug: PASSED" -ForegroundColor Green
} else {
    Write-Host "InvalidMemoryAccessBug: FAILED (exit code $invalidMemResult)" -ForegroundColor Red
}
