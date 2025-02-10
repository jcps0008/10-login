package project.hexa.student.infrastructure.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.hexa.student.application.StudentService;
import project.hexa.student.infrastructure.controller.dto.StudentInputDto;
import project.hexa.student.infrastructure.controller.dto.StudentOutputDto;

import java.util.List;

import static project.hexa.components.StringConstants.DELETE_SUCCESSFULLY;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentOutputDto> addStudent(@Valid @RequestBody StudentInputDto studentInputDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudent(studentInputDTO));
    }

    @GetMapping("/{id_student}")
    public ResponseEntity<StudentOutputDto> getStudentById(@PathVariable Integer id_student,  @RequestParam(required=false) String outputType) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudentById(id_student, outputType));
    }

    @GetMapping
    public ResponseEntity<List<StudentOutputDto>> getAllStudents(@RequestParam(required=false) String outputType) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudents(outputType));
    }

    @PutMapping("/{id_student}")
    public ResponseEntity<StudentOutputDto> updateStudent(@PathVariable Integer id_student, @Valid @RequestBody StudentInputDto studentInputDto) {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(id_student, studentInputDto));
    }

    @DeleteMapping("/{id_student}")
    public ResponseEntity<String> deleteStudentById(@PathVariable Integer id_student) {
        studentService.deleteStudent(id_student);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_SUCCESSFULLY + id_student);
    }
}


