import java.util.*;

class StudentNotFoundException extends Exception {
    StudentNotFoundException(String msg) {
        super(msg);
    }
}

class CourseNotFoundException extends Exception {
    CourseNotFoundException(String msg) {
        super(msg);
    }
}

class Student {
    private int id;
    private String name;
    private int roll_number;

    Student(int id, String name, int roll_number) {
        this.id = id;
        this.name = name;
        this.roll_number = roll_number;
    }

    int get_id() { return id; }
    String get_name() { return name; }
    int get_roll() { return roll_number; }

    void print_student() {
        System.out.println(roll_number + "\t" + name);
    }
}

class Course {
    private int id;
    private String course_name;
    private String faculty_name;

    Course(int id, String course_name, String faculty_name) {
        this.id = id;
        this.course_name = course_name;
        this.faculty_name = faculty_name;
    }

    int get_id() 
    { return id; }
    String get_name()
     { return course_name; }
    String get_faculty() 
    { return faculty_name; }

    void print_course() {
        System.out.println(id + "\t" + course_name + "\t\t" + faculty_name);
    }
}

class Attendance {
    private int student_id;
    private int course_id;
    private int total_classes;
    private int attended_classes;

    Attendance(int student_id, int course_id) {
        this.student_id = student_id;
        this.course_id = course_id;
        this.total_classes = 0;
        this.attended_classes = 0;
    }

    int get_student_id()
    { return student_id; }
    int get_course_id() { return course_id; }
    int get_total() { return total_classes; }
    int get_attended() { return attended_classes; }

    void mark(boolean present) {
        total_classes++;
        if (present) {
            attended_classes++;
        }
    }

    double get_percentage() {
        if (total_classes == 0) {
            return 0;
        }
        return (attended_classes * 100.0) / total_classes;
    }
}

class Performance {
    private int student_id;
    private int course_id;
    private int marks;

    Performance(int student_id, int course_id, int marks) {
        this.student_id = student_id;
        this.course_id = course_id;
        this.marks = marks;
    }

    int get_student_id() { return student_id; }
    int get_course_id() { return course_id; }
    int get_marks() { return marks; }

        String get_grade() {
            if (marks >= 90) {
                return "A";
            } else if (marks >= 75) {
                return "B";
            } else if (marks >= 60) {
                return "C";
            } else if (marks >= 40) {
                return "D";
            } else {
                return "F";
            }
        }
    }

public class CollegeSystem {

    static ArrayList<Student> student_list = new ArrayList<Student>();
    static ArrayList<Course> course_list = new ArrayList<Course>();
    static ArrayList<Attendance> attendance_list = new ArrayList<Attendance>();
    static ArrayList<Performance> performance_list = new ArrayList<Performance>();

    static int next_student_id = 1;
    static int next_course_id = 1;
    static int attendance_threshold = 75;

    static final String ADMIN_PASSWORD = "admin123";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice = 0;

        while (choice != 3) {

            System.out.println();
            System.out.println("======= COLLEGE ATTENDANCE AND COURSE MANAGEMENT SYSTEM =======");
            System.out.println("1. Admin Management");
            System.out.println("2. Student Section");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String input = sc.nextLine();

            if (!valid_number(input)) {
                System.out.println("Invalid choice, enter numbers only.");
                continue;
            }

            choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    admin_menu();
                    break;
                case 2:
                    student_menu();
                    break;
                case 3:
                    System.out.println("Thank you. Closing college system.");
                    break;
                default:
                    System.out.println("Please choose a valid option (1-3).");
            }
        }

        sc.close();
    }

    static void admin_menu() {
        System.out.print("Enter admin password: ");
        String password = sc.nextLine();

        if (!password.equals(ADMIN_PASSWORD)) {
            System.out.println("Wrong password. Access denied.");
            return;
        }

        int choice = 0;

        while (choice != 9) {

            System.out.println();
            System.out.println("======= ADMIN MANAGEMENT ========");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Add Course");
            System.out.println("4. View All Courses");
            System.out.println("5. Mark Attendance");
            System.out.println("6. Add Performance Marks");
            System.out.println("7. View Attendance Reports");
            System.out.println("8. Set Attendance Threshold (current: " + attendance_threshold + "%)");
            System.out.println("9. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String input = sc.nextLine();

            if (!valid_number(input)) {
                System.out.println("Invalid choice, enter numbers only.");
                continue;
            }

            choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    add_student();
                    break;
                case 2:
                    view_students();
                    break;
                case 3:
                    add_course();
                    break;
                case 4:
                    view_courses();
                    break;
                case 5:
                    mark_attendance();
                    break;
                case 6:
                    add_performance();
                    break;
                case 7:
                    view_attendance_reports();
                    break;
                case 8:
                    set_threshold();
                    break;
                case 9:
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Please choose a valid option (1-9).");
            }
        }
    }

    static void student_menu() {
        int choice = 0;

        while (choice != 3) {

            System.out.println();
            System.out.println("======= STUDENT SECTION ========");
            System.out.println("1. View My Attendance");
            System.out.println("2. View My Performance");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");

            String input = sc.nextLine();

            if (!valid_number(input)) {
                System.out.println("Invalid choice, enter numbers only.");
                continue;
            }

            choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    view_my_attendance();
                    break;
                case 2:
                    view_my_performance();
                    break;
                case 3:
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Please choose a valid option (1-3).");
            }
        }
    }

    static boolean valid_name(String s) {
        if (s == null || s.trim().length() == 0) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    static boolean valid_number(String s) {
        if (s == null || s.trim().length() == 0) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static String read_valid_name(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = sc.nextLine();
            if (valid_name(value)) {
                return value.trim();
            }
            System.out.println("Invalid input. Only alphabets and spaces are allowed, try again.");
        }
    }

    static int read_valid_number(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = sc.nextLine();
            if (valid_number(value)) {
                return Integer.parseInt(value);
            }
            System.out.println("Invalid input. Only numbers are allowed, try again.");
        }
    }

    static char read_valid_status(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = sc.nextLine().trim().toUpperCase();
            if (value.length() == 1 && (value.charAt(0) == 'P' || value.charAt(0) == 'A')) {
                return value.charAt(0);
            }
            System.out.println("Invalid input. Enter only P for present or A for absent.");
        }
    }

    static Student find_student_by_roll(int roll_number) {
        for (int i = 0; i < student_list.size(); i++) {
            if (student_list.get(i).get_roll() == roll_number) {
                return student_list.get(i);
            }
        }
        return null;
    }

    static Course find_course_by_id(int id) {
        for (int i = 0; i < course_list.size(); i++) {
            if (course_list.get(i).get_id() == id) {
                return course_list.get(i);
            }
        }
        return null;
    }

    static Attendance find_attendance(int student_id, int course_id) {
        for (int i = 0; i < attendance_list.size(); i++) {
            Attendance a = attendance_list.get(i);
            if (a.get_student_id() == student_id && a.get_course_id() == course_id) {
                return a;
            }
        }
        return null;
    }

    static void add_student() {
        String name = read_valid_name("Enter student name: ");
        int roll_number = read_valid_number("Enter roll number: ");

        if (find_student_by_roll(roll_number) != null) {
            System.out.println("A student with this roll number already exists.");
            return;
        }

        Student s = new Student(next_student_id, name, roll_number);
        student_list.add(s);
        System.out.println("Student added successfully with roll number: " + roll_number);
        next_student_id++;
    }

    static void view_students() {
        if (student_list.size() == 0) {
            System.out.println("No students added yet.");
            return;
        }
        System.out.println("Roll\tName");
        for (int i = 0; i < student_list.size(); i++) {
            student_list.get(i).print_student();
        }
    }

    static void add_course() {
        String course_name = read_valid_name("Enter course name: ");
        String faculty_name = read_valid_name("Enter faculty name: ");

        Course c = new Course(next_course_id, course_name, faculty_name);
        course_list.add(c);
        System.out.println("Course added successfully with ID: " + next_course_id);
        next_course_id++;
    }

    static void view_courses() {
        if (course_list.size() == 0) {
            System.out.println("No courses added yet.");
            return;
        }
        System.out.println("ID\tCourse\t\tFaculty");
        for (int i = 0; i < course_list.size(); i++) {
            course_list.get(i).print_course();
        }
    }

    static void mark_attendance() {
        int roll_number = read_valid_number("Enter student roll number: ");
        int course_id = read_valid_number("Enter course ID: ");

        try {
            Student s = find_student_by_roll(roll_number);
            Course c = find_course_by_id(course_id);

            if (s == null) {
                throw new StudentNotFoundException("Student with roll number " + roll_number + " does not exist.");
            }
            if (c == null) {
                throw new CourseNotFoundException("Course with ID " + course_id + " does not exist.");
            }

            char status = read_valid_status("Enter status (P for present, A for absent): ");
            boolean present = status == 'P';

            Attendance a = find_attendance(s.get_id(), c.get_id());
            if (a == null) {
                a = new Attendance(s.get_id(), c.get_id());
                attendance_list.add(a);
            }
            a.mark(present);

            System.out.println("Attendance marked for " + s.get_name() + " in " + c.get_name());
            double percentage = a.get_percentage();
            System.out.println("Current attendance: " + String.format("%.2f", percentage) + "%");

            if (percentage < attendance_threshold) {
                System.out.println("ATTENDANCE WARNING: " + s.get_name() + " is below the " + attendance_threshold + "% threshold in " + c.get_name());
            }

        } catch (StudentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (CourseNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void add_performance() {
        int roll_number = read_valid_number("Enter student roll number: ");
        int course_id = read_valid_number("Enter course ID: ");

        try {
            Student s = find_student_by_roll(roll_number);
            Course c = find_course_by_id(course_id);

            if (s == null) {
                throw new StudentNotFoundException("Student with roll number " + roll_number + " does not exist.");
            }
            if (c == null) {
                throw new CourseNotFoundException("Course with ID " + course_id + " does not exist.");
            }

            int marks = read_valid_number("Enter marks obtained (0-100): ");
            if (marks > 100) {
                System.out.println("Marks cannot be more than 100. Entry cancelled.");
                return;
            }

            Performance p = new Performance(s.get_id(), c.get_id(), marks);
            performance_list.add(p);
            System.out.println("Marks recorded for " + s.get_name() + " in " + c.get_name() + ". Grade: " + p.get_grade());

        } catch (StudentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (CourseNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void view_attendance_reports() {
        if (attendance_list.size() == 0) {
            System.out.println("No attendance records yet.");
            return;
        }

        System.out.println("Roll\tName\t\tCourse\t\tAttended/Total\tPercentage\tStatus");
        for (int i = 0; i < attendance_list.size(); i++) {
            Attendance a = attendance_list.get(i);
            Student s = find_student_by_id(a.get_student_id());
            Course c = find_course_by_id(a.get_course_id());

            if (s == null || c == null) {
                continue;
            }

            double percentage = a.get_percentage();
            String status = percentage < attendance_threshold ? "WARNING" : "OK";

            System.out.println(s.get_roll() + "\t" + s.get_name() + "\t\t" + c.get_name() + "\t\t"
                    + a.get_attended() + "/" + a.get_total() + "\t\t"
                    + String.format("%.2f", percentage) + "%\t\t" + status);
        }
    }

    static Student find_student_by_id(int id) {
        for (int i = 0; i < student_list.size(); i++) {
            if (student_list.get(i).get_id() == id) {
                return student_list.get(i);
            }
        }
        return null;
    }

    static void set_threshold() {
        int value = read_valid_number("Enter new attendance threshold percentage (0-100): ");
        if (value > 100) {
            System.out.println("Threshold cannot be more than 100. No change made.");
            return;
        }
        attendance_threshold = value;
        System.out.println("Attendance threshold updated to " + attendance_threshold + "%");
    }

    static void view_my_attendance() {
        int roll_number = read_valid_number("Enter your roll number: ");
        Student s = find_student_by_roll(roll_number);

        if (s == null) {
            System.out.println("No student found with this roll number.");
            return;
        }

        boolean found = false;
        System.out.println("Course\t\tAttended/Total\tPercentage\tStatus");
        for (int i = 0; i < attendance_list.size(); i++) {
            Attendance a = attendance_list.get(i);
            if (a.get_student_id() == s.get_id()) {
                Course c = find_course_by_id(a.get_course_id());
                if (c == null) {
                    continue;
                }
                double percentage = a.get_percentage();
                String status = percentage < attendance_threshold ? "WARNING" : "OK";
                System.out.println(c.get_name() + "\t\t" + a.get_attended() + "/" + a.get_total() + "\t\t"
                        + String.format("%.2f", percentage) + "%\t\t" + status);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No attendance records found for you yet.");
        }
    }

    static void view_my_performance() {
        int roll_number = read_valid_number("Enter your roll number: ");
        Student s = find_student_by_roll(roll_number);

        if (s == null) {
            System.out.println("No student found with this roll number.");
            return;
        }

        boolean found = false;
        System.out.println("Course\t\tMarks\tGrade");
        for (int i = 0; i < performance_list.size(); i++) {
            Performance p = performance_list.get(i);
            if (p.get_student_id() == s.get_id()) {
                Course c = find_course_by_id(p.get_course_id());
                if (c == null) {
                    continue;
                }
                System.out.println(c.get_name() + "\t\t" + p.get_marks() + "\t" + p.get_grade());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No performance records found for you yet.");
        }
    }
}
