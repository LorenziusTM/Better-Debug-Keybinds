$ErrorActionPreference = 'Stop'
& (Join-Path $PSScriptRoot 'publish-version.ps1') -MinecraftVersion '1.21.11'
