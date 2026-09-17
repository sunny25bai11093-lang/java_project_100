# 1. Containerization (Dockerfile)
cat << 'EOF' > Dockerfile
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/smart-student-performance-manager-1.0.0.jar app.jar
COPY data ./data
ENTRYPOINT ["java", "-jar", "app.jar"]
EOF

# 2. Docker Compose (docker-compose.yml)
cat << 'EOF' > docker-compose.yml
version: '3.8'
services:
  sspm-app:
    build: .
    stdin_open: true
    tty: true
    volumes:
      - ./data:/app/data
EOF

# 3. Automation Task Runner (Makefile)
cat << 'EOF' > Makefile
.PHONY: build test run clean docker-build docker-run

build:
	mvn clean compile

test:
	mvn clean test

run:
	mvn exec:java -Dexec.mainClass="com.vit.smartstudent.Main"

package:
	mvn clean package

clean:
	mvn clean

docker-build:
	docker build -t sspm-app:latest .

docker-run:
	docker run -it --rm -v $(PWD)/data:/app/data sspm-app:latest
EOF

# 4. GitHub Issue Template - Bug Report
mkdir -p .github/ISSUE_TEMPLATE
cat << 'EOF' > .github/ISSUE_TEMPLATE/bug_report.md
---
name: Bug Report
about: Create a report to help reproduce and fix a defect.
title: "[BUG] "
labels: bug
assignees: sunny25bai11093-lang
---

**Describe the Bug**
A clear and concise description of what the bug is.

**To Reproduce**
1. Run application via `run.sh` or `Main.java`
2. Select menu option `...`
3. Enter input `...`
4. See error

**Expected Behavior**
What you expected to happen.

**Log/Console Output**
Paste console trace if applicable.
EOF

# 5. GitHub Issue Template - Feature Request
cat << 'EOF' > .github/ISSUE_TEMPLATE/feature_request.md
---
name: Feature Request
about: Suggest an idea or enhancement for SSPM.
title: "[FEAT] "
labels: enhancement
assignees: sunny25bai11093-lang
---

**Proposed Feature**
A clear description of what should be added (e.g., MySQL integration, JavaFX UI).

**Motivation / Use Case**
Why this feature is useful for academic records management.

**Suggested Implementation Details**
Any specific classes, algorithms, or libraries to utilize.
EOF

# 6. Pull Request Template
cat << 'EOF' > .github/PULL_REQUEST_TEMPLATE.md
## Summary of Changes
- Briefly describe the fixes or features introduced.

## Checklist
- [ ] Code builds cleanly with `mvn clean compile`.
- [ ] All JUnit 5 tests pass with `mvn clean test`.
- [ ] Input validation applied via `InputValidator`.
- [ ] CSV schemas updated and verified.
EOF

# 7. Project Changelog (CHANGELOG.md)
cat << 'EOF' > CHANGELOG.md
# Changelog

All notable changes to the **Smart Student Performance Manager** project are documented here.

## [1.0.0] - 2026-09-17
### Added
- Domain models: `Student`, `Course`, `MarkRecord`, `AttendanceRecord`.
- Service layer: CRUD actions, automated reports, and cohort summaries.
- Safe CSV persistence using `java.nio.file`.
- Input validation safeguards for registration numbers, emails, and score limits.
- Automated unit test suite with JUnit 5.
- Continuous Integration via GitHub Actions.
- Containerization support via `Dockerfile` and `docker-compose.yml`.
EOF

# 8. Security Policy (SECURITY.md)
cat << 'EOF' > SECURITY.md
# Security Policy

## Supported Versions
| Version | Supported          |
| ------- | ------------------ |
| 1.0.0   | :white_check_mark: |

## Reporting a Vulnerability
If you discover an issue related to data validation bypass or storage corruption:
1. Open a confidential GitHub advisory or contact the maintainer directly.
2. Provide input payloads used to trigger the unexpected application behavior.
EOF

# 9. Local Git Pre-Commit Hook (Prevents committing code that breaks JUnit tests)
mkdir -p .githooks
cat << 'EOF' > .githooks/pre-commit
#!/bin/sh
echo "[*] Running automated test suite before commit..."
mvn test -q
if [ $? -ne 0 ]; then
    echo "[!] Unit tests failed. Commit aborted."
    exit 1
fi
echo "[✓] All tests passed."
EOF
chmod +x .githooks/pre-commit
git config core.hooksPath .githooks

# Commit and push all added files to GitHub
git add .
git commit -m "feat: add Dockerfile, compose, templates, Makefile, and security policy"
git push origin main