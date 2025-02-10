package project.hexa.student.application;

import project.hexa.student.infrastructure.controller.dto.StudentInputDto;
import project.hexa.student.infrastructure.controller.dto.StudentOutputDto;
import project.hexa.student.infrastructure.controller.dto.StudentOutputWithAsignaturasDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputDto;

import java.util.List;

public interface StudentService {
    StudentOutputDto addStudent(StudentInputDto studentInputDTO);

    StudentOutputDto getStudentById(Integer id, String outputType);

    List<StudentOutputDto> getAllStudents(String outputType);

    StudentOutputDto updateStudent(Integer id, StudentInputDto studentInputDTO);

    void deleteStudent(Integer id);

    List<SubjectOutputDto> getSubjectsByStudentId(Integer studentId);


    StudentOutputWithAsignaturasDto assignSubjectsToStudent(Integer studentId, List<Integer> subjectIds);

    StudentOutputWithAsignaturasDto unassignSubjectsFromStudent(Integer studentId, List<Integer> subjectIds);


}