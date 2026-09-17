# 1. Create full directory structure
mkdir -p src/main/java/com/vit/smartstudent/model
mkdir -p src/main/java/com/vit/smartstudent/service
mkdir -p src/main/java/com/vit/smartstudent/util
mkdir -p src/test/java/com/vit/smartstudent
mkdir -p data

# 2. Write CSV seed datasets
cat << 'EOF' > data/students.csv
id,name,email,branch,semester
25BAI11093,Sunny Gupta,sunny.gupta2025@vitbhopal.ac.in,AIML,2
25BCE10042,Rohan Varma,rohan.varma2025@vitbhopal.ac.in,CSE,2
EOF

cat << 'EOF' > data/courses.csv
code,name,credits
CSE1021,Programming in Java,4
MAT2002,Discrete Mathematics,4
CSE2001,Data Structures and Algorithms,4
EOF

cat << 'EOF' > data/marks.csv
studentId,courseCode,marks
25BAI11093,CSE1021,94.0
25BAI11093,MAT2002,88.5
25BAI11093,CSE2001,91.0
25BCE10042,CSE1021,82.0
EOF

cat << 'EOF' > data/attendance.csv
studentId,courseCode,attended,total
25BAI11093,CSE1021,28,30
25BAI11093,MAT2002,27,30
25BAI11093,CSE2001,29,30
25BCE10042,CSE1021,25,30
EOF

# 3. Write Domain Models
cat << 'EOF' > src/main/java/com/vit/smartstudent/model/Student.java
package com.vit.smartstudent.model;

public class Student {
    private String id;
    private String name;
    private String email;
    private String branch;
    private int semester;

    public Student(String id, String name, String email, String branch, int semester) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.semester = semester;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public String toCsv() {
        return String.format("%s,%s,%s,%s,%d", id, name, email, branch, semester);
    }
}
EOF

cat << 'EOF' > src/main/java/com/vit/smartstudent/model/Course.java
package com.vit.smartstudent.model;

public class Course {
    private String code;
    private String name;
    private int credits;

    public Course(String code, String name, int credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String toCsv() {
        return String.format("%s,%s,%d", code, name, credits);
    }
}
EOF

cat << 'EOF' > src/main/java/com/vit/smartstudent/model/MarkRecord.java
package com.vit.smartstudent.model;

public class MarkRecord {
    private String studentId;
    private String courseCode;
    private double marks;

    public MarkRecord(String studentId, String courseCode, double marks) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.marks = marks;
    }

    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }

    public String toCsv() {
        return String.format("%s,%s,%.2f", studentId, courseCode, marks);
    }
}
EOF

cat << 'EOF' > src/main/java/com/vit/smartstudent/model/AttendanceRecord.java
package com.vit.smartstudent.model;

public class AttendanceRecord {
    private String studentId;
    private String courseCode;
    private int attended;
    private int total;

    public AttendanceRecord(String studentId, String courseCode, int attended, int total) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.attended = attended;
        this.total = total;
    }

    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public int getAttended() { return attended; }
    public int getTotal() { return total; }

    public void updateAttendance(int attended, int total) {
        this.attended = attended;
        this.total = total;
    }

    public double getPercentage() {
        if (total == 0) return 100.0;
        return ((double) attended / total) * 100.0;
    }

    public String toCsv() {
        return String.format("%s,%s,%d,%d", studentId, courseCode, attended, total);
    }
}
EOF

# 4. Write Utility Validator
cat << 'EOF' > src/main/java/com/vit/smartstudent/util/InputValidator.java
package com.vit.smartstudent.util;

import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern ID_PATTERN = 
        Pattern.compile("^[0-9]{2}[A-Za-z]{3}[0-9]{4,5}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidId(String id) {
        return id != null && ID_PATTERN.matcher(id.trim()).matches();
    }

    public static boolean isValidMarks(double marks) {
        return marks >= 0.0 && marks <= 100.0;
    }

    public static boolean isValidAttendance(int attended, int total) {
        return total >= 0 && attended >= 0 && attended <= total;
    }

    public static boolean isValidSemester(int sem) {
        return sem >= 1 && sem <= 10;
    }

    public static boolean isValidCredits(int credits) {
        return credits >= 1 && credits <= 8;
    }
}
EOF

# 5. Write Data Store & Services
cat << 'EOF' > src/main/java/com/vit/smartstudent/service/DataStore.java
package com.vit.smartstudent.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final String DATA_DIR = "data";

    public static void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    public static List<String> readLines(String fileName) {
        ensureDataDirectory();
        Path path = Paths.get(DATA_DIR, fileName);
        if (!Files.exists(path)) return new ArrayList<>();
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void writeLines(String fileName, List<String> lines) {
        ensureDataDirectory();
        Path path = Paths.get(DATA_DIR, fileName);
        try {
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ignored) {}
    }
}
EOF

cat << 'EOF' > src/main/java/com/vit/smartstudent/service/StudentService.java
package com.vit.smartstudent.service;

import com.vit.smartstudent.model.Student;
import com.vit.smartstudent.util.InputValidator;
import java.util.*;

public class StudentService {
    private final List<Student> students = new ArrayList<>();
    private final String fileName = "students.csv";

    public StudentService() { loadData(); }

    private void loadData() {
        List<String> lines = DataStore.readLines(fileName);
        students.clear();
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0 && lines.get(i).startsWith("id,")) continue;
            String[] p = lines.get(i).split(",");
            if (p.length == 5) {
                students.add(new Student(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), Integer.parseInt(p[4].trim())));
            }
        }
    }

    public void saveData() {
        List<String> lines = new ArrayList<>();
        lines.add("id,name,email,branch,semester");
        for (Student s : students) lines.add(s.toCsv());
        DataStore.writeLines(fileName, lines);
    }

    public boolean addStudent(Student s) {
        if (findStudent(s.getId()).isPresent()) return false;
        students.add(s);
        saveData();
        return true;
    }

    public Optional<Student> findStudent(String id) {
        return students.stream().filter(s -> s.getId().equalsIgnoreCase(id.trim())).findFirst();
    }

    public List<Student> getAllStudents() { return new ArrayList<>(students); }

    public boolean updateStudent(String id, String name, String email, String branch, int sem) {
        Optional<Student> opt = findStudent(id);
        if (opt.isPresent()) {
            Student s = opt.get();
            if (name != null && !name.isBlank()) s.setName(name.trim());
            if (email != null && InputValidator.isValidEmail(email)) s.setEmail(email.trim());
            if (branch != null && !branch.isBlank()) s.setBranch(branch.trim());
            if (InputValidator.isValidSemester(sem)) s.setSemester(sem);
            saveData();
            return true;
        }
        return false;
    }

    public boolean deleteStudent(String id) {
        boolean removed = students.removeIf(s -> s.getId().equalsIgnoreCase(id.trim()));
        if (removed) saveData();
        return removed;
    }
}
EOF

cat << 'EOF' > src/main/java/com/vit/smartstudent/service/CourseService.java
package com.vit.smartstudent.service;

import com.vit.smartstudent.model.Course;
import java.util.*;

public class CourseService {
    private final List<Course> courses = new ArrayList<>();
    private final String fileName = "courses.csv";

    public CourseService() { loadData(); }

    private void loadData() {
        List<String> lines = DataStore.readLines(fileName);
        courses.clear();
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0 && lines.get(i).startsWith("code,")) continue;
            String[] p = lines.get(i).split(",");
            if (p.length == 3) {
                courses.add(new Course(p[0].trim(), p[1].trim(), Integer.parseInt(p[2].trim())));
            }
        }
    }

    public void saveData() {
        List<String> lines = new ArrayList<>();
        lines.add("code,name,credits");
        for (Course c : courses) lines.add(c.toCsv());
        DataStore.writeLines(fileName, lines);
    }

    public boolean addCourse(Course c) {
        if (findCourse(c.getCode()).isPresent()) return false;
        courses.add(c);
        saveData();
        return true;
    }

    public Optional<Course> findCourse(String code) {
        return courses.stream().filter(c -> c.getCode().equalsIgnoreCase(code.trim())).findFirst();
    }

    public List<Course> getAllCourses() { return new ArrayList<>(courses); }
}
EOF

cat << 'EOF' > src/main/java/com/vit/smartstudent/service/AcademicService.java
package com.vit.smartstudent.service;

import com.vit.smartstudent.model.AttendanceRecord;
import com.vit.smartstudent.model.MarkRecord;
import java.util.*;
import java.util.stream.Collectors;

public class AcademicService {
    private final List<MarkRecord> marks = new ArrayList<>();
    private final List<AttendanceRecord> attendanceList = new ArrayList<>();

    public AcademicService() {
        loadMarks();
        loadAttendance();
    }

    private void loadMarks() {
        List<String> lines = DataStore.readLines("marks.csv");
        marks.clear();
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0 && lines.get(i).startsWith("studentId,")) continue;
            String[] p = lines.get(i).split(",");
            if (p.length == 3) marks.add(new MarkRecord(p[0].trim(), p[1].trim(), Double.parseDouble(p[2].trim())));
        }
    }

    private void loadAttendance() {
        List<String> lines = DataStore.readLines("attendance.csv");
        attendanceList.clear();
        for (int i = 0; i < lines.size(); i++) {
            if (i == 0 && lines.get(i).startsWith("studentId,")) continue;
            String[] p = lines.get(i).split(",");
            if (p.length == 4) attendanceList.add(new AttendanceRecord(p[0].trim(), p[1].trim(), Integer.parseInt(p[2].trim()), Integer.parseInt(p[3].trim())));
        }
    }

    public void recordMarks(String sid, String code, double score) {
        marks.removeIf(m -> m.getStudentId().equalsIgnoreCase(sid) && m.getCourseCode().equalsIgnoreCase(code));
        marks.add(new MarkRecord(sid, code, score));
        List<String> lines = new ArrayList<>();
        lines.add("studentId,courseCode,marks");
        for (MarkRecord m : marks) lines.add(m.toCsv());
        DataStore.writeLines("marks.csv", lines);
    }

    public void recordAttendance(String sid, String code, int attended, int total) {
        attendanceList.removeIf(a -> a.getStudentId().equalsIgnoreCase(sid) && a.getCourseCode().equalsIgnoreCase(code));
        attendanceList.add(new AttendanceRecord(sid, code, attended, total));
        List<String> lines = new ArrayList<>();
        lines.add("studentId,courseCode,attended,total");
        for (AttendanceRecord a : attendanceList) lines.add(a.toCsv());
        DataStore.writeLines("attendance.csv", lines);
    }

    public List<MarkRecord> getStudentMarks(String sid) {
        return marks.stream().filter(m -> m.getStudentId().equalsIgnoreCase(sid)).collect(Collectors.toList());
    }

    public List<AttendanceRecord> getStudentAttendance(String sid) {
        return attendanceList.stream().filter(a -> a.getStudentId().equalsIgnoreCase(sid)).collect(Collectors.toList());
    }

    public List<MarkRecord> getAllMarks() { return marks; }
    public List<AttendanceRecord> getAllAttendance() { return attendanceList; }
}
EOF

cat << 'EOF' > src/main/java/com/vit/smartstudent/service/ReportService.java
package com.vit.smartstudent.service;

import com.vit.smartstudent.model.*;
import java.util.*;

public class ReportService {
    private final StudentService studentService;
    private final CourseService courseService;
    private final AcademicService academicService;

    public ReportService(StudentService ss, CourseService cs, AcademicService as) {
        this.studentService = ss;
        this.courseService = cs;
        this.academicService = as;
    }

    public void printStudentReport(String id) {
        Optional<Student> opt = studentService.findStudent(id);
        if (opt.isEmpty()) {
            System.out.println("[!] Student ID not found.");
            return;
        }
        Student s = opt.get();
        List<MarkRecord> mList = academicService.getStudentMarks(s.getId());
        List<AttendanceRecord> aList = academicService.getStudentAttendance(s.getId());

        System.out.println("\n======================================================================");
        System.out.println("                     STUDENT PERFORMANCE REPORT                      ");
        System.out.println("======================================================================");
        System.out.printf("Student Name : %-25s Registration No : %s\n", s.getName(), s.getId());
        System.out.printf("Branch       : %-25s Semester        : %d\n", s.getBranch(), s.getSemester());
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("%-10s %-30s %-8s %-15s %s\n", "Code", "Course Title", "Marks", "Attendance", "Status");
        System.out.println("----------------------------------------------------------------------");

        double totalMarks = 0;
        int count = 0;
        double totalAttended = 0;
        double totalLectures = 0;

        for (MarkRecord mr : mList) {
            String title = courseService.findCourse(mr.getCourseCode()).map(Course::getName).orElse("Unknown Course");
            Optional<AttendanceRecord> ar = aList.stream().filter(a -> a.getCourseCode().equalsIgnoreCase(mr.getCourseCode())).findFirst();
            String att = "N/A";
            if (ar.isPresent()) {
                att = String.format("%d/%d (%.1f%%)", ar.get().getAttended(), ar.get().getTotal(), ar.get().getPercentage());
                totalAttended += ar.get().getAttended();
                totalLectures += ar.get().getTotal();
            }
            System.out.printf("%-10s %-30s %-8.2f %-15s %s\n", mr.getCourseCode(), title, mr.getMarks(), att, mr.getMarks() >= 50.0 ? "PASS" : "FAIL");
            totalMarks += mr.getMarks();
            count++;
        }

        double avgMarks = count > 0 ? totalMarks / count : 0.0;
        double avgAtt = totalLectures > 0 ? (totalAttended / totalLectures) * 100.0 : 100.0;
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("Cumulative Average Marks : %.2f / 100\n", avgMarks);
        System.out.printf("Overall Attendance Rate  : %.2f%%\n", avgAtt);
        System.out.println("Academic Distinction     : " + (avgMarks >= 90.0 ? "Distinction" : "Regular Pass"));
        System.out.println("Eligibility Status       : " + (avgAtt >= 75.0 ? "Eligible for Exams" : "DEBARRED"));
        System.out.println("======================================================================\n");
    }

    public void printClassSummary() {
        List<MarkRecord> marks = academicService.getAllMarks();
        List<AttendanceRecord> attList = academicService.getAllAttendance();
        double avgClassMark = marks.stream().mapToDouble(MarkRecord::getMarks).average().orElse(0.0);
        double avgClassAtt = attList.stream().mapToDouble(AttendanceRecord::getPercentage).average().orElse(100.0);

        System.out.println("\n======================================================================");
        System.out.println("                         CLASS COHORT SUMMARY                         ");
        System.out.println("======================================================================");
        System.out.printf("Total Enrolled Students : %d\n", studentService.getAllStudents().size());
        System.out.printf("Class Average Marks     : %.2f / 100\n", avgClassMark);
        System.out.printf("Class Mean Attendance   : %.2f%%\n", avgClassAtt);
        System.out.println("======================================================================\n");
    }
}
EOF

# 6. Write Console Application (Main)
cat << 'EOF' > src/main/java/com/vit/smartstudent/Main.java
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
    private static final ReportService reportService = new ReportService(studentService, courseService, academicService);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
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
                case "9" -> {
                    System.out.print("Enter Student ID: ");
                    reportService.printStudentReport(scanner.nextLine().trim());
                }
                case "10" -> reportService.printClassSummary();
                case "0" -> {
                    System.out.println("Exiting Smart Student Performance Manager. Goodbye!");
                    exit = true;
                }
                default -> System.out.println("[!] Invalid choice. Enter 0 - 10.");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Enter Registration No (e.g., 25BAI11093): ");
        String id = scanner.nextLine().trim();
        if (!InputValidator.isValidId(id)) { System.out.println("[!] Invalid format."); return; }
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Institutional Email: ");
        String email = scanner.nextLine().trim();
        if (!InputValidator.isValidEmail(email)) { System.out.println("[!] Invalid email."); return; }
        System.out.print("Enter Branch: ");
        String branch = scanner.nextLine().trim();
        System.out.print("Enter Semester (1-10): ");
        try {
            int sem = Integer.parseInt(scanner.nextLine().trim());
            if (studentService.addStudent(new Student(id, name, email, branch, sem))) {
                System.out.println("[✓] Student added.");
            } else System.out.println("[!] Student ID already exists.");
        } catch (Exception e) { System.out.println("[!] Invalid semester input."); }
    }

    private static void listStudents() {
        List<Student> list = studentService.getAllStudents();
        System.out.println("\n----------------- REGISTERED STUDENTS -----------------");
        for (Student s : list) System.out.printf("[%s] %s | %s | %s | Sem: %d\n", s.getId(), s.getName(), s.getEmail(), s.getBranch(), s.getSemester());
        System.out.println("-------------------------------------------------------");
    }

    private static void updateStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter New Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter New Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter New Branch: ");
        String branch = scanner.nextLine().trim();
        System.out.print("Enter New Semester (0 to skip): ");
        try {
            int sem = Integer.parseInt(scanner.nextLine().trim());
            studentService.updateStudent(id, name, email, branch, sem);
            System.out.println("[✓] Updated.");
        } catch (Exception e) { System.out.println("[!] Skipped semester update."); }
    }

    private static void deleteStudent() {
        System.out.print("Enter Student ID: ");
        if (studentService.deleteStudent(scanner.nextLine().trim())) System.out.println("[✓] Deleted.");
        else System.out.println("[!] Record not found.");
    }

    private static void addCourse() {
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter Course Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Credits (1-8): ");
        try {
            int cr = Integer.parseInt(scanner.nextLine().trim());
            if (courseService.addCourse(new Course(code, name, cr))) System.out.println("[✓] Course added.");
            else System.out.println("[!] Course code already exists.");
        } catch (Exception e) { System.out.println("[!] Invalid credits."); }
    }

    private static void listCourses() {
        for (Course c : courseService.getAllCourses()) System.out.printf("[%s] %s (%d Credits)\n", c.getCode(), c.getName(), c.getCredits());
    }

    private static void recordMarks() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter Marks (0-100): ");
        try {
            double marks = Double.parseDouble(scanner.nextLine().trim());
            if (InputValidator.isValidMarks(marks)) {
                academicService.recordMarks(id, code, marks);
                System.out.println("[✓] Marks recorded.");
            } else System.out.println("[!] Marks out of range.");
        } catch (Exception e) { System.out.println("[!] Invalid number."); }
    }

    private static void recordAttendance() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().trim().toUpperCase();
        try {
            System.out.print("Attended: ");
            int att = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Total: ");
            int tot = Integer.parseInt(scanner.nextLine().trim());
            if (InputValidator.isValidAttendance(att, tot)) {
                academicService.recordAttendance(id, code, att, tot);
                System.out.println("[✓] Attendance recorded.");
            } else System.out.println("[!] Invalid attendance values.");
        } catch (Exception e) { System.out.println("[!] Invalid integers."); }
    }
}
EOF

# 7. Write JUnit 5 Unit Tests
cat << 'EOF' > src/test/java/com/vit/smartstudent/InputValidatorTest.java
package com.vit.smartstudent;

import com.vit.smartstudent.util.InputValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InputValidatorTest {
    @Test
    public void testEmailValidation() {
        assertTrue(InputValidator.isValidEmail("sunny.gupta2025@vitbhopal.ac.in"));
        assertFalse(InputValidator.isValidEmail("bad-email"));
    }

    @Test
    public void testIdValidation() {
        assertTrue(InputValidator.isValidId("25BAI11093"));
        assertFalse(InputValidator.isValidId("12345"));
    }

    @Test
    public void testMarksValidation() {
        assertTrue(InputValidator.isValidMarks(95.0));
        assertFalse(InputValidator.isValidMarks(105.0));
        assertFalse(InputValidator.isValidMarks(-5.0));
    }
}
EOF

cat << 'EOF' > src/test/java/com/vit/smartstudent/AttendanceRecordTest.java
package com.vit.smartstudent;

import com.vit.smartstudent.model.AttendanceRecord;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AttendanceRecordTest {
    @Test
    public void testPercentageCalculation() {
        AttendanceRecord record = new AttendanceRecord("25BAI11093", "CSE1021", 28, 30);
        assertEquals(93.333, record.getPercentage(), 0.01);
    }

    @Test
    public void testZeroLectureEdgeCase() {
        AttendanceRecord record = new AttendanceRecord("25BAI11093", "CSE1021", 0, 0);
        assertEquals(100.0, record.getPercentage());
    }
}
EOF

# 8. Standard Maven POM configuration
cat << 'EOF' > pom.xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.vit.smartstudent</groupId>
    <artifactId>smart-student-performance-manager</artifactId>
    <version>1.0.0</version>
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <junit.jupiter.version>5.10.2</junit.jupiter.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit.jupiter.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit.jupiter.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
EOF

# 9. Clean up corrupt artifacts and push to GitHub
rm -f pom\[1\].xml statement\[1\].md .gitignore\[1\].gitignore twice_read2 sspm_code.sh sspm_codez_2.sh

git add src data pom.xml
git commit -m "feat: complete Java source code, tests, seed CSV data, and Maven configuration"
git push origin main