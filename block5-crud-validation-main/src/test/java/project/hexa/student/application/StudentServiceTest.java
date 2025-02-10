package project.hexa.student.application;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import project.hexa.components.StringConstants;
import project.hexa.exception.EntityNotFoundException;
import project.hexa.exception.WrongOutputTypeException;
import project.hexa.mapper.StudentMapper;
import project.hexa.mapper.SubjectMapper;
import project.hexa.person.domain.Person;
import project.hexa.person.infrastructure.repository.PersonRepository;
import project.hexa.professor.domain.Professor;
import project.hexa.professor.infrastructure.repository.ProfessorRepository;
import project.hexa.student.domain.Student;
import project.hexa.student.infrastructure.controller.dto.*;
import project.hexa.student.infrastructure.repository.StudentRepository;
import project.hexa.subject.domain.Subject;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputSimpleDto;
import project.hexa.subject.infrastructure.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static project.hexa.components.StringConstants.*;

@SpringBootTest
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private SubjectMapper subjectMapper;

    private final Integer studentId = 1;

    @InjectMocks
    private StudentServiceImpl studentService;

    //addStudent TEST
    @Test
    void addStudent_ValidInput_ReturnsCreatedStudent() {
        // Arrange
        StudentInputDto studentInputDto = new StudentInputDto();
        studentInputDto.setId_person(1);
        studentInputDto.setId_professor(2);

        Student studentEntity = new Student();
        studentEntity.setId_student(1);

        StudentOutputSimpleDto studentOutputSimpleDto = new StudentOutputSimpleDto();
        studentOutputSimpleDto.setId_student(1);

        studentOutputSimpleDto.setId_student(1);

        Person mockPerson = new Person();
        Professor mockProfessor = new Professor();

        // Mockeo de los repositorios y mapper
        when(personRepository.findById(1)).thenReturn(Optional.of(mockPerson));
        when(professorRepository.findById(2)).thenReturn(Optional.of(mockProfessor));
        when(studentMapper.toStudentEntity(studentInputDto)).thenReturn(studentEntity);
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);
        when(studentMapper.toStudentOutputSimpleDto(studentEntity)).thenReturn(studentOutputSimpleDto);

        // Act
        StudentOutputDto result = studentService.addStudent(studentInputDto);

        // Assert
        assertEquals(studentOutputSimpleDto, result);
        verify(personRepository).findById(1);
        verify(professorRepository).findById(2);
        verify(studentMapper).toStudentEntity(studentInputDto);
        verify(studentRepository).save(studentEntity);
        verify(studentMapper).toStudentOutputSimpleDto(studentEntity);
    }

    //getStudentById TEST
    @Test
    void getStudentById_SimpleOutput_ReturnsStudent() {
        // Arrange
        Student student = new Student();
        student.setId_student(studentId);

        StudentOutputSimpleDto expectedDto = new StudentOutputSimpleDto();
        expectedDto.setId_student(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toStudentOutputSimpleDto(student)).thenReturn(expectedDto);

        // Act
        StudentOutputDto result = studentService.getStudentById(studentId, OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(expectedDto, result);
        verify(studentRepository).findById(studentId);
        verify(studentMapper).toStudentOutputSimpleDto(student);
    }

    @Test
    void getAllStudents_FullOutput_ReturnsListOfFullDtos() {

        // Arrange
        Student student1 = new Student();
        Student student2 = new Student();
        List<Student> students = List.of(student1, student2);

        StudentOutputFullDto studentOutputFullDto1 = new StudentOutputFullDto();
        StudentOutputFullDto studentOutputFullDto2 = new StudentOutputFullDto();


        Mockito.when(studentRepository.findAll()).thenReturn(students);
        Mockito.when(studentMapper.toStudentOutputFullDto(student1)).thenReturn(studentOutputFullDto1);
        Mockito.when(studentMapper.toStudentOutputFullDto(student2)).thenReturn(studentOutputFullDto2);

        // Act
        List<StudentOutputDto> result = studentService.getAllStudents(OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(2, result.size());
        assertEquals(studentOutputFullDto1, result.get(0));
        assertEquals(studentOutputFullDto2, result.get(1));


        Mockito.verify(studentRepository).findAll();
        Mockito.verify(studentMapper).toStudentOutputFullDto(student1);
        Mockito.verify(studentMapper).toStudentOutputFullDto(student2);

        // Confirmamos que no se intentó mapear a la salida simple
        Mockito.verify(studentMapper, Mockito.never()).toStudentOutputSimpleDto(Mockito.any());
    }

    @Test
    void getStudentById_FullOutput_ReturnsStudent() {
        // Arrange
        int studentId = 1;

        Student student = new Student();
        student.setId_student(studentId);

        StudentOutputFullDto expectedDto = new StudentOutputFullDto();
        expectedDto.setId_student(studentId);


        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        Mockito.when(studentMapper.toStudentOutputFullDto(student)).thenReturn(expectedDto);

        // Act
        StudentOutputDto result = studentService.getStudentById(studentId, OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(expectedDto, result);

        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(studentMapper).toStudentOutputFullDto(student);

        // Confirmamos que no se intentó mapear a la salida simple
        Mockito.verify(studentMapper, Mockito.never()).toStudentOutputSimpleDto(Mockito.any());
    }


    @Test
    void getStudentById_InvalidOutputType_ThrowsWrongOutputTypeException() {
        // Arrange

        // Act & Assert
        WrongOutputTypeException exception = assertThrows(
                WrongOutputTypeException.class,
                () -> studentService.getStudentById(studentId, OUTPUT_TYPE_INVALID)
        );

        assertEquals(StringConstants.WRONG_OUTPUT_TYPE, exception.getMessage());
        //verifica que no se haya interactuado con el mapper ya que antes se lanza una excepcion
        verifyNoInteractions(studentMapper);
    }



    //getAllStudents TEST
    @Test
    void getAllStudents_SimpleOutput_ReturnsListOfSimpleDtos() {

        // Arrange
        Student student1 = new Student();
        Student student2 = new Student();
        List<Student> students = List.of(student1, student2);

        StudentOutputSimpleDto studentOutputSimpleDto1 = new StudentOutputSimpleDto();
        StudentOutputSimpleDto studentOutputSimpleDto2 = new StudentOutputSimpleDto();

        Mockito.when(studentRepository.findAll()).thenReturn(students);
        Mockito.when(studentMapper.toStudentOutputSimpleDto(student1)).thenReturn(studentOutputSimpleDto1);
        Mockito.when(studentMapper.toStudentOutputSimpleDto(student2)).thenReturn(studentOutputSimpleDto2);

        // Act
        List<StudentOutputDto> result = studentService.getAllStudents(OUTPUT_TYPE_SIMPLE);

        // Assert
        assertEquals(2, result.size());
        assertEquals(studentOutputSimpleDto1, result.get(0));
        assertEquals(studentOutputSimpleDto2, result.get(1));
        Mockito.verify(studentRepository).findAll();
        Mockito.verify(studentMapper).toStudentOutputSimpleDto(student1);
        Mockito.verify(studentMapper).toStudentOutputSimpleDto(student2);
    }

    //updateStuden TEST
    @Test
    void updateStudent_ValidInput_ReturnsUpdatedStudentDto(){
        // Arrange
        Integer studentId = 1;
        Integer personId = 2;
        Integer professorId = 3;

        StudentInputDto studentInputDto = new StudentInputDto();
        studentInputDto.setId_person(personId);
        studentInputDto.setId_professor(professorId);

        // Creamos el estudiante existente con una persona asociada
        Student existingStudent = new Student();
        Person person = new Person();
        person.setId_person(personId);
        existingStudent.setPerson(person);


        // Creamos el profesor
        Professor professor = new Professor();
        professor.setId_professor(professorId);
        //existingStudent.setProfessor(professor);

        StudentOutputSimpleDto studentOutputSimpleDto = new StudentOutputSimpleDto();

        // Mocks
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        Mockito.when(studentMapper.toStudentOutputSimpleDto(existingStudent)).thenReturn(studentOutputSimpleDto);
        Mockito.when(studentRepository.save(existingStudent)).thenReturn(existingStudent);

        // Act
        StudentOutputDto result = studentService.updateStudent(studentId, studentInputDto);

        // Assert
        assertEquals(studentOutputSimpleDto, result);
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(studentMapper).updateStudentEntityFromDto(studentInputDto, existingStudent);
        Mockito.verify(studentRepository).save(existingStudent);
    }

    //Asignandole profesor al estudiante existente
    @Test
    void updateStudent_ValidInput_ReturnsUpdatedStudentDto_Asignprofessor(){
        // Arrange
        Integer studentId = 1;
        Integer personId = 2;
        Integer professorId = 3;

        StudentInputDto studentInputDto = new StudentInputDto();
        studentInputDto.setId_person(personId);
        studentInputDto.setId_professor(professorId);

        // Creamos el estudiante existente con una persona asociada
        Student existingStudent = new Student();
        Person person = new Person();
        person.setId_person(personId);
        existingStudent.setPerson(person);

        // Creamos el profesor
        Professor professor = new Professor();
        professor.setId_professor(professorId);
        existingStudent.setProfessor(professor);

        StudentOutputSimpleDto studentOutputSimpleDto = new StudentOutputSimpleDto();

        // Mocks
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        Mockito.when(studentMapper.toStudentOutputSimpleDto(existingStudent)).thenReturn(studentOutputSimpleDto);
        Mockito.when(studentRepository.save(existingStudent)).thenReturn(existingStudent);

        // Act
        StudentOutputDto result = studentService.updateStudent(studentId, studentInputDto);

        // Assert
        assertEquals(studentOutputSimpleDto, result);
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(studentMapper).updateStudentEntityFromDto(studentInputDto, existingStudent);
        Mockito.verify(studentRepository).save(existingStudent);
    }

    //Asignandole profesor al estudiante existente
    @Test
    void updateStudent_ValidInput_ReturnsUpdatedStudentDto3(){
        // Arrange
        Integer studentId = 1;
        Integer personId = 2;
        Integer professorId = 3;
        Integer professorId2 = 2;

        StudentInputDto studentInputDto = new StudentInputDto();
        studentInputDto.setId_person(personId);
        studentInputDto.setId_professor(professorId2);

        // Creamos el estudiante existente con una persona asociada
        Student existingStudent = new Student();
        Person person = new Person();
        person.setId_person(personId);
        existingStudent.setPerson(person);

        // Creamos el profesor
        Professor professor = new Professor();
        Professor professor2 = new Professor();
        professor.setId_professor(professorId);
        professor2.setId_professor(professorId2);
        existingStudent.setProfessor(professor);

        StudentOutputSimpleDto studentOutputSimpleDto = new StudentOutputSimpleDto();

        // Mocks
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        Mockito.when(professorRepository.findById(professorId2)).thenReturn(Optional.of(professor2));
        Mockito.when(studentMapper.toStudentOutputSimpleDto(existingStudent)).thenReturn(studentOutputSimpleDto);
        Mockito.when(studentRepository.save(existingStudent)).thenReturn(existingStudent);

        // Act
        StudentOutputDto result = studentService.updateStudent(studentId, studentInputDto);

        // Assert
        assertEquals(studentOutputSimpleDto, result);
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(studentMapper).updateStudentEntityFromDto(studentInputDto, existingStudent);
        Mockito.verify(studentRepository).save(existingStudent);
    }

    @Test
    void updateStudent_ValidInput_ReturnsUpdatedStudentDto4(){
        // Arrange
        Integer studentId = 1;
        Integer personId = 2;
        Integer professorId = null;
        //Integer professorId2 = 2;

        StudentInputDto studentInputDto = new StudentInputDto();
        studentInputDto.setId_person(personId);
        //inputDto.setId_professor(professorId);

        // Creamos el estudiante existente con una persona asociada
        Student existingStudent = new Student();
        Person person = new Person();
        person.setId_person(personId);
        existingStudent.setPerson(person);


        // Creamos el profesor
        Professor professor = new Professor();
        //Professor professor2 = new Professor();
        existingStudent.setProfessor(professor);

        StudentOutputSimpleDto studentOutputSimpleDto = new StudentOutputSimpleDto();

        // Mocks
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        Mockito.when(studentMapper.toStudentOutputSimpleDto(existingStudent)).thenReturn(studentOutputSimpleDto);
        Mockito.when(studentRepository.save(existingStudent)).thenReturn(existingStudent);

        // Act
        StudentOutputDto result = studentService.updateStudent(studentId, studentInputDto);

        // Assert
        assertEquals(studentOutputSimpleDto, result);
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(studentMapper).updateStudentEntityFromDto(studentInputDto, existingStudent);
        Mockito.verify(studentRepository).save(existingStudent);
    }

    @Test
    void updateStudent_ValidInput_ReturnsUpdatedStudentDto5(){
        // Arrange
        Integer studentId = 1;
        Integer personId = 2;
        Integer personId2 = 3;
        //Integer professorId = 4;

        StudentInputDto studentInputDto = new StudentInputDto();
        studentInputDto.setId_person(personId);

        // Creamos el estudiante existente con una persona asociada
        Student existingStudent = new Student();
        Person person = new Person();
        Person person2 = new Person();
        person.setId_person(personId);
        person2.setId_person(personId2);
        existingStudent.setPerson(person2);


        StudentOutputSimpleDto studentOutputSimpleDto = new StudentOutputSimpleDto();

        // Mocks
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        Mockito.when(personRepository.findById(personId)).thenReturn(Optional.of(person));
        //Mockito.when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        Mockito.when(studentMapper.toStudentOutputSimpleDto(existingStudent)).thenReturn(studentOutputSimpleDto);
        Mockito.when(studentRepository.save(existingStudent)).thenReturn(existingStudent);

        // Act
        StudentOutputDto result = studentService.updateStudent(studentId, studentInputDto);

        // Assert
        assertEquals(studentOutputSimpleDto, result);
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(studentMapper).updateStudentEntityFromDto(studentInputDto, existingStudent);
        Mockito.verify(studentRepository).save(existingStudent);
    }


    //deleteStudent TEST
    @Test
    void deleteStudent_ValidId_DeletesStudent() throws EntityNotFoundException {
        // Arrange
        Integer studentId = 1;
        Student student = new Student();
        student.setSubjects(new ArrayList<>());

        // Aseguramos que el campo `person` esté inicializado
        Person person = new Person();
        student.setPerson(person);

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        Mockito.when(personRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(person));

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(studentRepository).deleteById(studentId);
    }

    //estudiante no es encontrado:
    @Test
    void deleteStudent_StudentNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Integer studentId = 1;

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            studentService.deleteStudent(studentId);
        });

        // Assert
        assertEquals(STUDENT_ID_NOT_FOUND + studentId, exception.getMessage());
    }

    @Test
    void deleteStudent_ValidId_DeletesStudent2() throws EntityNotFoundException {
        // Arrange
        Integer studentId = 1;
        Student student = new Student();
        student.setSubjects(new ArrayList<>());

        Person person = new Person();
        student.setPerson(person);

        // Creamos un profesor con una lista de estudiantes
        Professor professor = new Professor();
        professor.setStudents(new ArrayList<>());
        student.setProfessor(professor);

        // Agregamos al estudiante al profesor
        professor.getStudents().add(student);

        // Agregamos un subject con una lista de estudiantes
        Subject subject = new Subject();
        subject.setStudents(new ArrayList<>());
        student.setSubjects(List.of(subject));
        subject.getStudents().add(student);

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        Mockito.when(personRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(person));
        Mockito.when(professorRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(professor));
        Mockito.when(subjectRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(subject));

        // Act
        studentService.deleteStudent(studentId);

        // Assert
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(studentRepository).deleteById(studentId);

        //verifica que una condición booleana sea falsa
        assertFalse(professor.getStudents().contains(student));
        assertFalse(subject.getStudents().contains(student));
    }

    //getSubjectsByStudentId TEST
    @Test
    void getSubjectsByStudentId_ValidStudentId_ReturnsSubjectsList() {
        // Arrange
        Integer studentId = 1;
        Student student = new Student();
        student.setId_student(studentId);

        Subject subject1 = new Subject();
        Subject subject2 = new Subject();
        List<Subject> subjects = List.of(subject1, subject2);
        student.setSubjects(subjects);

        SubjectOutputSimpleDto subjectOutputSimpleDto1 = new SubjectOutputSimpleDto();
        SubjectOutputSimpleDto subjectOutputSimpleDto2 = new SubjectOutputSimpleDto();

        // Mocks
        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        Mockito.when(subjectMapper.toAsignaturaOutputSimpleDto(subject1)).thenReturn(subjectOutputSimpleDto1);
        Mockito.when(subjectMapper.toAsignaturaOutputSimpleDto(subject2)).thenReturn(subjectOutputSimpleDto2);

        // Act
        List<SubjectOutputDto> result = studentService.getSubjectsByStudentId(studentId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(subjectOutputSimpleDto1));
        assertTrue(result.contains(subjectOutputSimpleDto2));
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(subjectMapper, Mockito.times(2)).toAsignaturaOutputSimpleDto(Mockito.any(Subject.class));
    }

    //assignSubjectsToStudent TEST
    @Test
    void assignSubjectsToStudent_ValidSubjects_AssignsSuccessfully() {
        // Arrange
        Integer studentId = 1;
        List<Integer> subjectIds = List.of(101, 102);

        Student student = new Student();
        student.setId_student(studentId);
        student.setSubjects(new ArrayList<>());

        Subject subject1 = new Subject();
        subject1.setId_subject(101);
        Subject subject2 = new Subject();
        subject2.setId_subject(102);
        List<Subject> subjectList = List.of(subject1, subject2);

        StudentOutputWithAsignaturasDto studentOutputWithAsignaturasDto = new StudentOutputWithAsignaturasDto();

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        Mockito.when(subjectRepository.findAllById(subjectIds)).thenReturn(subjectList);
        Mockito.when(studentMapper.toStudentOutputWithAsignaturasDto(student)).thenReturn(studentOutputWithAsignaturasDto);

        // Act
        StudentOutputWithAsignaturasDto result = studentService.assignSubjectsToStudent(studentId, subjectIds);

        // Assert
        assertEquals(studentOutputWithAsignaturasDto, result);
        assertTrue(student.getSubjects().containsAll(subjectList));
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(subjectRepository).findAllById(subjectIds);
        Mockito.verify(studentRepository).save(student);
        Mockito.verify(studentMapper).toStudentOutputWithAsignaturasDto(student);
    }


    @Test
    void assignSubjectsToStudent_AllSubjectsAlreadyAssigned_ThrowsUnprocessableEntityException() {
        // Arrange
        Integer studentId = 1;
        List<Integer> subjectIds = List.of(101, 102);

        Student student = new Student();
        student.setId_student(studentId);

        Subject subject1 = new Subject();
        subject1.setId_subject(101);
        Subject subject2 = new Subject();
        subject2.setId_subject(102);
        student.setSubjects(List.of(subject1, subject2)); // Ya tiene asignadas estas asignaturas

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        Mockito.when(subjectRepository.findAllById(subjectIds)).thenReturn(List.of(subject1, subject2));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> studentService.assignSubjectsToStudent(studentId, subjectIds));

        assertEquals(SUBJECT_ASSIGNED_OR_NOTEXIST, exception.getMessage());
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(subjectRepository).findAllById(subjectIds);
    }

    //unassignSubjectsFromStudent TEST
    @Test
    void unassignSubjectsFromStudent_ValidSubjects_UnassignsSuccessfully() {
        // Arrange
        Integer studentId = 1;
        List<Integer> subjectIds = List.of(101, 102);

        Student student = new Student();
        student.setId_student(studentId);

        Subject subject1 = new Subject();
        subject1.setId_subject(101);
        Subject subject2 = new Subject();
        subject2.setId_subject(102);
        student.setSubjects(new ArrayList<>(List.of(subject1, subject2)));

        StudentOutputWithAsignaturasDto studentOutputWithAsignaturasDto = new StudentOutputWithAsignaturasDto();

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        Mockito.when(subjectRepository.findAllById(subjectIds)).thenReturn(List.of(subject1, subject2));
        Mockito.when(studentMapper.toStudentOutputWithAsignaturasDto(student)).thenReturn(studentOutputWithAsignaturasDto);

        // Act
        StudentOutputWithAsignaturasDto result = studentService.unassignSubjectsFromStudent(studentId, subjectIds);

        // Assert
        assertEquals(studentOutputWithAsignaturasDto, result);
        assertFalse(student.getSubjects().contains(subject1));
        assertFalse(student.getSubjects().contains(subject2));
        Mockito.verify(studentRepository).findById(studentId);
        Mockito.verify(subjectRepository).findAllById(subjectIds);
        Mockito.verify(studentRepository).save(student);
        Mockito.verify(studentMapper).toStudentOutputWithAsignaturasDto(student);
    }

    //checkoutputType TEST
    @Test
    void checkoutputType_NullInput_ReturnsDefaultType() {
        // Arrange
        StudentServiceImpl studentService = new StudentServiceImpl();

        // Act
        String result = studentService.checkoutputType(OUTPUT_TYPE_NULL);

        // Assert
        assertEquals(OUTPUT_TYPE_SIMPLE, result);
    }

    @Test
    void checkoutputType_EmptyInput_ThrowsWrongOutputTypeException() {
        // Arrange
        StudentServiceImpl studentService = new StudentServiceImpl();

        // Act & Assert
        assertThrows(WrongOutputTypeException.class, () -> studentService.checkoutputType(OUTPUT_TYPE_EMPTY));
    }

    @Test
    void checkoutputType_ValidInput_ReturnsSameType() {
        // Arrange
        StudentServiceImpl studentService = new StudentServiceImpl();

        // Act
        String result = studentService.checkoutputType(OUTPUT_TYPE_FULL);

        // Assert
        assertEquals(OUTPUT_TYPE_FULL, result);
    }

}
