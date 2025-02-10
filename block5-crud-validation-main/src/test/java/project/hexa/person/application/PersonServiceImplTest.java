package project.hexa.person.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import project.hexa.components.StringConstants;
import project.hexa.exception.EntityNotFoundException;
import project.hexa.exception.UnprocessableEntityException;
import project.hexa.exception.WrongOutputTypeException;
import project.hexa.mapper.PersonMapper;
import project.hexa.person.domain.Person;
import project.hexa.person.infrastructure.controller.dto.PersonInputDto;
import project.hexa.person.infrastructure.controller.dto.PersonOutputDto;
import project.hexa.person.infrastructure.controller.dto.PersonOutputFullDto;
import project.hexa.person.infrastructure.controller.dto.PersonOutputSimpleDto;
import project.hexa.person.infrastructure.repository.PersonRepository;
import project.hexa.professor.domain.Professor;
import project.hexa.student.domain.Student;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static project.hexa.components.StringConstants.*;

@SpringBootTest
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    //Creamos un servicio y le inyectamos los mocks
    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final String  USERNAME_JOHN = "John";
    private final String USERNAME_NOEXISTENT = "NonExistent";

    //addPerson TESTS
    /**
     * Test for the {@code addPerson} method in {@link PersonService}.
     *
     * Case: Valid input.
     * Ensures that when a valid {@link PersonInputDto} is provided without any validation errors,
     * the service correctly saves the entity and returns the corresponding {@link PersonOutputDto}.
     *
     * @throws UnprocessableEntityException if an unexpected error occurs (not expected in this case).
     */
    @Test
    void addPerson_ValidInput_ReturnsOutputDto() throws UnprocessableEntityException {
        // Arrange
        PersonInputDto inputDto = new PersonInputDto();
        Person person = new Person();
        PersonOutputSimpleDto personOutputSimpleDto = new PersonOutputSimpleDto(); // Using the specific implementation

        Mockito.when(personMapper.toPersonaEntity(inputDto)).thenReturn(person);
        Mockito.when(personRepository.save(person)).thenReturn(person);
        Mockito.when(personMapper.toPersonaOutputSimpleDto(person)).thenReturn(personOutputSimpleDto);

        // Act
        PersonOutputDto result = personService.addPerson(inputDto);

        // Assert
        assertEquals(personOutputSimpleDto, result);
        Mockito.verify(personRepository).save(person);
        Mockito.verify(personMapper).toPersonaOutputSimpleDto(person);
    }



    //getPersonById TESTS
    /**
     * Test for the {@code getPersonById} method in {@link PersonService}.
     *
     * Case: Valid ID with simple output type.
     * Ensures that when a valid ID and output type "SIMPLE" are provided,
     * the service retrieves the person from the repository and maps it to a {@link PersonOutputSimpleDto}.
     *
     * @throws EntityNotFoundException if the person with the given ID is not found (not expected in this case).
     */
    @Test
    void getPersonById_WithValidIdAndSimpleOutput_ReturnsSimpleDto() throws EntityNotFoundException {
        // Arrange
        Integer id = 1;
        Person person = new Person();
        PersonOutputSimpleDto personOutputSimpleDto = new PersonOutputSimpleDto();

        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));
        Mockito.when(personMapper.toPersonaOutputSimpleDto(person)).thenReturn(personOutputSimpleDto);

        // Act
        PersonOutputDto result = personService.getPersonById(id, OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(personOutputSimpleDto, result);
        Mockito.verify(personRepository).findById(id);
        Mockito.verify(personMapper).toPersonaOutputSimpleDto(person);
    }

    /**
     * Test for the {@code getPersonById} method in {@link PersonService}.
     *
     * Case: Valid ID with full output type.
     * Ensures that when a valid ID and output type "FULL" are provided,
     * the service retrieves the person from the repository and maps it to a {@link PersonOutputFullDto}.
     *
     * @throws EntityNotFoundException if the person with the given ID is not found (not expected in this case).
     */
    @Test
    void getPersonById_WithValidIdAndFullOutput_ReturnsFullDto() throws EntityNotFoundException {
        // Arrange
        Integer id = 1;
        Person person = new Person();
        PersonOutputFullDto personOutputFullDto = new PersonOutputFullDto();

        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));
        Mockito.when(personMapper.toPersonaOutputFullDto(person)).thenReturn(personOutputFullDto);

        // Act
        PersonOutputDto result = personService.getPersonById(id, OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(personOutputFullDto, result);
        Mockito.verify(personRepository).findById(id);
        Mockito.verify(personMapper).toPersonaOutputFullDto(person);
    }

    /**
     * Test for the {@code getPersonById} method in {@link PersonService}.
     *
     * Case: Invalid ID.
     * Ensures that when an invalid ID is provided, the service throws
     * an {@link EntityNotFoundException} with the appropriate message.
     */
    @Test
    void getPersonById_WithInvalidId_ThrowsEntityNotFoundException() {
        // Arrange
        Integer id = 999;
        Mockito.when(personRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personService.getPersonById(id, StringConstants.OUTPUT_TYPE_SIMPLE);
        });

        // Assert
        assertTrue(exception.getMessage().contains(StringConstants.PERSON_ID_NOT_FOUND + id));
        Mockito.verify(personRepository).findById(id);
    }

    /**
     * Test for the {@code getPersonById} method in {@link PersonService}.
     *
     * Case: Invalid output type.
     * Ensures that when an invalid output type is provided, the service throws
     * a {@link WrongOutputTypeException}.
     *
     * Verifies that the repository is not accessed in this case since the output type validation occurs first.
     */
    @Test
    void getPersonById_WithInvalidOutputType_ThrowsWrongOutputTypeException() {
        // Arrange
        Integer id = 1;

        // Act
        assertThrows(WrongOutputTypeException.class, () -> {
            personService.getPersonById(id, OUTPUT_TYPE_INVALID);
        });

        // Assert
        verifyNoInteractions(personRepository);
    }


    //getPersonByUsername TESTS
    /**
     * Test for the {@code getPersonByUsername} method in {@link PersonService}.
     *
     * Case: Valid username with simple output type.
     * Ensures that when a valid username and output type "SIMPLE" are provided,
     * the service retrieves matching persons from the repository and maps them
     * to a list of {@link PersonOutputSimpleDto}.
     *
     * @throws EntityNotFoundException if no matching persons are found (not expected in this case).
     */



    @Test
    void getPersonByUsername_WithValidUsernameAndSimpleOutput_ReturnsListOfSimpleDtos() throws EntityNotFoundException {
        // Arrange
        String outputType = StringConstants.OUTPUT_TYPE_SIMPLE;
        Person person = new Person();
        PersonOutputSimpleDto personOutputSimpleDto = new PersonOutputSimpleDto();

        Mockito.when(personRepository.findAllByUsernameContaining(USERNAME_JOHN)).thenReturn(List.of(person));
        Mockito.when(personMapper.toPersonaOutputSimpleDto(person)).thenReturn(personOutputSimpleDto);

        // Act
        List<PersonOutputDto> result = personService.getPersonByUsername(USERNAME_JOHN, outputType);

        // Assert
        assertEquals(1, result.size());
        assertEquals(personOutputSimpleDto, result.get(0));
        Mockito.verify(personRepository).findAllByUsernameContaining(USERNAME_JOHN);
        Mockito.verify(personMapper).toPersonaOutputSimpleDto(person);
    }

    /**
     * Test for the {@code getPersonByUsername} method in {@link PersonService}.
     *
     * Case: Valid username with full output type.
     * Ensures that when a valid username and output type "FULL" are provided,
     * the service retrieves matching persons from the repository and maps them
     * to a list of {@link PersonOutputFullDto}.
     *
     * @throws EntityNotFoundException if no matching persons are found (not expected in this case).
     */
    @Test
    void getPersonByUsername_WithValidUsernameAndFullOutput_ReturnsListOfFullDtos() throws EntityNotFoundException {
        // Arrange
        Person person = new Person();
        PersonOutputFullDto personOutputFullDto = new PersonOutputFullDto();

        Mockito.when(personRepository.findAllByUsernameContaining(USERNAME_JOHN)).thenReturn(List.of(person));
        Mockito.when(personMapper.toPersonaOutputFullDto(person)).thenReturn(personOutputFullDto);

        // Act
        List<PersonOutputDto> result = personService.getPersonByUsername(USERNAME_JOHN, OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(1, result.size());
        assertEquals(personOutputFullDto, result.get(0));
        Mockito.verify(personRepository).findAllByUsernameContaining(USERNAME_JOHN);
        Mockito.verify(personMapper).toPersonaOutputFullDto(person);
    }

    /**
     * Test for the {@code getPersonByUsername} method in {@link PersonService}.
     *
     * Case: No matching users.
     * Ensures that when no persons with the given username are found,
     * the service throws an {@link EntityNotFoundException} with the appropriate message.
     */
    @Test
    void getPersonByUsername_WithNoMatchingUsers_ThrowsEntityNotFoundException() {
        // Arrange

        Mockito.when(personRepository.findAllByUsernameContaining(USERNAME_NOEXISTENT)).thenReturn(Collections.emptyList());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personService.getPersonByUsername(USERNAME_NOEXISTENT, OUTPUT_TYPE_SIMPLE);
        });

        assertTrue(exception.getMessage().contains(PERSON_EMPTY));
        Mockito.verify(personRepository).findAllByUsernameContaining(USERNAME_NOEXISTENT);
    }


    //getAllPerson TESTS
    /**
     * Test for the {@code getAllPerson} method in {@link PersonService}.
     *
     * Case: Simple output type.
     * Ensures that when the output type is "SIMPLE", the service retrieves all persons
     * from the repository and maps them to a list of {@link PersonOutputSimpleDto}.
     *
     * @throws EntityNotFoundException if no persons are found (not expected in this case).
     */
    @Test
    void getAllPerson_WithSimpleOutput_ReturnsListOfSimpleDtos() throws EntityNotFoundException {
        // Arrange
        Person person = new Person();
        PersonOutputSimpleDto personOutputSimpleDto = new PersonOutputSimpleDto();

        Mockito.when(personRepository.findAll()).thenReturn(List.of(person));
        Mockito.when(personMapper.toPersonaOutputSimpleDto(person)).thenReturn(personOutputSimpleDto);

        // Act
        List<PersonOutputDto> result = personService.getAllPerson(OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(1, result.size());
        assertEquals(personOutputSimpleDto, result.get(0));
        Mockito.verify(personRepository).findAll();
        Mockito.verify(personMapper).toPersonaOutputSimpleDto(person);
    }

    /**
     * Test for the {@code getAllPerson} method in {@link PersonService}.
     *
     * Case: Full output type.
     * Ensures that when the output type is "FULL", the service retrieves all persons
     * from the repository and maps them to a list of {@link PersonOutputFullDto}.
     *
     * @throws EntityNotFoundException if no persons are found (not expected in this case).
     */
    @Test
    void getAllPerson_WithFullOutput_ReturnsListOfFullDtos() throws EntityNotFoundException {
        // Arrange
        Person person = new Person();
        PersonOutputFullDto personOutputFullDto = new PersonOutputFullDto();

        Mockito.when(personRepository.findAll()).thenReturn(List.of(person));
        Mockito.when(personMapper.toPersonaOutputFullDto(person)).thenReturn(personOutputFullDto);

        // Act
        List<PersonOutputDto> result = personService.getAllPerson(OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(1, result.size());
        assertEquals(personOutputFullDto, result.get(0));
        Mockito.verify(personRepository).findAll();
        Mockito.verify(personMapper).toPersonaOutputFullDto(person);
    }

    /**
     * Test for the {@code getAllPerson} method in {@link PersonService}.
     *
     * Case: No persons in the repository.
     * Ensures that when no persons are found in the repository, the service throws an
     * {@link EntityNotFoundException} with the appropriate message.
     */
    @Test
    void getAllPerson_WithNoPersons_ThrowsEntityNotFoundException() {
        // Arrange
        Mockito.when(personRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personService.getAllPerson(StringConstants.OUTPUT_TYPE_SIMPLE);
        });

        assertTrue(exception.getMessage().contains(StringConstants.PERSON_EMPTY));
        Mockito.verify(personRepository).findAll();
    }

    /**
     * Test for the {@code getAllPerson} method in {@link PersonService}.
     *
     * Case: Invalid output type.
     * Ensures that when an invalid output type is provided, the service throws a
     * {@link WrongOutputTypeException}.
     */
    @Test
    void getAllPerson_WithInvalidOutputType_ThrowsWrongOutputTypeException() {
        // Arrange
        Person person = new Person();

        Mockito.when(personRepository.findAll()).thenReturn(List.of(person));

        // Act & Assert
        assertThrows(WrongOutputTypeException.class, () -> {
            personService.getAllPerson(OUTPUT_TYPE_INVALID);
        });

        Mockito.verify(personRepository).findAll();
    }


    //updatePerson TEST
    @Test
    void updatePerson_WithValidInput_ReturnsUpdatedSimpleDto() throws EntityNotFoundException, UnprocessableEntityException {
        // Arrange
        Integer id = 1;
        PersonInputDto inputDto = new PersonInputDto();
        Person existingPerson = new Person();
        PersonOutputSimpleDto outputDto = new PersonOutputSimpleDto();

        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(existingPerson));
        Mockito.doNothing().when(personMapper).updatePersonaEntityFromDto(inputDto, existingPerson);
        Mockito.when(personRepository.save(existingPerson)).thenReturn(existingPerson);
        Mockito.when(personMapper.toPersonaOutputSimpleDto(existingPerson)).thenReturn(outputDto);

        // Act
        PersonOutputDto result = personService.updatePerson(id, inputDto);

        // Assert
        assertEquals(outputDto, result);
        Mockito.verify(personRepository).findById(id);
        Mockito.verify(personMapper).updatePersonaEntityFromDto(inputDto, existingPerson);
        Mockito.verify(personRepository).save(existingPerson);
        Mockito.verify(personMapper).toPersonaOutputSimpleDto(existingPerson);
    }

    @Test
    void updatePerson_WithInvalidId_ThrowsEntityNotFoundException() {
        // Arrange
        Integer id = 99;
        PersonInputDto inputDto = new PersonInputDto();

        Mockito.when(personRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personService.updatePerson(id, inputDto);
        });

        assertTrue(exception.getMessage().contains(PERSON_ID_NOT_FOUND + id));
        Mockito.verify(personRepository).findById(id);
    }



    //deletePerson TEST
    /**
     * Test for the {@code deletePerson} method in {@link PersonService}.
     *
     * Case: Person has an associated professor.
     * Ensures that when a person has an associated professor, an attempt to delete the person results in an
     * {@link UnprocessableEntityException}, and the delete operation is not executed.
     */
    @Test
    void deletePerson_WithAssociatedProfessor_ThrowsUnprocessableEntityException() {
        // Arrange
        Integer id = 1;
        Person person = new Person();
        Professor professor = Mockito.mock(Professor.class);
        person.setProfessor(professor);
        person.setStudent(null);

        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));

        // Act & Assert
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> {
            personService.deletePerson(id);
        });

        assertTrue(exception.getMessage().contains(StringConstants.PROFESSOR_EXIST));
        Mockito.verify(personRepository).findById(id);
        Mockito.verify(personRepository, Mockito.never()).deleteById(Mockito.anyInt());
    }

    /**
     * Test for the {@code deletePerson} method in {@link PersonService}.
     *
     * Case: Person has an associated student.
     * Ensures that when a person has an associated student, an attempt to delete the person results in an
     * {@link UnprocessableEntityException}, and the delete operation is not executed.
     */
    @Test
    void deletePerson_WithAssociatedStudent_ThrowsUnprocessableEntityException() {
        // Arrange
        Integer id = 1;
        Person person = new Person();
        Student student = Mockito.mock(Student.class);
        person.setProfessor(null);
        person.setStudent(student);

        Mockito.when(personRepository.findById(id)).thenReturn(Optional.of(person));

        // Act & Assert
        UnprocessableEntityException exception = assertThrows(UnprocessableEntityException.class, () -> {
            personService.deletePerson(id);
        });

        assertTrue(exception.getMessage().contains(StringConstants.STUDENT_EXIST));
        Mockito.verify(personRepository).findById(id);
        Mockito.verify(personRepository, Mockito.never()).deleteById(Mockito.anyInt());
    }


    //checkoutputType TEST
    @Test
    void checkoutputType_NullInput_ReturnsDefaultType() {
        // Arrange
        PersonServiceImpl personService = new PersonServiceImpl();

        // Act
        String result = personService.checkoutputType(OUTPUT_TYPE_NULL);

        // Assert
        assertEquals(StringConstants.OUTPUT_TYPE_SIMPLE, result);
    }

    @Test
    void checkoutputType_EmptyInput_ThrowsWrongOutputTypeException() {
        // Arrange
        PersonServiceImpl personService = new PersonServiceImpl();

        // Act & Assert
        assertThrows(WrongOutputTypeException.class, () -> personService.checkoutputType(OUTPUT_TYPE_EMPTY));
    }

    @Test
    void checkoutputType_ValidInput_ReturnsSameType() {
        // Arrange
        PersonServiceImpl personService = new PersonServiceImpl();

        // Act
        String result = personService.checkoutputType(OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(OUTPUT_TYPE_FULL, result);
    }

    //getPersonsByCriteria TEST
    //pasando un número de página negativo (se lanza una excepción)
    @Test
    void getPersonsByCriteria_negativePage_throwsException() {
        // Arrange
        int page = -1;
        int size = 10;

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> personService.getPersonsByCriteria(null, null, null, null, null, null, page, size)
        );

        // Verifica que el mensaje de error sea el esperado
        assertEquals(PATTERN_PAGE, exception.getMessage());
    }

    //Pasando un tamaño de página inválido (se lanza una excepción).
    @Test
    void getPersonsByCriteria_invalidSize_throwsException() {
        // Arrange
        int page = 0;
        int size = 0;

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> personService.getPersonsByCriteria(null, null, null, null, null, null, page, size)
        );

        // Verifica que el mensaje de error sea el esperado
        assertEquals(CORRECT_SIZE, exception.getMessage());
    }

    //que fechaInferior sea después de fechaSuperio (lanza una excepción).
    @Test
    void getPersonsByCriteria_invalidDateRange_throwsException() {
        // Arrange
        Date fechaInferior = new GregorianCalendar(2023, Calendar.DECEMBER, 20).getTime();
        Date fechaSuperior = new GregorianCalendar(2023, Calendar.DECEMBER, 10).getTime(); // Fecha "antes" de fechaInferior

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> personService.getPersonsByCriteria(null, null, null, fechaSuperior, fechaInferior, null, 0, 10)
        );

        // Verifica que el mensaje de error sea el esperado
        assertEquals(CORRECT_DATE, exception.getMessage());
    }

    //orderBy inválido (se lanza una excepción).
    @Test
    void getPersonsByCriteria_invalidOrderBy_throwsException() {
        // Arrange

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> personService.getPersonsByCriteria(null, null, null, null, null, INVALID_ORDERBY_VARIABLE_TEST, 0, 10)
        );

        System.out.println(exception.getMessage());

        // Verifica que el mensaje de error sea el esperado
        assertEquals(ERROR_MSG_ORDERBY, exception.getMessage());
    }

    //verifica que el metodo llama correctamente al repositorio con los parámetros calculados.
    @Test
    void getPersonsByCriteria_repositoryCalledWithCorrectArguments() {
        // Arrange
        String username = "johnDoe";
        String name = "John";
        String surname = "Doe";
        Date fechaSuperior = new GregorianCalendar(2023, Calendar.DECEMBER, 20).getTime();
        Date fechaInferior = new GregorianCalendar(2023, Calendar.DECEMBER, 10).getTime();
        String orderBy = "name";
        int page = 1;
        int size = 10;
        int offset = page * size;
        int limit = size;

        List<Person> mockPersons = List.of(new Person());
        Mockito.when(personRepository.findByCriteriaWithPagination(
                Mockito.eq(username), Mockito.eq(name), Mockito.eq(surname),
                Mockito.eq(fechaSuperior), Mockito.eq(fechaInferior),
                Mockito.eq(orderBy), Mockito.eq(offset), Mockito.eq(limit)
        )).thenReturn(mockPersons);

        PersonOutputSimpleDto dto = new PersonOutputSimpleDto();
        Mockito.when(personMapper.toPersonaOutputSimpleDto(Mockito.any(Person.class))).thenReturn(dto);

        // Act
        List<PersonOutputDto> result = personService.getPersonsByCriteria(
                username, name, surname, fechaSuperior, fechaInferior, orderBy, page, size
        );

        // Assert
        // Verifica que el repositorio fue llamado con los parámetros correctos
        Mockito.verify(personRepository).findByCriteriaWithPagination(
                Mockito.eq(username), Mockito.eq(name), Mockito.eq(surname),
                Mockito.eq(fechaSuperior), Mockito.eq(fechaInferior),
                Mockito.eq(orderBy), Mockito.eq(offset), Mockito.eq(limit)
        );

        // Verifica los resultados
        assertNotNull(result);
        assertEquals(1, result.size());
    }


}
