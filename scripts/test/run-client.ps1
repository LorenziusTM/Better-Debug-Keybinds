param(
	[Parameter(Mandatory = $true)]
	[string]$MinecraftVersion
)

$ErrorActionPreference = 'Stop'
$repositoryRoot = (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path
$versionMatrix = Get-Content (Join-Path $PSScriptRoot '..\versions.json') -Raw | ConvertFrom-Json
$version = $versionMatrix | Where-Object { $_.minecraft -eq $MinecraftVersion }

if ($null -eq $version) {
	throw "Minecraft $MinecraftVersion is not in scripts\versions.json."
}

$javaPath = if ($env:JAVA_HOME) {
	Join-Path $env:JAVA_HOME 'bin\java.exe'
} else {
	(Get-Command java.exe -ErrorAction Stop).Source
}
if (-not (Test-Path $javaPath)) {
	throw "Java executable not found at $javaPath."
}

$javaProcess = New-Object System.Diagnostics.Process
$javaProcess.StartInfo = New-Object System.Diagnostics.ProcessStartInfo
$javaProcess.StartInfo.FileName = $javaPath
$javaProcess.StartInfo.Arguments = '-version'
$javaProcess.StartInfo.UseShellExecute = $false
$javaProcess.StartInfo.RedirectStandardOutput = $true
$javaProcess.StartInfo.RedirectStandardError = $true
if (-not $javaProcess.Start()) {
	throw "Could not start Java at $javaPath."
}
$stdoutTask = $javaProcess.StandardOutput.ReadToEndAsync()
$stderrTask = $javaProcess.StandardError.ReadToEndAsync()
$javaProcess.WaitForExit()
$javaOutput = $stdoutTask.GetAwaiter().GetResult() + $stderrTask.GetAwaiter().GetResult()
$javaExitCode = $javaProcess.ExitCode
$javaProcess.Dispose()

if ($javaExitCode -ne 0) {
	throw "Java version check failed with exit code $javaExitCode."
}
if ($javaOutput -notmatch 'version "(\d+)') {
	throw "Could not determine the Java version from $javaPath."
}
if ([int]$Matches[1] -lt 25) {
	throw 'Fabric Loom requires Java 25 to run Gradle. Set JAVA_HOME to a Java 25 installation, then retry.'
}

Push-Location $repositoryRoot
try {
	Write-Host "Starting Minecraft $MinecraftVersion for manual testing..."
	& (Join-Path $repositoryRoot 'gradlew.bat') runClient `
			"-Pminecraft_version=$($version.minecraft)" `
			"-Ploader_version=$($version.loader)" `
			"-Pfabric_api_version=$($version.fabricApi)" `
			"-Pjava_version=$($version.java)"
	if ($LASTEXITCODE -ne 0) {
		throw "Gradle runClient failed for Minecraft $MinecraftVersion with exit code $LASTEXITCODE."
	}
} finally {
	Pop-Location
}
