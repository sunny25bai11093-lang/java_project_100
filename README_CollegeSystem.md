# College Attendance and Course Management System

The **College Attendance and Course Management System** is a Java console application for managing student information, courses, attendance, and academic performance. It provides separate Admin and Student sections with simple menu-based operations.

## Project Description

The system is designed to maintain basic college records in an organized manner. Administrators can manage students and courses, record attendance, enter performance marks, generate attendance reports, and configure the attendance threshold.

Students can enter their roll number to view their attendance and performance information.

All records are maintained in memory using Java `ArrayList` collections.

## Objectives

- Add and manage student records.
- Add and manage course records.
- Record student attendance.
- Calculate attendance percentages.
- Show attendance warnings when the percentage is below the configured threshold.
- Store performance marks.
- Calculate grades automatically.
- Generate attendance reports.
- Allow students to view their attendance and performance.
- Validate user input.
- Handle missing students and courses using custom exceptions.
- Demonstrate Java OOP and collection concepts.

## Features

### Admin Management

The Admin section provides access to the main management operations. Admin access is protected using a password configured in the source code.

### Student Management

- Add a new student.
- Automatically assign a student ID.
- Store student name and roll number.
- Prevent duplicate roll numbers.
- View all students.

### Course Management

- Add a new course.
- Automatically assign a course ID.
- Store course name and faculty name.
- View all courses.

### Attendance Management

- Select a student using roll number.
- Select a course using course ID.
- Mark attendance as Present or Absent.
- Maintain total classes and attended classes.
- Record attendance for repeated classes.

### Attendance Percentage

The attendance percentage is calculated using:

**Attendance Percentage = (Attended Classes / Total Classes) × 100**

If no classes have been recorded, the attendance percentage is displayed as `0`.

### Attendance Status and Warning

The system compares the student's attendance percentage with the configured attendance threshold.

- If percentage is below the threshold, the status is `WARNING`.
- Otherwise, the status is `OK`.

The default attendance threshold is **75%**.

### Performance Management

- Add marks for a student in a course.
- Marks above 100 are rejected.
- Display the calculated grade after marks are entered.

### Grade Calculation

The system uses the following grade ranges:

| Marks | Grade |
|---|---|
| 90 and above | A |
| 75–89 | B |
| 60–74 | C |
| 40–59 | D |
| Below 40 | F |

### Attendance Reports

The Admin section can display an attendance report containing:

- Student roll number
- Student name
- Course name
- Attended classes
- Total classes
- Attendance percentage
- Attendance status

### Student Section

Students can enter their roll number and:

1. View their attendance.
2. View their performance.
3. Return to the main menu.

## Input Validation

The system validates:

- Student and course names.
- Numeric inputs.
- Attendance status using `P` or `A`.
- Duplicate student roll numbers.
- Marks greater than 100.

## Exception Handling

Two custom exceptions are used:

- `StudentNotFoundException` — used when a requested student cannot be found.
- `CourseNotFoundException` — used when a requested course cannot be found.

## OOP Concepts Used

### Classes and Objects

The project is divided into classes such as `Student`, `Course`, `Attendance`, `Performance`, and `CollegeSystem`.

### Encapsulation

Data fields in the main entity classes are declared `private` and accessed through getter methods.

### Collections

`ArrayList` is used to maintain lists of students, courses, attendance records, and performance records.

### Custom Exceptions

Custom exception classes are created for student and course lookup errors.

> **Note:** The current code does not use inheritance hierarchies, interfaces, abstract classes, or method overriding/polymorphism. The documentation therefore does not claim these concepts as implemented features.

## Main Classes

| Class | Purpose |
|---|---|
| `CollegeSystem` | Controls menus, validation, management operations, and program flow |
| `Student` | Stores student ID, name, and roll number |
| `Course` | Stores course ID, course name, and faculty name |
| `Attendance` | Stores and calculates attendance for a student-course combination |
| `Performance` | Stores marks and calculates the corresponding grade |
| `StudentNotFoundException` | Handles student lookup errors |
| `CourseNotFoundException` | Handles course lookup errors |

## Technologies Used

- Java
- Console-based application
- Object-Oriented Programming
- Java Collections Framework
- `ArrayList`
- `Scanner`
- Exception Handling
- Custom Exceptions

## Java Collections Used

The project uses `ArrayList` collections for:

- Student records
- Course records
- Attendance records
- Performance records

## Data Storage

The current implementation stores all information in memory while the program is running. It does **not** use a database or file-based persistence, so records are not permanently saved after the program terminates.

## Admin Workflow

1. Select **Admin Management**.
2. Enter the configured admin password.
3. Choose an operation from the Admin menu.
4. Manage students, courses, attendance, performance, reports, or threshold settings.
5. Return to the main menu.

## Student Workflow

1. Select **Student Section**.
2. Enter the student's roll number.
3. Choose:
   - View My Attendance
   - View My Performance
4. Return to the main menu.

## Future Enhancements

Possible future improvements include:

- Database connectivity.
- Secure password storage.
- Student authentication.
- Faculty-specific access.
- Update and delete operations.
- Prevention of duplicate performance records.
- Persistent attendance and performance history.
- Graphical or web-based interface.

## Conclusion

The College Attendance and Course Management System provides the core functionality required to manage students, courses, attendance, and performance through a simple Java console application. Its use of classes, encapsulation, collections, validation, and custom exceptions makes the project organized and easy to extend.
