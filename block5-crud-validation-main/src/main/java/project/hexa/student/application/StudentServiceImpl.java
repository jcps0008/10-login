package project.hexa.student.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.hexa.components.StringConstants;
import project.hexa.exception.EntityNotFoundException;
import project.hexa.exception.UnprocessableEntityException;
import project.hexa.exception.WrongOutputTypeException;
import project.hexa.mapper.StudentMapper;
import project.hexa.mapper.SubjectMapper;
import project.hexa.person.domain.Person;
import project.hexa.person.infrastructure.repository.PersonRepository;
import project.hexa.professor.domain.Professor;
import project.hexa.professor.infrastructure.repository.ProfessorRepository;
import project.hexa.student.domain.Student;
import project.hexa.student.infrastructure.controller.dto.StudentInputDto;
import project.hexa.student.infrastructure.controller.dto.StudentOutputDto;
import project.hexa.student.infrastructure.controller.dto.StudentOutputWithAsignaturasDto;
import project.hexa.student.infrastructure.repository.StudentRepository;
import project.hexa.subject.domain.Subject;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputDto;
import project.hexa.subject.infrastructure.repository.SubjectRepository;

import java.util.List;

import static project.hexa.components.StringConstants.*;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private SubjectMapper subjectMapper;


    @Override
    public StudentOutputDto addStudent(StudentInputDto studentInputDTO)
            throws UnprocessableEntityException {

        Student student = studentMapper.toStudentEntity(studentInputDTO);

        student.setPerson(checkPersonaIsProfessorOrStudent(
                findPersonaByIdOrThrow(
                    studentInputDTO.getId_person()))
        );
        if(studentInputDTO.getId_professor()!=null) {
            student.setProfessor(findProfessorByIdOrThrow(studentInputDTO.getId_professor()));
        }

        return studentMapper.toStudentOutputSimpleDto(
            studentRepository.save(student)
        );
    }

    @Override
    public StudentOutputDto getStudentById(Integer id, String outputType) throws EntityNotFoundException {
        outputType=checkoutputType(outputType);
        if (OUTPUT_TYPE_SIMPLE.equals(outputType)) {
            return studentMapper.toStudentOutputSimpleDto(findByIdOrThrow(id));
        } else if (OUTPUT_TYPE_FULL.equals(outputType)) {
            return studentMapper.toStudentOutputFullDto(findByIdOrThrow(id));
        } else {
            throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
        }
    }

    @Override
    public List<StudentOutputDto> getAllStudents(String outputType) throws EntityNotFoundException {
        outputType=checkoutputType(outputType);
        List<Student> students = studentRepository.findAll();
        checkEmptyStudents(students);

        if (OUTPUT_TYPE_SIMPLE.equals(outputType)) {
            return students.stream()
                    .map(student -> (StudentOutputDto) studentMapper.toStudentOutputSimpleDto(student))
                    .toList();
        } else if (OUTPUT_TYPE_FULL.equals(outputType)) {
            return students.stream()
                    .map(student -> (StudentOutputDto) studentMapper.toStudentOutputFullDto(student))
                    .toList();
        } else {
            throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
        }
    }

    @Override
    public StudentOutputDto updateStudent(Integer id, StudentInputDto studentInputDTO) throws EntityNotFoundException, UnprocessableEntityException {

        Student updateStudent = findByIdOrThrow(id);

        if(!updateStudent.getPerson().getId_person().equals(studentInputDTO.getId_person())){
            updateStudent.setPerson(checkPersonaIsProfessorOrStudent(
                    findPersonaByIdOrThrow(
                            studentInputDTO.getId_person()))
            );
        }

        if (studentInputDTO.getId_professor() == null) {
            updateStudent.setProfessor(null);
        } else if(updateStudent.getProfessor() == null || !updateStudent.getProfessor().getId_professor().equals(studentInputDTO.getId_professor())) {
            updateStudent.setProfessor(findProfessorByIdOrThrow(studentInputDTO.getId_professor()));
        }
        studentMapper.updateStudentEntityFromDto(studentInputDTO, updateStudent);

        return studentMapper.toStudentOutputSimpleDto(studentRepository.save(updateStudent));
    }

    @Override
    public void deleteStudent(Integer id) throws EntityNotFoundException {
        Student student = findByIdOrThrow(id);

        /*
         * The teacher deletes the student from his list
         * -> the teacher has a list of students
         */
        if(student.getProfessor()!=null){
            student.getProfessor().getStudents().remove(student);
        }

        /*
         * For subjects, set the student to null
         * -> the subject has a list of students
         * process to delete a student's subjects
         */
        if(!student.getSubjects().isEmpty()){
            student.getSubjects().forEach(subject -> subject.getStudents().remove(student)
            );
        }

        /*
         * For people set the student to null -> A person has a Student
         */
        student.getPerson().setStudent(null);

        studentRepository.deleteById(id);
    }

    @Override
    public List<SubjectOutputDto> getSubjectsByStudentId(Integer studentId) throws EntityNotFoundException {
        Student student = findByIdOrThrow(studentId);

        return student.getSubjects().stream()
                .map(subject -> (SubjectOutputDto) subjectMapper.toAsignaturaOutputSimpleDto(subject))
                .toList();
    }

    @Override
    public StudentOutputWithAsignaturasDto assignSubjectsToStudent(Integer studentId, List<Integer> subjectIds) {
        Student student = findByIdOrThrow(studentId);

        /*
         * Search for subjects by their IDs
         */
        List<Subject> subjectsToAssign = subjectRepository.findAllById(subjectIds);
        checkEmptySubjects(subjectsToAssign, SUBJECT_NOT_VALID);

        /*
         * Filter the subjects that are already assigned
         */
        List<Subject> newSubjects = subjectsToAssign.stream()
                .filter(subject -> !student.getSubjects().contains(subject))
                .toList();
        checkEmptySubjects(newSubjects, SUBJECT_ASSIGNED_OR_NOTEXIST);

        student.getSubjects().addAll(newSubjects);


        studentRepository.save(student);

        return studentMapper.toStudentOutputWithAsignaturasDto(student);
    }

    @Override
    public StudentOutputWithAsignaturasDto unassignSubjectsFromStudent(Integer studentId, List<Integer> subjectIds) {
        /*
         * Find the student or throw an exception if it doesn't exist
         */
        Student student = findByIdOrThrow(studentId);

        /*
         * Search for subjects by their IDs
         */

        List<Subject> subjectsToUnassign = subjectRepository.findAllById(subjectIds);
        checkEmptySubjects(subjectsToUnassign, SUBJECT_NOT_VALID);

        /*
         * Filter the subjects that are assigned to the student
         */
        List<Subject> removableSubjects = subjectsToUnassign.stream()
                .filter(student.getSubjects()::contains)
                .toList();
        checkEmptySubjects(removableSubjects, SUBJECT_UNASSIGNED);

        /*
         * Delete the student's subjects
         */
        student.getSubjects().removeAll(removableSubjects);


        studentRepository.save(student);

        /*
        * Returns the DTO with updated subjects
        */
        return studentMapper.toStudentOutputWithAsignaturasDto(student);
    }


    //Auxiliary methods
    private Student findByIdOrThrow(Integer id) throws EntityNotFoundException {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StringConstants.STUDENT_ID_NOT_FOUND + id));
    }

    private void checkEmptyStudents(List<Student> students) throws EntityNotFoundException {
        if (students.isEmpty()) {
            throw new EntityNotFoundException(StringConstants.STUDENT_EMPTY);
        }
    }

    private void checkEmptySubjects(List<Subject> subjects, String message) throws EntityNotFoundException {
        if (subjects.isEmpty()) {
            throw new EntityNotFoundException(message);
        }
    }


    private Person findPersonaByIdOrThrow(Integer id) throws EntityNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StringConstants.PERSON_ID_NOT_FOUND + id));
    }

    private Professor findProfessorByIdOrThrow(Integer id) throws EntityNotFoundException {
        return professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StringConstants.PROFESSOR_ID_NOT_FOUND + id));
    }

    private Person checkPersonaIsProfessorOrStudent(Person person){
        if(person.getStudent()!=null){
            throw new UnprocessableEntityException(PERSON_IS_STUDENT);
        }
        if(person.getProfessor()!=null){
            throw new UnprocessableEntityException(PERSON_IS_PROFESOR);
        }
        return person;
    }
    protected String checkoutputType(String outputType){
        if(outputType==null){
            return StringConstants.OUTPUT_TYPE_SIMPLE;
        }
        if(outputType.isEmpty()){
            throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
        }
        return outputType;
    }
}
