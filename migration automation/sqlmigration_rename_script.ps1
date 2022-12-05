

$files = Get-ChildItem "C:\Users\DELL\Desktop\MSSQL" | Sort-Object -CaseSensitive

$i = 1
foreach ($file in $files){
    $newname = "V$i"+ "__create_"+ $file.Name.Replace("dbo.","").Replace(".Table","")
    Rename-Item $($file.FullName) $newname
    $i++
}