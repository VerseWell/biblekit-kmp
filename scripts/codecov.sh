#!/bin/sh

set -e

# Function to upload coverage report to codecov
# Accepts token as first parameter and file path as second parameter
upload_coverage() {
    local token=$1
    local file_path=$2
    
    # Validate token parameter
    if [ -z "$token" ]; then
        echo "Error: Token parameter is required"
        echo "Usage: $0 <codecov-token>"
        exit 1
    fi
    
    # Check if file exists
    if [ ! -f "$file_path" ]; then
        echo "Error: Coverage file does not exist: $file_path"
        exit 1
    fi
    
    # Calculate current git SHA
    local current_sha=$(git rev-parse HEAD)
    
    # Upload coverage report
    codecov upload-coverage \
        --file "$file_path" \
        -t "$token" \
        --gcov-executable gcov \
        --git-service github \
        --sha "$current_sha"
}

# Main execution
./gradlew koverXmlReportRelease
upload_coverage "$1" ./biblekit/build/reports/kover/reportRelease.xml
