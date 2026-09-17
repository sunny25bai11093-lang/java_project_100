package com.vit.smartstudent;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// --- DOMAIN MODELS ---
class Student {
    private String id, name, email, branch;
    private int semester;

    public Student(String id, String name, String email, String branch, int semester) {
        this.id = id; this.name = name; this.email = email; this.branch = branch; this.semester = semester;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getBranch() { return branch; }
    public int getSemester() { return semester; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setBranch(String branch) { this.branch = branch; }
    public void setSemester(int semester) { this.semester = semester; }
    public String toCsv() { return String.format("%s,%s,%s,%s,%d", id, name, email, branch, semester); }
}

class Course {
    private String code, name;
    private int credits;

    public Course(String code, String name, int credits) {
        this.code = code; this.name = name; this.credits = credits;
    }
    public String getCode() { return code; }
    public String getName() { return name; }
    public int getCredits() { return credits; }
    public String toCsv() { return String.format("%s,%s,%d", code, name, credits); }
}

class MarkRecord {
    private String studentId, courseCode;
    private double marks;

    public MarkRecord(String studentId, String courseCode, double marks) {
        this.studentId = studentId; this.courseCode = courseCode; this.marks = marks;
    }
    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public double getMarks() { return marks; }
    public void setMarks(double marks) { this.marks = marks; }
    public String toCsv() { return String.format("%s,%s,%.2f", studentId, courseCode, marks); }
}

class AttendanceRecord {
    private String studentId, courseCode;
    private int attended, total;

    public AttendanceRecord(String studentId, String courseCode, int attended, int total) {
        this.studentId = studentId; this.courseCode = courseCode; this.attended = attended; this.total = total;
    }
    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public int getAttended() { return attended; }
    public int getTotal() { return total; }
    public void update(int attended, int total) { this.attended = attended; this.total = total; }
    public double getPercentage() { return total == 0 ? 100.0 : ((double) attended / total) * 100.0; }
    public String toCsv() { return String.format("%s,%s,%d,%d", studentId, courseCode, attended, total); }
}

// --- VALIDATION ---
class InputValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern ID_PATTERN = Pattern.compile("^[0-9]{2}[A-Za-z]{3}[0-9]{4,5}$");

    public static boolean isValidEmail(String email) { return email != null && EMAIL_PATTERN.matcher(email.trim()).matches(); }
    public static boolean isValidId(String id) { return id != null && ID_PATTERN.matcher(id.trim()).matches(); }
    public static boolean isValidMarks(double marks) { return marks >= 0.0 && marks <= 100.0; }
    public static boolean isValidAttendance(int attended, int total) { return total >= 0 && attended >= 0 && attended <= total; }
}

// --- SERVICES & CLI ENTRYPOINT ---
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Student> students = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();
    private static final List<MarkRecord> marks = new ArrayList<>();
    private static final List<AttendanceRecord> attendance = new ArrayList<>();

    public static void main(String[] args) {
        seedData();
        boolean exit = false;
        while (!exit) {
            System.out.println("""
            ======================================================================
                             SMART STUDENT PERFORMANCE MANAGER                   
               Author: Sunny Gupta (25BAI11093) | Programming in Java Project   
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
                case "9" -> printStudentReport();
                case "10" -> printClassSummary();
                case "0" -> { System.out.println("Exiting SSPM. Goodbye!"); exit = true; }
                default -> System.out.println("[!] Invalid choice. Enter 0 - 10.");
            }
        }
    }

    private static void seedData() {
        students.add(new Student("25BAI11093", "Sunny Gupta", "sunny.gupta2025@vitbhopal.ac.in", "AIML", 2));
        courses.add(new Course("CSE1021", "Programming in Java", 4));
        courses.add(new Course("MAT2002", "Discrete Mathematics", 4));
        courses.add(new Course("CSE2001", "Data Structures and Algorithms", 4));
        marks.add(new MarkRecord("25BAI11093", "CSE1021", 94.0));
        marks.add(new MarkRecord("25BAI11093", "MAT2002", 88.5));
        marks.add(new MarkRecord("25BAI11093", "CSE2001", 91.0));
        attendance.add(new AttendanceRecord("25BAI11093", "CSE1021", 28, 30));
        attendance.add(new AttendanceRecord("25BAI11093", "MAT2002", 27, 30));
        attendance.add(new AttendanceRecord("25BAI11093", "CSE2001", 29, 30));
    }

    private static void addStudent() {
        System.out.print("Enter Registration No (e.g., 25BAI11093): ");
        String id = scanner.nextLine().trim();
        if (!InputValidator.isValidId(id)) { System.out.println("[!] Invalid Registration format."); return; }
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Institutional Email: ");
        String email = scanner.nextLine().trim();
        if (!InputValidator.isValidEmail(email)) { System.out.println("[!] Invalid email address."); return; }
        System.out.print("Enter Branch: ");
        String branch = scanner.nextLine().trim();
        System.out.print("Enter Semester (1-10): ");
        try {
            int sem = Integer.parseInt(scanner.nextLine().trim());
            students.add(new Student(id, name, email, branch, sem));
            System.out.println("[✓] Student registered successfully.");
        } catch (Exception e) { System.out.println("[!] Invalid semester."); }
    }

    private static void listStudents() {
        System.out.println("\n----------------- REGISTERED STUDENTS -----------------");
        for (Student s : students) System.out.printf("[%s] %s | %s | %s | Sem: %d\n", s.getId(), s.getName(), s.getEmail(), s.getBranch(), s.getSemester());
        System.out.println("-------------------------------------------------------");
    }

    private static void updateStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        Optional<Student> opt = students.stream().filter(s -> s.getId().equalsIgnoreCase(id)).findFirst();
        if (opt.isEmpty()) { System.out.println("[!] Student not found."); return; }
        Student s = opt.get();
        System.out.print("Enter New Name: ");
        String n = scanner.nextLine().trim();
        if (!n.isBlank()) s.setName(n);
        System.out.println("[✓] Updated.");
    }

    private static void deleteStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        if (students.removeIf(s -> s.getId().equalsIgnoreCase(id))) System.out.println("[✓] Removed.");
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
            courses.add(new Course(code, name, cr));
            System.out.println("[✓] Course added.");
        } catch (Exception e) { System.out.println("[!] Invalid credit unit."); }
    }

    private static void listCourses() {
        System.out.println("\n----------------- COURSE CATALOG -----------------");
        for (Course c : courses) System.out.printf("[%s] %s (%d Credits)\n", c.getCode(), c.getName(), c.getCredits());
        System.out.println("--------------------------------------------------");
    }

    private static void recordMarks() {
        System.out.print("Enter Student ID: ");
        String sid = scanner.nextLine().trim();
        System.out.print("Enter Course Code: ");
        String cid = scanner.nextLine().trim().toUpperCase();
        System.out.print("Enter Marks (0-100): ");
        try {
            double m = Double.parseDouble(scanner.nextLine().trim());
            if (InputValidator.isValidMarks(m)) {
                marks.removeIf(x -> x.getStudentId().equalsIgnoreCase(sid) && x.getCourseCode().equalsIgnoreCase(cid));
                marks.add(new MarkRecord(sid, cid, m));
                System.out.println("[✓] Marks recorded.");
            } else System.out.println("[!] Score out of range (0-100).");
        } catch (Exception e) { System.out.println("[!] Invalid numeric input."); }
    }

    private static void recordAttendance() {
        System.out.print("Enter Student ID: ");
        String sid = scanner.nextLine().trim();
        System.out.print("Enter Course Code: ");
        String cid = scanner.nextLine().trim().toUpperCase();
        try {
            System.out.print("Attended Classes: ");
            int att = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Total Classes: ");
            int tot = Integer.parseInt(scanner.nextLine().trim());
            if (InputValidator.isValidAttendance(att, tot)) {
                attendance.removeIf(a -> a.getStudentId().equalsIgnoreCase(sid) && a.getCourseCode().equalsIgnoreCase(cid));
                attendance.add(new AttendanceRecord(sid, cid, att, tot));
                System.out.println("[✓] Attendance logged.");
            } else System.out.println("[!] Attended must be <= Total and non-negative.");
        } catch (Exception e) { System.out.println("[!] Invalid integer entered."); }
    }

    private static void printStudentReport() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        Optional<Student> opt = students.stream().filter(s -> s.getId().equalsIgnoreCase(id)).findFirst();
        if (opt.isEmpty()) { System.out.println("[!] Student not found."); return; }
        Student s = opt.get();

        System.out.println("\n======================================================================");
        System.out.println("                     STUDENT PERFORMANCE REPORT                      ");
        System.out.println("======================================================================");
        System.out.printf("Student Name : %-25s Registration No : %s\n", s.getName(), s.getId());
        System.out.printf("Branch       : %-25s Semester        : %d\n", s.getBranch(), s.getSemester());
        System.out.println("----------------------------------------------------------------------");

        double totalMarks = 0;
        int count = 0, totalAtt = 0, totalLec = 0;
        for (MarkRecord mr : marks.stream().filter(m -> m.getStudentId().equalsIgnoreCase(s.getId())).toList()) {
            Optional<AttendanceRecord> ar = attendance.stream().filter(a -> a.getStudentId().equalsIgnoreCase(s.getId()) && a.getCourseCode().equalsIgnoreCase(mr.getCourseCode())).findFirst();
            String attStr = "N/A";
            if (ar.isPresent()) {
                attStr = String.format("%d/%d (%.1f%%)", ar.get().getAttended(), ar.get().getTotal(), ar.get().getPercentage());
                totalAtt += ar.get().getAttended();
                totalLec += ar.get().getTotal();
            }
            System.out.printf("%-10s Marks: %-8.2f Attendance: %-15s [%s]\n", mr.getCourseCode(), mr.getMarks(), attStr, mr.getMarks() >= 50 ? "PASS" : "FAIL");
            totalMarks += mr.getMarks();
            count++;
        }
        double avg = count > 0 ? totalMarks / count : 0.0;
        double attPct = totalLec > 0 ? ((double) totalAtt / totalLec) * 100.0 : 100.0;
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("Cumulative Average Marks : %.2f / 100\n", avg);
        System.out.printf("Overall Attendance Rate  : %.2f%%\n", attPct);
        System.out.println("Academic Distinction     : " + (avg >= 90.0 ? "First Class with Distinction" : "First Class"));
        System.out.println("======================================================================\n");
    }

    private static void printClassSummary() {
        double avgM = marks.stream().mapToDouble(MarkRecord::getMarks).average().orElse(0.0);
        double avgA = attendance.stream().mapToDouble(AttendanceRecord::getPercentage).average().orElse(100.0);
        System.out.println("\n------------------ CLASS SUMMARY ------------------");
        System.out.printf("Total Enrolled Students : %d\n", students.size());
        System.out.printf("Class Average Marks     : %.2f / 100\n", avgM);
        System.out.printf("Class Mean Attendance   : %.2f%%\n", avgA);
        System.out.println("---------------------------------------------------");
    }
}