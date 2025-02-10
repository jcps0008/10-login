package project.hexa.professor.infrastructure.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.hexa.professor.application.ProfessorService;
import project.hexa.professor.infrastructure.controller.dto.ProfessorInputDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputSimpleDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static project.hexa.components.StringConstants.DELETE_SUCCESSFULLY;
import static project.hexa.components.StringConstants.OUTPUT_TYPE_SIMPLE;

@SpringBootTest
public class ProfessorControllerTest {

    @Mock
    private ProfessorService professorService;

    @InjectMocks
    private ProfessorController professorController;


    //addProfessor TEST
    @Test
    void addProfessor_ValidRequest_ReturnsCreatedProfessor() {
        // Arrange
        ProfessorInputDto professorInputDto = new ProfessorInputDto();
        ProfessorOutputDto professorOutputDto = new ProfessorOutputSimpleDto();

        when(professorService.addProfessor(professorInputDto)).thenReturn(professorOutputDto);

        // Act
        ResponseEntity<ProfessorOutputDto> response = professorController.addProfessor(professorInputDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(professorOutputDto, response.getBody());
        verify(professorService).addProfessor(professorInputDto);
    }

    //getProfessorById TEST
    @Test
    void getProfessorById_ReturnsPersonOutputDto() {
        // Arrange
        int idProfessor = 1;
        ProfessorOutputDto outputDto = Mockito.mock(ProfessorOutputDto.class);

        Mockito.when(professorService.getProfessorById(idProfessor, OUTPUT_TYPE_SIMPLE)).thenReturn(outputDto);

        // Act
        ResponseEntity<ProfessorOutputDto> response = professorController.getProfessorById(idProfessor, OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(outputDto, response.getBody());

        // Verify
        Mockito.verify(professorService).getProfessorById(idProfessor, OUTPUT_TYPE_SIMPLE);
    }

    //getAllProfessor TEST
    @Test
    void getAllProfessor_ValidRequest_ReturnsAllPersons() {
        // Arrange
        List<ProfessorOutputDto> mockResponse = List.of(Mockito.mock(ProfessorOutputDto.class));
        Mockito.when(professorService.getAllProfessors(Mockito.anyString())).thenReturn(mockResponse);

        // Act
        ResponseEntity<List<ProfessorOutputDto>> response = professorController.getAllProfessors(Mockito.anyString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
        Mockito.verify(professorService).getAllProfessors(Mockito.anyString());
    }

    //updateProfessor TEST
    @Test
    void updateProfessor_ValidRequest_ReturnsUpdatedPerson() {
        // Arrange
        int id=1;
        ProfessorInputDto professorInputDto = new ProfessorInputDto();
        ProfessorOutputSimpleDto professorOutputSimpleDto = new ProfessorOutputSimpleDto();


        Mockito.when(professorService.updateProfessor(anyInt(), Mockito.any(ProfessorInputDto.class)))
                .thenReturn(professorOutputSimpleDto);

        // Act
        ResponseEntity<ProfessorOutputDto> response = professorController.updateProfessor(id, professorInputDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professorOutputSimpleDto, response.getBody());
        Mockito.verify(professorService).updateProfessor(anyInt(), Mockito.any(ProfessorInputDto.class));
    }

    //deleteProfessorById TEST
    @Test
    void deleteProfessorById_ValidRequest_ReturnsSuccessMessage() {
        // Arrange
        Integer personId = 1;
        String expectedMessage = DELETE_SUCCESSFULLY + personId;

        Mockito.doNothing().when(professorService).deleteProfessor(anyInt());

        // Act
        ResponseEntity<String> response = professorController.deleteProfessorById(personId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        Mockito.verify(professorService).deleteProfessor(Mockito.eq(personId));
    }
}
