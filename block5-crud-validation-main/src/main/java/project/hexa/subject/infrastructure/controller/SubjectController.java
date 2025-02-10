package project.hexa.subject.infrastructure.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.hexa.subject.application.SubjectService;
import project.hexa.subject.infrastructure.controller.dto.SubjectInputDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputDto;

import java.util.List;

import static project.hexa.components.StringConstants.DELETE_SUCCESSFULLY;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectOutputDto> addSubject(@Valid @RequestBody SubjectInputDto subjectInputDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.addSubject(subjectInputDto));
    }

    @GetMapping("/{id_subject}")
    public ResponseEntity<SubjectOutputDto> getSubjectById(@PathVariable Integer id_subject, @RequestParam(required=false) String outputType) {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.getSubjectById(id_subject, outputType));
    }


    @GetMapping
    public ResponseEntity<List<SubjectOutputDto>> getAllSubjects(@RequestParam(required=false) String outputType) {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.getAllSubjects(outputType));
    }


    @PutMapping("/{id_subject}")
    public ResponseEntity<SubjectOutputDto> updateSubject(@PathVariable Integer id_subject, @Valid @RequestBody SubjectInputDto subjectInputDto) {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.updateSubject(id_subject, subjectInputDto));
    }


    @DeleteMapping("/{id_subject}")
    public ResponseEntity<String> deleteSubjectById(@PathVariable Integer id_subject) {
        subjectService.deleteSubject(id_subject);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_SUCCESSFULLY + id_subject);
    }
}

