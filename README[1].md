# Smart Student Performance Manager

## Overview
A modular Java 17 console application for student records, courses, marks, attendance, analytics, and reports.

## Problem Statement
Manual academic record handling can make CRUD operations, calculations, and reporting repetitive. This project centralizes these operations in one validated Java system.

## Major Functional Modules
1. Student Management – add, list, update, delete.
2. Course Management – add and list courses.
3. Academic Records – marks and attendance.
4. Reporting & Analytics – student report and class summary.
5. Persistence & Validation – CSV storage and reusable input rules.

## Technologies
Java 17, Maven, JUnit 5, CSV file storage, Git/GitHub.

## Run
Requirements: JDK 17+ and Maven 3.8+.

```bash
mvn clean test
mvn package
java -cp target/classes com.vit.smartstudent.Main
```

## Project Structure
```text
src/main/java/com/vit/smartstudent/
├── Main.java
├── model/Student.java
├── model/Course.java
├── model/MarkRecord.java
├── model/AttendanceRecord.java
├── service/DataStore.java
├── service/StudentService.java
├── service/CourseService.java
├── service/AcademicService.java
├── service/ReportService.java
└── util/InputValidator.java
src/test/java/com/vit/smartstudent/
├── InputValidatorTest.java
└── AttendanceRecordTest.java
```

## Testing
Run `mvn clean test`. Tests cover marks validation, attendance validation, attendance percentage calculation, and the zero-total edge case.

## Design Artefacts
The PDF report in `docs/Project_Report.pdf` contains problem statement, objectives, requirements, architecture, workflow, use case, class/component, sequence, storage design, implementation details, testing, challenges, learnings, future enhancements, references, and a rubric coverage matrix.

## Java Concepts Demonstrated
Encapsulation, constructors, classes/objects, collections, streams, exception handling, file I/O, modular service design, validation, and unit testing.

## Future Enhancements
JavaFX GUI, MySQL/PostgreSQL, authentication, role-based access, charts, PDF export, search/filtering, and notifications.
