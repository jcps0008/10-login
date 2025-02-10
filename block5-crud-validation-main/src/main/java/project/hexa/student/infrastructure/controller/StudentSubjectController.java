package project.hexa.student.infrastructure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.hexa.student.application.StudentService;
import project.hexa.student.infrastructure.controller.dto.StudentOutputWithAsignaturasDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputDto;

import java.util.List;

@RestController
@RequestMapping("/student-subjects")
public class StudentSubjectController {
    @Autowired
    private final StudentService studentService;

    public StudentSubjectController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id_student}")
    public ResponseEntity<List<SubjectOutputDto>> getSubjectsByStudentId(@PathVariable Integer id_student) {
        return ResponseEntity.ok(studentService.getSubjectsByStudentId(id_student));
    }

    @PostMapping("/{id_student}/assign")
    public ResponseEntity<StudentOutputWithAsignaturasDto> assignSubjectsToStudent(
            @PathVariable Integer id_student, @RequestBody List<Integer> subjectIds) {
        return ResponseEntity.ok(studentService.assignSubjectsToStudent(id_student, subjectIds));
    }

    @PostMapping("/{id_student}/unassign")
    public ResponseEntity<StudentOutputWithAsignaturasDto> unassignSubjectsFromStudent(
            @PathVariable Integer id_student, @RequestBody List<Integer> subjectIds) {
        return ResponseEntity.ok(studentService.unassignSubjectsFromStudent(id_student, subjectIds));
    }
}
