package project.hexa.professor.infrastructure.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.hexa.professor.application.ProfessorService;
import project.hexa.professor.infrastructure.controller.dto.ProfessorInputDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputDto;

import java.util.List;

import static project.hexa.components.StringConstants.DELETE_SUCCESSFULLY;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping
    public ResponseEntity<ProfessorOutputDto> addProfessor(@Valid @RequestBody ProfessorInputDto professorInputDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.addProfessor(professorInputDto));
    }

    @GetMapping("/{id_professor}")
    public ResponseEntity<ProfessorOutputDto> getProfessorById(@PathVariable Integer id_professor, @RequestParam(required=false) String outputType) {
        return ResponseEntity.status(HttpStatus.OK).body(professorService.getProfessorById(id_professor, outputType));
    }

    @GetMapping
    public ResponseEntity<List<ProfessorOutputDto>> getAllProfessors(@RequestParam(required=false) String outputType) {
        return ResponseEntity.status(HttpStatus.OK).body(professorService.getAllProfessors(outputType));
    }

    @PutMapping("/{id_professor}")
    public ResponseEntity<ProfessorOutputDto> updateProfessor(@PathVariable Integer id_professor, @Valid @RequestBody ProfessorInputDto professorInputDto) {
        return ResponseEntity.status(HttpStatus.OK).body(professorService.updateProfessor(id_professor, professorInputDto));
    }

    @DeleteMapping("/{id_professor}")
    public ResponseEntity<String> deleteProfessorById(@PathVariable Integer id_professor) {
        professorService.deleteProfessor(id_professor);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_SUCCESSFULLY + id_professor);
    }
}
