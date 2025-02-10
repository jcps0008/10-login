package project.hexa.subject.infrastructure.controller;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.hexa.subject.application.SubjectService;
import project.hexa.subject.infrastructure.controller.dto.SubjectInputDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputSimpleDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.hexa.components.StringConstants.DELETE_SUCCESSFULLY;
import static project.hexa.components.StringConstants.OUTPUT_TYPE_SIMPLE;

@SpringBootTest
public class SubjectControllerTest {
    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    //addSubject TEST
    @Test
    void addSubject_ValidRequest_ReturnsCreatedProfessor() {
        // Arrange
        SubjectInputDto subjectInputDto = new SubjectInputDto();
        SubjectOutputDto subjectOutputDto = new SubjectOutputSimpleDto();

        when(subjectService.addSubject(subjectInputDto)).thenReturn(subjectOutputDto);

        // Act
        ResponseEntity<SubjectOutputDto> response = subjectController.addSubject(subjectInputDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(subjectOutputDto, response.getBody());
        verify(subjectService).addSubject(subjectInputDto);
    }

    //getSubjectById TEST
    @Test
    void getSubjectById_ReturnsPersonOutputDto() {
        // Arrange
        int idSubject = 1;
        SubjectOutputDto outputDto = Mockito.mock(SubjectOutputDto.class);

        Mockito.when(subjectService.getSubjectById(idSubject, OUTPUT_TYPE_SIMPLE)).thenReturn(outputDto);

        // Act
        ResponseEntity<SubjectOutputDto> response = subjectController.getSubjectById(idSubject, OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(outputDto, response.getBody());

        // Verify
        Mockito.verify(subjectService).getSubjectById(idSubject, OUTPUT_TYPE_SIMPLE);
    }

    //getAllSubjects TEST
    @Test
    void getAllSubjects_ValidRequest_ReturnsAllPersons() {
        // Arrange
        List<SubjectOutputDto> mockResponse = List.of(Mockito.mock(SubjectOutputDto.class));
        Mockito.when(subjectService.getAllSubjects(Mockito.anyString())).thenReturn(mockResponse);

        // Act
        ResponseEntity<List<SubjectOutputDto>> response = subjectController.getAllSubjects(Mockito.anyString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        Mockito.verify(subjectService).getAllSubjects(Mockito.anyString());
    }

    //updateSubject TEST
    @Test
    void updateSubject_ValidRequest_ReturnsUpdatedPerson() {
        // Arrange
        int id=1;
        SubjectInputDto subjectInputDto = new SubjectInputDto();
        SubjectOutputSimpleDto subjectOutputSimpleDto = new SubjectOutputSimpleDto();


        when(subjectService.updateSubject(anyInt(), Mockito.any(SubjectInputDto.class)))
                .thenReturn(subjectOutputSimpleDto);

        // Act
        ResponseEntity<SubjectOutputDto> response = subjectController.updateSubject(id, subjectInputDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subjectOutputSimpleDto, response.getBody());

        Mockito.verify(subjectService).updateSubject(anyInt(), Mockito.any(SubjectInputDto.class));
    }

    //deleteSubjectById TEST
    @Test
    void deleteSubjectById_ValidRequest_ReturnsSuccessMessage() {
        // Arrange
        Integer studentId = 1;
        String expectedMessage = DELETE_SUCCESSFULLY + studentId;

        Mockito.doNothing().when(subjectService).deleteSubject(anyInt());

        // Act
        ResponseEntity<String> response = subjectController.deleteSubjectById(studentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        Mockito.verify(subjectService).deleteSubject(Mockito.eq(studentId));
    }

}
