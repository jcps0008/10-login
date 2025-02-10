package project.hexa.professor.application;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import project.hexa.exception.EntityNotFoundException;
import project.hexa.exception.UnprocessableEntityException;
import project.hexa.exception.WrongOutputTypeException;
import project.hexa.mapper.ProfessorMapper;
import project.hexa.person.domain.Person;
import project.hexa.person.infrastructure.repository.PersonRepository;
import project.hexa.professor.domain.Professor;
import project.hexa.professor.infrastructure.controller.dto.ProfessorInputDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputFullDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputSimpleDto;
import project.hexa.professor.infrastructure.repository.ProfessorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static project.hexa.components.StringConstants.*;

@SpringBootTest
public class ProfessorServiceTest {
    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ProfessorMapper professorMapper;

    @InjectMocks
    private ProfessorServiceImpl professorService;

    //addProfessor TESTS
    @Test
    void addProfessor_ValidInput_ReturnsOutputDto() throws UnprocessableEntityException {
        // Arrange
        ProfessorInputDto inputDto = new ProfessorInputDto();
        inputDto.setId_person(1);

        Professor professor = new Professor();
        Person person = new Person();
        ProfessorOutputSimpleDto outputDto = new ProfessorOutputSimpleDto();

        Mockito.when(professorMapper.toProfesorEntity(inputDto)).thenReturn(professor);
        Mockito.when(personRepository.findById(1)).thenReturn(Optional.of(person));
        Mockito.when(professorRepository.save(professor)).thenReturn(professor);
        Mockito.when(professorMapper.toProfesorOutputSimpleDto(professor)).thenReturn(outputDto);

        // Act
        ProfessorOutputDto result = professorService.addProfessor(inputDto);

        // Assert
        assertEquals(outputDto, result);
        Mockito.verify(professorMapper).toProfesorEntity(inputDto);
        Mockito.verify(personRepository).findById(1);
        Mockito.verify(professorRepository).save(professor);
        Mockito.verify(professorMapper).toProfesorOutputSimpleDto(professor);
    }

    //getProfessorById TESTS
    @Test
    void getProfessorById_SimpleOutput_ReturnsSimpleDto() throws EntityNotFoundException {
        // Arrange
        int professorId = 1;
        Professor professor = new Professor();
        ProfessorOutputSimpleDto professorOutputSimpleDto = new ProfessorOutputSimpleDto();

        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        Mockito.when(professorMapper.toProfesorOutputSimpleDto(professor)).thenReturn(professorOutputSimpleDto);

        // Act
        ProfessorOutputDto result = professorService.getProfessorById(professorId, OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(professorOutputSimpleDto, result);
        Mockito.verify(professorRepository).findById(professorId);
        Mockito.verify(professorMapper).toProfesorOutputSimpleDto(professor);
    }

    @Test
    void getProfessorById_FullOutput_ReturnsFullDto() throws EntityNotFoundException {
        // Arrange
        int professorId = 1;

        Professor professor = new Professor();
        ProfessorOutputFullDto professorOutputFullDto = new ProfessorOutputFullDto();

        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        Mockito.when(professorMapper.toProfesorOutputFullDto(professor)).thenReturn(professorOutputFullDto);

        // Act
        ProfessorOutputDto result = professorService.getProfessorById(professorId, OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(professorOutputFullDto, result);
        Mockito.verify(professorRepository).findById(professorId);
        Mockito.verify(professorMapper).toProfesorOutputFullDto(professor);
    }

    @Test
    void getProfessorById_InvalidOutputType_ThrowsException() {
        // Arrange
        int professorId = 1;

        Professor professor = new Professor();

        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));

        // Act & Assert
        //el metodo assertThrows verifica que se lance una excepción cuando de ejecuta
        assertThrows(WrongOutputTypeException.class, () -> {
            professorService.getProfessorById(professorId, OUTPUT_TYPE_INVALID);
        });

    }

    @Test
    void getProfessorById_NonExistentId_ThrowsEntityNotFoundException() {
        // Arrange
        int professorId = 1;

        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            professorService.getProfessorById(professorId, OUTPUT_TYPE_SIMPLE);
        });

        Mockito.verify(professorRepository).findById(professorId);
    }

    //getAllProfessors TEST
    @Test
    void getAllProfessors_SimpleOutput_ReturnsListOfSimpleDtos() throws EntityNotFoundException {
        // Arrange

        Professor professor1 = new Professor();
        Professor professor2 = new Professor();
        List<Professor> professorList = List.of(professor1, professor2);

        ProfessorOutputSimpleDto professorOutputSimpleDto1 = new ProfessorOutputSimpleDto();
        ProfessorOutputSimpleDto professorOutputSimpleDto2 = new ProfessorOutputSimpleDto();

        Mockito.when(professorRepository.findAll()).thenReturn(professorList);
        Mockito.when(professorMapper.toProfesorOutputSimpleDto(professor1)).thenReturn(professorOutputSimpleDto1);
        Mockito.when(professorMapper.toProfesorOutputSimpleDto(professor2)).thenReturn(professorOutputSimpleDto2);

        // Act
        List<ProfessorOutputDto> result = professorService.getAllProfessors(OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(2, result.size());
        assertEquals(professorOutputSimpleDto1, result.get(0));
        assertEquals(professorOutputSimpleDto2, result.get(1));
        Mockito.verify(professorRepository).findAll();
        Mockito.verify(professorMapper).toProfesorOutputSimpleDto(professor1);
        Mockito.verify(professorMapper).toProfesorOutputSimpleDto(professor2);
    }

    @Test
    void getAllProfessors_FullOutput_ReturnsListOfFullDtos() throws EntityNotFoundException {

        // Arrange
        Professor professor1 = new Professor();
        Professor professor2 = new Professor();
        List<Professor> professorList = List.of(professor1, professor2);

        ProfessorOutputFullDto professorOutputFullDto1 = new ProfessorOutputFullDto();
        ProfessorOutputFullDto professorOutputFullDto2 = new ProfessorOutputFullDto();

        Mockito.when(professorRepository.findAll()).thenReturn(professorList);
        Mockito.when(professorMapper.toProfesorOutputFullDto(professor1)).thenReturn(professorOutputFullDto1);
        Mockito.when(professorMapper.toProfesorOutputFullDto(professor2)).thenReturn(professorOutputFullDto2);

        // Act
        List<ProfessorOutputDto> result = professorService.getAllProfessors(OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(2, result.size());
        assertEquals(professorOutputFullDto1, result.get(0));
        assertEquals(professorOutputFullDto2, result.get(1));
        Mockito.verify(professorRepository).findAll();
        Mockito.verify(professorMapper).toProfesorOutputFullDto(professor1);
        Mockito.verify(professorMapper).toProfesorOutputFullDto(professor2);
    }

    @Test
    void getAllProfessors_EmptyList_ThrowsEntityNotFoundException() {

        // Arrange
        Mockito.when(professorRepository.findAll()).thenReturn(List.of());

        // Act & Assert
        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class, () -> professorService.getAllProfessors(OUTPUT_TYPE_SIMPLE));

        // Cambiar el mensaje esperado según lo que se lanza en el código
        assertEquals(PROFESSOR_EMPTY, exception.getMessage());
        Mockito.verify(professorRepository).findAll();
    }

    //updateProfessor TEST
    @Test
    void updateProfessor_ValidInput_ReturnsUpdatedProfessorDto() {
        // Arrange
        Integer professorId = 1;
        Integer personId = 2;
        ProfessorInputDto professorInputDto = new ProfessorInputDto();
        professorInputDto.setId_person(personId);

        Professor existingProfessor = new Professor();
        Person person = new Person();
        ProfessorOutputSimpleDto professorOutputSimpleDto = new ProfessorOutputSimpleDto();

        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(existingProfessor));
        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        Mockito.when(professorMapper.toProfesorEntity(professorInputDto)).thenReturn(existingProfessor);
        Mockito.when(professorMapper.toProfesorOutputSimpleDto(existingProfessor)).thenReturn(professorOutputSimpleDto);
        Mockito.when(professorRepository.save(existingProfessor)).thenReturn(existingProfessor);

        // Act
        ProfessorOutputDto result = professorService.updateProfessor(professorId, professorInputDto);

        // Assert
        assertEquals(professorOutputSimpleDto, result);
        Mockito.verify(professorRepository).findById(professorId);
        Mockito.verify(professorMapper).updateProfesorEntityFromDto(professorInputDto, existingProfessor);
        Mockito.verify(professorRepository).save(existingProfessor);
    }



    @Test
    void updateProfessor_ProfessorNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Integer professorId = 1;
        ProfessorInputDto professorInputDto = new ProfessorInputDto();
        professorInputDto.setId_person(2);

        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> professorService.updateProfessor(professorId, professorInputDto));
        assertEquals(PROFESSOR_ID_NOT_FOUND + professorId, exception.getMessage());
        Mockito.verify(professorRepository).findById(professorId);
    }

    @Test
    void updateProfessor_InvalidPerson_ThrowsUnprocessableEntityException() throws EntityNotFoundException {
        // Arrange
        Integer professorId = 1;
        ProfessorInputDto professorInputDto = new ProfessorInputDto();
        professorInputDto.setId_person(2); // ID de persona inválido

        Professor existingProfessor = new Professor();
        Person invalidPerson = new Person();
        invalidPerson.setProfessor(new Professor()); // Persona ya es profesor

        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(existingProfessor));
        Mockito.when(personRepository.findById(professorInputDto.getId_person())).thenReturn(Optional.of(invalidPerson));

        // Act & Assert
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class,
                () -> professorService.updateProfessor(professorId, professorInputDto));
        assertEquals(PERSON_IS_PROFESOR, exception.getMessage());
        Mockito.verify(professorRepository).findById(professorId);
        Mockito.verify(personRepository).findById(professorInputDto.getId_person());
    }

    //deleteProfessor TEST
    @Test
    void deleteProfessor_ValidId_DeletesProfessor() throws EntityNotFoundException {
        // Arrange
        Integer professorId = 1;
        Professor professor = new Professor();
        professor.setStudents(new ArrayList<>()); // Inicializamos la lista de estudiantes como vacía.

        // Aseguramos que el campo `person` esté inicializado.
        Person person = new Person();
        professor.setPerson(person);

        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        Mockito.when(personRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(person));

        // Act
        professorService.deleteProfessor(professorId);

        // Assert
        Mockito.verify(professorRepository).findById(professorId);
        Mockito.verify(professorRepository).deleteById(professorId);
    }



    @Test
    void deleteProfessor_ProfessorNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Integer professorId = 1;
        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            professorService.deleteProfessor(professorId);
        });

        //Assert
        assertEquals(PROFESSOR_ID_NOT_FOUND + professorId, exception.getMessage());
    }

    //checkoutputType TEST
    @Test
    void checkoutputType_NullInput_ReturnsDefaultType() {
        // Arrange
        ProfessorServiceImpl professorService = new ProfessorServiceImpl();

        // Act
        String result = professorService.checkoutputType(OUTPUT_TYPE_NULL);

        // Assert
        assertEquals(OUTPUT_TYPE_SIMPLE, result);
    }

    @Test
    void checkoutputType_EmptyInput_ThrowsWrongOutputTypeException() {
        // Arrange
        ProfessorServiceImpl professorService = new ProfessorServiceImpl();

        // Act & Assert
        assertThrows(WrongOutputTypeException.class, () -> professorService.checkoutputType(OUTPUT_TYPE_EMPTY));
    }

    @Test
    void checkoutputType_ValidInput_ReturnsSameType() {
        // Arrange
        ProfessorServiceImpl professorService = new ProfessorServiceImpl();

        // Act
        String result = professorService.checkoutputType(OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(OUTPUT_TYPE_FULL, result);
    }

}

