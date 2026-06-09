param(
  [Parameter(Mandatory = $true)]
  [string]$Path
)

$resolved = Resolve-Path -LiteralPath $Path
$bytes = [System.IO.File]::ReadAllBytes($resolved)
[Convert]::ToBase64String($bytes)
