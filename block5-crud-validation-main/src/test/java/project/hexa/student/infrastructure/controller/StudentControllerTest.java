package project.hexa.student.infrastructure.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.hexa.student.application.StudentService;
import project.hexa.student.infrastructure.controller.dto.StudentInputDto;
import project.hexa.student.infrastructure.controller.dto.StudentOutputDto;
import project.hexa.student.infrastructure.controller.dto.StudentOutputSimpleDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.hexa.components.StringConstants.DELETE_SUCCESSFULLY;
import static project.hexa.components.StringConstants.OUTPUT_TYPE_SIMPLE;

@SpringBootTest
public class StudentControllerTest {
    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;


    //addStudent TEST
    @Test
    void addStudent_ValidRequest_ReturnsCreatedProfessor() {
        // Arrange
        StudentInputDto studentInputDto = new StudentInputDto();
        StudentOutputDto studentOutputDto = new StudentOutputSimpleDto();

        when(studentService.addStudent(studentInputDto)).thenReturn(studentOutputDto);

        // Act
        ResponseEntity<StudentOutputDto> response = studentController.addStudent(studentInputDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(studentOutputDto, response.getBody());
        verify(studentService).addStudent(studentInputDto);
    }

    //getStudentById TEST
    @Test
    void getStudentById_ReturnsPersonOutputDto() {
        // Arrange
        int idStudent = 1;
        StudentOutputDto outputDto = Mockito.mock(StudentOutputDto.class);

        Mockito.when(studentService.getStudentById(idStudent, OUTPUT_TYPE_SIMPLE)).thenReturn(outputDto);

        // Act
        ResponseEntity<StudentOutputDto> response = studentController.getStudentById(idStudent, OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(outputDto, response.getBody());

        // Verify
        Mockito.verify(studentService).getStudentById(idStudent, OUTPUT_TYPE_SIMPLE);
    }

    //getAllStudents TEST
    @Test
    void getAllStudents_ValidRequest_ReturnsAllPersons() {
        // Arrange
        List<StudentOutputDto> mockResponse = List.of(Mockito.mock(StudentOutputDto.class));
        Mockito.when(studentService.getAllStudents(Mockito.anyString())).thenReturn(mockResponse);

        // Act
        ResponseEntity<List<StudentOutputDto>> response = studentController.getAllStudents(Mockito.anyString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        Mockito.verify(studentService).getAllStudents(Mockito.anyString());
    }

    //updateStudent TEST
    @Test
    void updateStudent_ValidRequest_ReturnsUpdatedPerson() {
        // Arrange
        int id=1;
        StudentInputDto studentInputDto = new StudentInputDto();
        StudentOutputSimpleDto studentOutputSimpleDto = new StudentOutputSimpleDto();

        when(studentService.updateStudent(anyInt(), Mockito.any(StudentInputDto.class)))
                .thenReturn(studentOutputSimpleDto);

        // Act
        ResponseEntity<StudentOutputDto> response = studentController.updateStudent(id, studentInputDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(studentOutputSimpleDto, response.getBody());
        Mockito.verify(studentService).updateStudent(anyInt(), Mockito.any(StudentInputDto.class));
    }

    //deleteStudentById TEST
    @Test
    void deleteStudentById_ValidRequest_ReturnsSuccessMessage() {
        // Arrange
        Integer studentId = 1;
        String expectedMessage = DELETE_SUCCESSFULLY + studentId;

        Mockito.doNothing().when(studentService).deleteStudent(anyInt());

        // Act
        ResponseEntity<String> response = studentController.deleteStudentById(studentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        Mockito.verify(studentService).deleteStudent(Mockito.eq(studentId));
    }
}
