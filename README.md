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
package com.vit.smartstudent;

import com.vit.smartstudent.model.Course;
import com.vit.smartstudent.model.Student;
import com.vit.smartstudent.service.*;
import com.vit.smartstudent.util.InputValidator;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService studentService = new StudentService();
    private static final CourseService courseService = new CourseService();
    private static final AcademicService academicService = new AcademicService();
    private static final ReportService reportService = 
        new ReportService(studentService, courseService, academicService);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            displayMenu();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addStudent();
                case "2" -> listStudents();
                case "3" -> updateStudent();
                case "4" -> deleteStudent();
                case "5" -> addCourse();
                case "6" -> listCourses();
                case "7" -> recordMarks();
                case "8" -> recordAttendance();
                case "9" -> generateReport();
                case "10" -> reportService.printClassSummary();
                case "0" -> {
                    System.out.println("Exiting Smart Student Performance Manager. Goodbye!");
                    exit = true;
                }
                default -> System.out.println("[!] Invalid option. Please select 0 through 10.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("""
        ======================================================================
                         SMART STUDENT PERFORMANCE MANAGER                   
                       VIT Bhopal - Programming in Java Project              
        ======================================================================
        1. Add Student               6. List Courses
        2. List Students             7. Record/Update Marks
        3. Update Student            8. Record/Update Attendance
        4. Delete Student            9. Generate Student Performance Report
        5. Add Course               10. Generate Class Summary Report
        0. Exit System
        ----------------------------------------------------------------------""");
    }

    private static void addStudent() {
        System.out.print("Enter Registration No (e.g., 25BAI11093): ");
        String id = scanner.nextLine().trim();
        if (!InputValidator.isValidId(id)) {
            System.out.println("[!] Error: Invalid Registration Number pattern.");
            return;
        }
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Institutional Email: ");
        String email = scanner.nextLine().trim();
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("[!] Error: Invalid email address format.");
            return;
        }
        System.out.print("Enter Academic Branch: ");
        String branch = scanner.nextLine().trim();
        System.out.print("Enter Current Semester (1-10): ");
        int sem;
        try {
            sem = Integer.parseInt(scanner.nextLine().trim());
            if (!InputValidator.isValidSemester(sem)) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("[!] Error: Semester must be an integer between 1 and 10.");
            return;
        }

        boolean success = studentService.addStudent(new Student(id, name, email, branch, sem));
        if (success) System.out.println("[✓] Student enrolled successfully.");
        else System.out.println("[!] Error: Student with ID " + id + " already exists.");
    }

    private static void listStudents() {
        List<Student> list = studentService.getAllStudents();
        System.out.println("\n----------------- REGISTERED STUDENTS -----------------");
        if (list.isEmpty()) System.out.println("No records found.");
        for (Student s : list) {
            System.out.printf("[%s] %s | %s | %s | Sem: %d\n", 
                s.getId(), s.getName(), s.getEmail(), s.getBranch(), s.getSemester());
        }
        System.out.println("-------------------------------------------------------");
    }

    private static void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        String id = scanner.nextLine().trim();
        if (studentService.findStudent(id).isEmpty()) {
            System.out.println("[!] Student not found.");
            return;
        }
        System.out.print("Enter New Name (leave blank to retain): ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter New Email (leave blank to retain): ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter New Branch (leave blank to retain): ");
        String branch = scanner.nextLine().trim();
        System.out.print("Enter New Semester (0 to retain): ");
        int sem = 0;
        try {
            String semIn = scanner.nextLine().trim();
            if (!semIn.isBlank()) sem = Integer.parseInt(semIn);
        } catch (NumberFormatException ignored) {}

        studentService.updateStudent(id, name, email, branch, sem);
        System.out.println("[✓] Record updated successfully.");
    }

    private static void deleteStudent() {
        System.out.print("Enter Student ID to remove: ");
        String id = scanner.nextLine().trim();
        if (studentService.deleteStudent(id)) {
            System.out.println("[✓] Student record removed.");
        } else {
            System.out.println("[!] Student record not found.");
        }
    }

    private static void addCourse() {
        System.out.print("Enter Course Code (e.g., CSE1021): ");
        String code = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter Course Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Credits (1-8): ");
        try {
            int credits = Integer.parseInt(scanner.nextLine().trim());
            if (!InputValidator.isValidCredits(credits)) throw new NumberFormatException();
            if (courseService.addCourse(new Course(code, name, credits))) {
                System.out.println("[✓] Course registered successfully.");
            } else {
                System.out.println("[!] Course code already exists.");
            }
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid credit unit entered.");
        }
    }

    private static void listCourses() {
        List<Course> list = courseService.getAllCourses();
        System.out.println("\n----------------- COURSE CATALOG -----------------");
        if (list.isEmpty()) System.out.println("No courses registered.");
        for (Course c : list) {
            System.out.printf("[%s] %-30s (%d Credits)\n", c.getCode(), c.getName(), c.getCredits());
        }
        System.out.println("--------------------------------------------------");
    }

    private static void recordMarks() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        if (studentService.findStudent(id).isEmpty()) {
            System.out.println("[!] Student ID not found.");
            return;
        }
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().trim().toUpperCase();
        if (courseService.findCourse(code).isEmpty()) {
            System.out.println("[!] Course Code not found.");
            return;
        }
        System.out.print("Enter Marks Obtained (0 - 100): ");
        try {
            double marks = Double.parseDouble(scanner.nextLine().trim());
            if (!InputValidator.isValidMarks(marks)) {
                System.out.println("[!] Marks must be bounded between 0.0 and 100.0.");
                return;
            }
            academicService.recordMarks(id, code, marks);
            System.out.println("[✓] Academic marks saved.");
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid numeric format.");
        }
    }

    private static void recordAttendance() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        if (studentService.findStudent(id).isEmpty()) {
            System.out.println("[!] Student ID not found.");
            return;
        }
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().trim().toUpperCase();
        if (courseService.findCourse(code).isEmpty()) {
            System.out.println("[!] Course Code not found.");
            return;
        }
        try {
            System.out.print("Enter Attended Classes: ");
            int attended = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Total Classes Conducted: ");
            int total = Integer.parseInt(scanner.nextLine().trim());

            if (!InputValidator.isValidAttendance(attended, total)) {
                System.out.println("[!] Invalid attendance values: Attended must be <= Total and non-negative.");
                return;
            }
            academicService.recordAttendance(id, code, attended, total);
            System.out.println("[✓] Attendance successfully logged.");
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid integer entered.");
        }
    }

    private static void generateReport() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        reportService.printStudentReport(id);
    }
}
