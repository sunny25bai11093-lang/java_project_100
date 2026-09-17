# Run this entire block in Git Bash / Terminal to generate all files instantly:

# 1. CI Workflow
mkdir -p .github/workflows
cat << 'EOF' > .github/workflows/maven-build.yml
name: Java CI with Maven

on:
  push:
    branches: [ "main", "master" ]
  pull_request:
    branches: [ "main", "master" ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - name: Check out repository
      uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven

    - name: Build with Maven
      run: mvn -B clean package --file pom.xml

    - name: Run Unit Tests
      run: mvn test
EOF

# 2. MIT License
cat << 'EOF' > LICENSE
MIT License

Copyright (c) 2026 Sunny Gupta (Reg No: 25BAI11093)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
EOF

# 3. Contributing Guidelines
cat << 'EOF' > CONTRIBUTING.md
# Contributing to Smart Student Performance Manager (SSPM)

Thank you for your interest in improving SSPM!

## Development Standards
- **Java Version:** Ensure you are developing using Java 17 LTS.
- **Code Style:** Adhere to standard Java naming conventions:
  - CamelCase for class names (`StudentService`)
  - mixedCase for methods and variables (`calculateAverage`)
- **Persistence Safety:** Never bypass `InputValidator` before writing new entries to `.csv` stores.

## Testing Guidelines
- All new service methods must include corresponding JUnit 5 assertions under `src/test/java/`.
- Verify the test suite passes before raising a PR:
  ```bash
  mvn clean test