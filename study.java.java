package com.vit.smartstudent;

import com.vit.smartstudent.model.AttendanceRecord;
import com.vit.smartstudent.model.MarkRecord;
import com.vit.smartstudent.service.AcademicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AcademicServiceTest {
    private AcademicService academicService;

    @BeforeEach
    public void setUp() {
        academicService = new AcademicService();
    }

    @Test
    public void testRecordAndFetchMarks() {
        String testStudent = "25BAI11093";
        String testCourse = "CSE1021";
        double testScore = 96.5;

        academicService.recordMarks(testStudent, testCourse, testScore);
        List<MarkRecord> marksList = academicService.getStudentMarks(testStudent);

        assertNotNull(marksList);
        assertTrue(marksList.stream().anyMatch(m -> m.getCourseCode().equalsIgnoreCase(testCourse) && m.getMarks() == testScore));
    }

    @Test
    public void testRecordAndFetchAttendance() {
        String testStudent = "25BAI11093";
        String testCourse = "MAT2002";
        int attended = 29;
        int total = 30;

        academicService.recordAttendance(testStudent, testCourse, attended, total);
        List<AttendanceRecord> attendanceList = academicService.getStudentAttendance(testStudent);

        assertNotNull(attendanceList);
        assertTrue(attendanceList.stream().anyMatch(a -> a.getCourseCode().equalsIgnoreCase(testCourse) && a.getAttended() == 29 && a.getTotal() == 30));
    }

    @Test
    public void testUpdateExistingMarks() {
        String testStudent = "25BAI11093";
        String testCourse = "CSE2001";

        academicService.recordMarks(testStudent, testCourse, 75.0);
        academicService.recordMarks(testStudent, testCourse, 92.0);

        List<MarkRecord> marksList = academicService.getStudentMarks(testStudent);
        long count = marksList.stream().filter(m -> m.getCourseCode().equalsIgnoreCase(testCourse)).count();
        assertEquals(1, count, "Duplicate marks entries should not exist for same student-course pair");
    }
}