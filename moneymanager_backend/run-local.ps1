# Load environment variables from .env file and run Spring Boot
Write-Host "Loading environment variables..." -ForegroundColor Cyan

# Read .env file and set environment variables
Get-Content .env | ForEach-Object {
    if ($_ -match '^([^=]+)=(.*)$') {
        $name = $matches[1].Trim()
        $value = $matches[2].Trim()
        [System.Environment]::SetEnvironmentVariable($name, $value, [System.EnvironmentVariableTarget]::Process)
        Write-Host "✓ Set $name" -ForegroundColor Green
    }
}

Write-Host "`nStarting Spring Boot application..." -ForegroundColor Cyan
mvn spring-boot:run
