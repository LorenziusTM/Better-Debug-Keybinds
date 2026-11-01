param(
	[Parameter(Mandatory = $true)]
	[string]$MinecraftVersion
)

$ErrorActionPreference = 'Stop'
$repositoryRoot = (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path
$versionMatrix = Get-Content (Join-Path $PSScriptRoot '..\versions.json') -Raw | ConvertFrom-Json
$supportedVersion = $versionMatrix | Where-Object { $_.minecraft -eq $MinecraftVersion }

if ($null -eq $supportedVersion) {
	throw "Minecraft $MinecraftVersion is not in scripts\versions.json."
}

Push-Location $repositoryRoot
try {
	$branch = & git branch --show-current
	if ($LASTEXITCODE -ne 0) {
		throw 'Could not determine the current Git branch.'
	}
	if ($branch -ne 'main') {
		throw 'Release scripts must be run from the main branch.'
	}

	$workingTree = & git status --porcelain
	if ($LASTEXITCODE -ne 0) {
		throw 'Could not inspect the Git working tree.'
	}
	if ($workingTree) {
		throw 'Commit or discard all working-tree changes before publishing a release.'
	}

	$head = & git rev-parse HEAD
	if ($LASTEXITCODE -ne 0) {
		throw 'Could not determine the current commit.'
	}
	$remoteMain = & git ls-remote origin refs/heads/main
	if ($LASTEXITCODE -ne 0) {
		throw 'Could not read origin/main. Check your remote and GitHub access.'
	}
	$remoteMainCommit = ($remoteMain -split '\s+')[0]
	if (-not $remoteMainCommit -or $head -ne $remoteMainCommit) {
		throw 'Update your local main branch to match origin/main before publishing.'
	}

	$releaseDate = [DateTime]::UtcNow.ToString(
			'yyyy.dd.MM',
			[Globalization.CultureInfo]::InvariantCulture)
	$prefix = "betterdebugkeybinds-v$MinecraftVersion-$releaseDate."
	$remoteTags = & git ls-remote --tags origin "refs/tags/${prefix}*"
	if ($LASTEXITCODE -ne 0) {
		throw 'Could not list release tags on origin.'
	}

	$counter = -1
	foreach ($remoteTag in $remoteTags) {
		$reference = ($remoteTag -split '\s+')[1]
		$pattern = '^refs/tags/' + [regex]::Escape($prefix) + '([0-9]+)$'
		if ($reference -match $pattern) {
			$existingCounter = [int]$Matches[1]
			if ($existingCounter -gt $counter) {
				$counter = $existingCounter
			}
		}
	}

	$tag = "$prefix$($counter + 1)"
	& git show-ref --verify --quiet "refs/tags/$tag"
	if ($LASTEXITCODE -eq 0) {
		throw "The local tag $tag already exists. Remove or inspect it before retrying."
	}

	Write-Host "Publishing Minecraft $MinecraftVersion from $head as $tag"
	& git tag --annotate $tag --message "Release Better Debug Keybinds for Minecraft $MinecraftVersion"
	if ($LASTEXITCODE -ne 0) {
		throw "Could not create tag $tag."
	}
	& git push origin "refs/tags/$tag"
	if ($LASTEXITCODE -ne 0) {
		throw "Could not push tag $tag. The local tag was kept; inspect it before retrying."
	}

	Write-Host "Pushed $tag. GitHub Actions will build and publish this Minecraft version."
} finally {
	Pop-Location
}
