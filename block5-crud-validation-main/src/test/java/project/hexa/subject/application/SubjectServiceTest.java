package project.hexa.subject.application;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import project.hexa.exception.EntityNotFoundException;
import project.hexa.exception.UnprocessableEntityException;
import project.hexa.exception.WrongOutputTypeException;
import project.hexa.mapper.SubjectMapper;
import project.hexa.student.domain.Student;
import project.hexa.subject.domain.Subject;
import project.hexa.subject.infrastructure.controller.dto.SubjectInputDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputFullDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputSimpleDto;
import project.hexa.subject.infrastructure.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static project.hexa.components.StringConstants.*;


@SpringBootTest
public class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private SubjectMapper subjectMapper;

    @InjectMocks
    private SubjectServiceImpl subjectService;


    //
    @Test
    void addSubject_ValidInput_ReturnsSubjectOutputDto() {
        // Arrange
        SubjectInputDto subjectInputDto = new SubjectInputDto();
        Subject subject = new Subject();
        SubjectOutputSimpleDto subjectOutputSimpleDto = new SubjectOutputSimpleDto();

        when(subjectMapper.toAsignaturaEntity(subjectInputDto)).thenReturn(subject);
        when(subjectRepository.save(subject)).thenReturn(subject);
        when(subjectMapper.toAsignaturaOutputSimpleDto(subject)).thenReturn(subjectOutputSimpleDto);

        // Act
        SubjectOutputDto result = subjectService.addSubject(subjectInputDto);

        // Assert
        assertEquals(subjectOutputSimpleDto, result);
        verify(subjectMapper).toAsignaturaEntity(subjectInputDto);
        verify(subjectRepository).save(subject);
        verify(subjectMapper).toAsignaturaOutputSimpleDto(subject);
    }

    //getSubjectById
    @Test
    void getSubjectById_ValidIdAndSimpleOutput_ReturnsSimpleDto() {
        // Arrange
        Integer subjectId = 1;
        Subject subject = new Subject();
        SubjectOutputSimpleDto subjectOutputSimpleDto = new SubjectOutputSimpleDto();

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(subjectMapper.toAsignaturaOutputSimpleDto(subject)).thenReturn(subjectOutputSimpleDto);

        // Act
        SubjectOutputDto result = subjectService.getSubjectById(subjectId, OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(subjectOutputSimpleDto, result);
        verify(subjectMapper).toAsignaturaOutputSimpleDto(subject);
    }

    @Test
    void getSubjectById_ValidIdAndFullOutput_ReturnsFullDto() {
        // Arrange
        Integer subjectId = 1;
        Subject subject = new Subject();
        SubjectOutputFullDto subjectOutputFullDto = new SubjectOutputFullDto();

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));
        when(subjectMapper.toAsignaturaOutputFullDto(subject)).thenReturn(subjectOutputFullDto);

        // Act
        SubjectOutputDto result = subjectService.getSubjectById(subjectId, OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(subjectOutputFullDto, result);
        verify(subjectMapper).toAsignaturaOutputFullDto(subject);
    }

    @Test
    void getSubjectById_NonExistentId_ThrowsEntityNotFoundException() {
        // Arrange
        Integer subjectId = 99;
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> subjectService.getSubjectById(subjectId, OUTPUT_TYPE_SIMPLE));
    }

    @Test
    void getSubjectById_InvalidOutputType_ThrowsWrongOutputTypeException() {
        // Arrange
        Integer subjectId = 1;
        Subject subject = new Subject();

        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject));

        // Act & Assert
        assertThrows(WrongOutputTypeException.class, () -> subjectService.getSubjectById(subjectId, OUTPUT_TYPE_INVALID));
    }

    //getAllSubjects TEST
    @Test
    void testGetAllSubjectsWithSimpleOutputType() throws EntityNotFoundException {
        // Arrange
        Subject subject = new Subject();
        SubjectOutputSimpleDto subjectOutputSimpleDto = new SubjectOutputSimpleDto();
        List<Subject> subjects = List.of(subject);

        when(subjectRepository.findAll()).thenReturn(subjects);
        when(subjectMapper.toAsignaturaOutputSimpleDto(subject)).thenReturn(subjectOutputSimpleDto);

        // Act
        List<SubjectOutputDto> result = subjectService.getAllSubjects(OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(1, result.size());
        verify(subjectRepository).findAll();
        verify(subjectMapper).toAsignaturaOutputSimpleDto(subject);
    }

    @Test
    void testGetAllSubjectsWithFullOutputType() throws EntityNotFoundException {
        // Arrange
        Subject subject = new Subject();
        SubjectOutputFullDto subjectOutputFullDto = new SubjectOutputFullDto();
        List<Subject> subjects = List.of(subject);

        when(subjectRepository.findAll()).thenReturn(subjects);
        when(subjectMapper.toAsignaturaOutputFullDto(subject)).thenReturn(subjectOutputFullDto);

        // Act
        List<SubjectOutputDto> result = subjectService.getAllSubjects(OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(1, result.size());
        verify(subjectRepository).findAll();
        verify(subjectMapper).toAsignaturaOutputFullDto(subject);
    }

    //OUTPUTTYPE invalido
    @Test
    void testGetAllSubjectsWithWrongOutputType() {
        // Arrange no es necesario ya que va a lanzar una excapcion antes

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> subjectService.getAllSubjects(OUTPUT_TYPE_INVALID));
    }

    //cuando se lanza una lista vacia
    @Test
    void testGetAllSubjectsThrowsEntityNotFoundException() {
        // Arrange
        when(subjectRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> subjectService.getAllSubjects(OUTPUT_TYPE_SIMPLE));
    }

    //UpdateSubject TEST
    @Test
    void testUpdateSubjectWithValidId() throws EntityNotFoundException, UnprocessableEntityException {
        // Arrange
        Integer id = 1;
        SubjectInputDto subjectInputDto = new SubjectInputDto();
        Subject existingSubject = new Subject();


        SubjectOutputSimpleDto subjectOutputSimpleDto = new SubjectOutputSimpleDto();


        when(subjectRepository.findById(id)).thenReturn(Optional.of(existingSubject));
        //when(subjectMapper.updateAsignaturaEntityFromDto(subjectInputDto, existingSubject)).thenReturn(existingSubject);
        when(subjectMapper.toAsignaturaOutputSimpleDto(existingSubject)).thenReturn(subjectOutputSimpleDto);
        when(subjectRepository.save(existingSubject)).thenReturn(existingSubject);


        // Act
        SubjectOutputDto result = subjectService.updateSubject(id, subjectInputDto);

        // Assert
        assertNotNull(result);// Verificamos que el resultado no sea null

        verify(subjectRepository).findById(id);
        verify(subjectMapper).updateAsignaturaEntityFromDto(subjectInputDto, existingSubject);
        verify(subjectRepository).save(existingSubject);
        verify(subjectMapper).toAsignaturaOutputSimpleDto(existingSubject);
    }

    //deleteSubject TEST
    @Test
    void testDeleteSubjectWhenIdExists() throws EntityNotFoundException {
        // Arrange
        Integer id = 1;
        Subject subject = mock(Subject.class);
        List<Student> students = new ArrayList<>();

        // Creamos estudiantes con la relación a la asignatura
        Student student1 = mock(Student.class);
        Student student2 = mock(Student.class);
        students.add(student1);
        students.add(student2);

        // Configuramos los mocks
        when(subject.getStudents()).thenReturn(students);
        when(subjectRepository.findById(id)).thenReturn(Optional.of(subject));
        when(student1.getSubjects()).thenReturn(new ArrayList<>(List.of(subject)));
        when(student2.getSubjects()).thenReturn(new ArrayList<>(List.of(subject)));

        // Act
        subjectService.deleteSubject(id);

        // Assert
        verify(subjectRepository).findById(id);
        verify(subject).getStudents();
        verify(student1).getSubjects();
        verify(student2).getSubjects();
        verify(subjectRepository).deleteById(id);
    }

    @Test
    void testDeleteSubjectThrowsEntityNotFoundException() {
        // Arrange
        Integer id = 1;
        when(subjectRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> subjectService.deleteSubject(id));

        // Verifica que `deleteById` nunca se llama
        verify(subjectRepository, never()).deleteById(any());
    }


    //checkoutputType TEST
    @Test
    void checkoutputType_NullInput_ReturnsDefaultType() {
        // Arrange
        SubjectServiceImpl subjectService = new SubjectServiceImpl();

        // Act
        String result = subjectService.checkoutputType(OUTPUT_TYPE_NULL);

        // Assert
        assertEquals(OUTPUT_TYPE_SIMPLE, result);
    }

    @Test
    void checkoutputType_EmptyInput_ThrowsWrongOutputTypeException() {
        // Arrange
        SubjectServiceImpl subjectService = new SubjectServiceImpl();

        // Act & Assert
        assertThrows(WrongOutputTypeException.class, () -> subjectService.checkoutputType(OUTPUT_TYPE_EMPTY));
    }

    @Test
    void checkoutputType_ValidInput_ReturnsSameType() {
        // Arrange
        SubjectServiceImpl subjectService = new SubjectServiceImpl();

        // Act
        String result = subjectService.checkoutputType(OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(OUTPUT_TYPE_FULL, result);
    }
}
