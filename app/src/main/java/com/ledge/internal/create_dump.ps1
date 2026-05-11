$projectPath = "$env:USERPROFILE\AndroidStudioProjects\Ledge\app\src\main"
$outputFile = "$PSScriptRoot\dump.txt"
$maxKB = 400

$ext = "*.kt","*.java","*.xml","*.json","*.gradle","*.kts","*.properties"
$exclude = "build",".gradle",".idea","generated"

Remove-Item $outputFile -ErrorAction Ignore

Get-ChildItem $projectPath -Recurse -Include $ext -File | ForEach-Object {

    $path = $_.FullName

    if ($exclude | Where-Object { $path -match "\\$_\\" }) { return }
    if ($path -notmatch "\\src\\") { return }
    if ($_.Length -gt ($maxKB * 1KB)) { return }

    Add-Content $outputFile "`n===== FILE: $path =====`n"

    try {
        Get-Content $path -Raw | Add-Content $outputFile
    }
    catch {
        Add-Content $outputFile "[ERROR]"
    }
}

Write-Host "Dump Created -> $outputFile"