package project.hexa.student.infrastructure.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.hexa.student.application.StudentService;
import project.hexa.student.infrastructure.controller.dto.StudentOutputWithAsignaturasDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class StudentSubjectControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentSubjectController studentSubjectController;

    //getSubjects TEST
    @Test
    void test_getSubjectsByStudentId() {
        // Arrange
        Integer studentId = 1;
        List<SubjectOutputDto> subjectList = List.of(mock(SubjectOutputDto.class), mock(SubjectOutputDto.class));

        when(studentService.getSubjectsByStudentId(studentId)).thenReturn(subjectList);

        // Act
        ResponseEntity<List<SubjectOutputDto>> response = studentSubjectController.getSubjectsByStudentId(studentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subjectList, response.getBody());
        verify(studentService).getSubjectsByStudentId(studentId);
    }

    //AssignSubjects TEST
    @Test
    void test_assignSubjectsToStudent() {
        // Arrange
        Integer studentId = 1;
        List<Integer> subjectIds = List.of(101, 102);
        StudentOutputWithAsignaturasDto studentWithSubjects = mock(StudentOutputWithAsignaturasDto.class);

        when(studentService.assignSubjectsToStudent(studentId, subjectIds)).thenReturn(studentWithSubjects);

        // Act
        ResponseEntity<StudentOutputWithAsignaturasDto> response =
                studentSubjectController.assignSubjectsToStudent(studentId, subjectIds);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(studentWithSubjects, response.getBody());
        verify(studentService).assignSubjectsToStudent(studentId, subjectIds);
    }

    //UnassignSubjects TEST
    @Test
    void test_unassignSubjectsFromStudent() {
        // Arrange
        Integer studentId = 1;
        List<Integer> subjectIds = List.of(101, 102);
        StudentOutputWithAsignaturasDto studentWithSubjects = mock(StudentOutputWithAsignaturasDto.class);

        when(studentService.unassignSubjectsFromStudent(studentId, subjectIds)).thenReturn(studentWithSubjects);

        // Act
        ResponseEntity<StudentOutputWithAsignaturasDto> response =
                studentSubjectController.unassignSubjectsFromStudent(studentId, subjectIds);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(studentWithSubjects, response.getBody());
        verify(studentService).unassignSubjectsFromStudent(studentId, subjectIds);
    }
}

