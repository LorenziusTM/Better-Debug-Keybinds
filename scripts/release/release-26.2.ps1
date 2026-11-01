$ErrorActionPreference = 'Stop'
& (Join-Path $PSScriptRoot 'publish-version.ps1') -MinecraftVersion '26.2'
