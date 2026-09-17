package com.vit.smartstudent.service;

import com.vit.smartstudent.model.AttendanceRecord;
import com.vit.smartstudent.model.MarkRecord;
import com.vit.smartstudent.model.Student;

import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {
    private final StudentService studentService;
    private final CourseService courseService;
    private final AcademicService academicService;

    public AnalyticsService(StudentService ss, CourseService cs, AcademicService as) {
        this.studentService = ss;
        this.courseService = cs;
        this.academicService = as;
    }

    public Map<String, Double> computeCourseAverageMarks() {
        Map<String, List<Double>> grouped = new HashMap<>();
        for (MarkRecord mr : academicService.getAllMarks()) {
            grouped.computeIfAbsent(mr.getCourseCode(), k -> new ArrayList<>()).add(mr.getMarks());
        }

        Map<String, Double> results = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : grouped.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            results.put(entry.getKey(), Math.round(avg * 100.0) / 100.0);
        }
        return results;
    }

    public List<Student> getAtRiskAttendanceStudents(double thresholdPercent) {
        Set<String> flaggedStudentIds = new HashSet<>();
        for (AttendanceRecord ar : academicService.getAllAttendance()) {
            if (ar.getPercentage() < thresholdPercent) {
                flaggedStudentIds.add(ar.getStudentId());
            }
        }

        return studentService.getAllStudents().stream()
                .filter(s -> flaggedStudentIds.contains(s.getId()))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getGradeDistribution(String courseCode) {
        Map<String, Integer> distribution = new LinkedHashMap<>();
        distribution.put("O (>=90)", 0);
        distribution.put("A+ (80-89)", 0);
        distribution.put("A (70-79)", 0);
        distribution.put("B (60-69)", 0);
        distribution.put("C (50-59)", 0);
        distribution.put("F (<50)", 0);

        for (MarkRecord mr : academicService.getAllMarks()) {
            if (mr.getCourseCode().equalsIgnoreCase(courseCode)) {
                double score = mr.getMarks();
                if (score >= 90.0) distribution.put("O (>=90)", distribution.get("O (>=90)") + 1);
                else if (score >= 80.0) distribution.put("A+ (80-89)", distribution.get("A+ (80-89)") + 1);
                else if (score >= 70.0) distribution.put("A (70-79)", distribution.get("A (70-79)") + 1);
                else if (score >= 60.0) distribution.put("B (60-69)", distribution.get("B (60-69)") + 1);
                else if (score >= 50.0) distribution.put("C (50-59)", distribution.get("C (50-59)") + 1);
                else distribution.put("F (<50)", distribution.get("F (<50)") + 1);
            }
        }
        return distribution;
    }

    public OptionalDouble calculateCohortGPA() {
        List<MarkRecord> marks = academicService.getAllMarks();
        if (marks.isEmpty()) return OptionalDouble.empty();
        return marks.stream().mapToDouble(MarkRecord::getMarks).average();
    }
}