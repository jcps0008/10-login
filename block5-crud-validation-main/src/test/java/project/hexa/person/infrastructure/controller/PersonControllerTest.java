package project.hexa.person.infrastructure.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import project.hexa.person.application.PersonService;
import project.hexa.person.infrastructure.controller.dto.PersonInputDto;
import project.hexa.person.infrastructure.controller.dto.PersonOutputDto;
import project.hexa.person.infrastructure.controller.dto.PersonOutputSimpleDto;
import project.hexa.person.infrastructure.feign.FeignProfessor;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputSimpleDto;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static project.hexa.components.StringConstants.*;


@SpringBootTest
class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    //addPerson TEST
    @Test
    void addPerson_ReturnsCreatedResponse() {
        // Arrange
        PersonInputDto personInputDto = Mockito.mock(PersonInputDto.class);
        PersonOutputDto personOutputDto = Mockito.mock(PersonOutputDto.class);

        Mockito.when(personService.addPerson(any(PersonInputDto.class))).thenReturn(personOutputDto);

        // Act
        ResponseEntity<PersonOutputDto> response = personController.addPerson(personInputDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(personOutputDto, response.getBody());

        // Verify
        Mockito.verify(personService).addPerson(any(PersonInputDto.class));
    }

    //getPersonById TEST
    @Test
    void getPersonById_ReturnsPersonOutputDto() {
        // Arrange
        int idPerson = 1;
        PersonOutputDto personOutputDto = Mockito.mock(PersonOutputDto.class);

        Mockito.when(personService.getPersonById(idPerson, OUTPUT_TYPE_SIMPLE)).thenReturn(personOutputDto);

        // Act
        ResponseEntity<PersonOutputDto> response = personController.getPersonById(idPerson, OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personOutputDto, response.getBody());

        // Verify
        Mockito.verify(personService).getPersonById(idPerson, OUTPUT_TYPE_SIMPLE);
    }

    //getPersonByUsername TEST
    @Test
    void getPersonByUsername_ValidRequest_ReturnsPersonList() {
        // Arrange
        List<PersonOutputDto> personOutputDtoList = List.of(Mockito.mock(PersonOutputDto.class)); // Lista simulada
        Mockito.when(personService.getPersonByUsername(Mockito.anyString(), Mockito.anyString())).thenReturn(personOutputDtoList);

        // Act
        ResponseEntity<List<PersonOutputDto>> response = personController.getPersonByUsername(Mockito.anyString(), Mockito.anyString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personOutputDtoList, response.getBody());
        Mockito.verify(personService).getPersonByUsername(Mockito.anyString(), Mockito.anyString());
    }

    //getAllPerson TEST
    @Test
    void getAllPerson_ValidRequest_ReturnsAllPersons() {
        // Arrange
        List<PersonOutputDto> personOutputDtoList = List.of(Mockito.mock(PersonOutputDto.class));

        Mockito.when(personService.getAllPerson(Mockito.anyString())).thenReturn(personOutputDtoList);

        // Act
        ResponseEntity<List<PersonOutputDto>> response = personController.getAllPerson(Mockito.anyString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personOutputDtoList, response.getBody());
        Mockito.verify(personService).getAllPerson(Mockito.anyString());
    }

    //updatePerson TEST
    @Test
    void updatePerson_ValidRequest_ReturnsUpdatedPerson() {
        // Arrange
        int id=1;
        PersonInputDto personInputDto = new PersonInputDto();
        PersonOutputSimpleDto personOutputSimpleDto = new PersonOutputSimpleDto();

        Mockito.when(personService.updatePerson(anyInt(), Mockito.any(PersonInputDto.class)))
                .thenReturn(personOutputSimpleDto);

        // Act
        ResponseEntity<PersonOutputDto> response = personController.updatePerson(id, personInputDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personOutputSimpleDto, response.getBody());
        Mockito.verify(personService).updatePerson(anyInt(), Mockito.any(PersonInputDto.class));
    }

    //deletePersonById TEST
    @Test
    void deletePersonById_ValidRequest_ReturnsSuccessMessage() {
        // Arrange
        Integer personId = 1;
        String expectedMessage = DELETE_SUCCESSFULLY + personId;


        Mockito.doNothing().when(personService).deletePerson(anyInt());

        // Act
        ResponseEntity<String> response = personController.deletePersonById(personId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        Mockito.verify(personService).deletePerson(Mockito.eq(personId));
    }

    @Mock
    private RestTemplate restTemplate;
    //getProfesor TEST
    @Test
    void getProfesor_ValidRequest_ReturnsProfessor() {
        // Arrange
        int professorId = 1;
        ProfessorOutputSimpleDto professorOutputSimpleDto = new ProfessorOutputSimpleDto();


        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(ProfessorOutputSimpleDto.class)))
                .thenReturn(new ResponseEntity<>(professorOutputSimpleDto, HttpStatus.OK));

        // Act
        ResponseEntity<ProfessorOutputDto> response = personController.getProfesor(professorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professorOutputSimpleDto, response.getBody());
    }

    @Mock
    private FeignProfessor feignProfessor;

    //getProfessorUsingFeign TEST
    @Test
    void getProfessorUsingFeign_ValidRequest_ReturnsProfessor() {
        // Arrange
        Integer professorId = 1;


        ProfessorOutputSimpleDto professorOutputSimpleDto = new ProfessorOutputSimpleDto();
        Mockito.when(feignProfessor.getProfessorById(professorId)).thenReturn(professorOutputSimpleDto);

        // Act
        ResponseEntity<ProfessorOutputDto> response = personController.getProfessorUsingFeign(professorId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(professorOutputSimpleDto, response.getBody());
    }

    //getPersonsByCriteria TEST

    //getPersonsByCriteria sin filtros (solo paginación)
    @Test
    void getPersonsByCriteria_NoFilters_ReturnsPaginatedResults() {
        // Arrange
        int page = 0;
        int size = 10;  // Solo paginación, sin filtros
        List<PersonOutputDto> personOutputDtoList = List.of(Mockito.mock(PersonOutputDto.class)); // Simular respuesta paginada

        Mockito.when(personService.getPersonsByCriteria(
                Mockito.isNull(), Mockito.isNull(), Mockito.isNull(),
                Mockito.isNull(), Mockito.isNull(),
                Mockito.isNull(), Mockito.eq(page), Mockito.eq(size)
        )).thenReturn(personOutputDtoList);

        // Act
        ResponseEntity<List<PersonOutputDto>> response = personController.getPersonsByCriteria(
                null, null, null, null, null, null, page, size);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personOutputDtoList, response.getBody());

        // Verify
        Mockito.verify(personService).getPersonsByCriteria(
                Mockito.isNull(), Mockito.isNull(), Mockito.isNull(),
                Mockito.isNull(), Mockito.isNull(),
                Mockito.isNull(), Mockito.eq(page), Mockito.eq(size)
        );
    }

    //getPersonsByCriteria con todos los filtros
    @Test
    void getPersonsByCriteria_WithAllFilters_ReturnsFilteredResults() {
        // Arrange
        Date fechaSuperior = new Date();
        Date fechaInferior = new Date(fechaSuperior.getTime() - 100000); // Una fecha anterior
        String ORDERBY_VARIABLE_TEST = "username";
        int page = 1;
        int size = 5;

        List<PersonOutputDto> personOutputDtoList = List.of(Mockito.mock(PersonOutputDto.class)); // Simulación de respuesta

        Mockito.when(personService.getPersonsByCriteria(
                Mockito.eq(USERNAME_VARIABLE_TEST), Mockito.eq(NAME_VARIABLE_TEST), Mockito.eq(SURNAME_VARIABLE_TEST),
                Mockito.eq(fechaSuperior), Mockito.eq(fechaInferior),
                Mockito.eq(ORDERBY_VARIABLE_TEST), Mockito.eq(page), Mockito.eq(size)
        )).thenReturn(personOutputDtoList);

        // Act
        ResponseEntity<List<PersonOutputDto>> response = personController.getPersonsByCriteria(
                USERNAME_VARIABLE_TEST, NAME_VARIABLE_TEST, SURNAME_VARIABLE_TEST, fechaSuperior, fechaInferior, ORDERBY_VARIABLE_TEST, page, size);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personOutputDtoList, response.getBody());

        // Verify
        Mockito.verify(personService).getPersonsByCriteria(
                Mockito.eq(USERNAME_VARIABLE_TEST), Mockito.eq(NAME_VARIABLE_TEST), Mockito.eq(SURNAME_VARIABLE_TEST),
                Mockito.eq(fechaSuperior), Mockito.eq(fechaInferior),
                Mockito.eq(ORDERBY_VARIABLE_TEST), Mockito.eq(page), Mockito.eq(size)
        );
    }

    //getPersonsByCriteria filtros parciales y ordenación
    @Test
    void getPersonsByCriteria_WithPartialFilters_ReturnsFilteredResults() {
        // Arrange
        int page = 2;
        int size = 15;

        List<PersonOutputDto> personOutputDtoList = List.of(Mockito.mock(PersonOutputDto.class)); // Simulación de respuesta

        Mockito.when(personService.getPersonsByCriteria(
                Mockito.eq(USERNAME_VARIABLE_TEST), Mockito.isNull(), Mockito.isNull(),
                Mockito.isNull(), Mockito.isNull(),
                Mockito.eq(ORDERBY_VARIABLE_TEST), Mockito.eq(page), Mockito.eq(size)
        )).thenReturn(personOutputDtoList);

        // Act
        ResponseEntity<List<PersonOutputDto>> response = personController.getPersonsByCriteria(
                USERNAME_VARIABLE_TEST, null, null, null, null, ORDERBY_VARIABLE_TEST, page, size);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personOutputDtoList, response.getBody());

        // Verify
        Mockito.verify(personService).getPersonsByCriteria(
                Mockito.eq(USERNAME_VARIABLE_TEST), Mockito.isNull(), Mockito.isNull(),
                Mockito.isNull(), Mockito.isNull(),
                Mockito.eq(ORDERBY_VARIABLE_TEST), Mockito.eq(page), Mockito.eq(size)
        );
    }

}