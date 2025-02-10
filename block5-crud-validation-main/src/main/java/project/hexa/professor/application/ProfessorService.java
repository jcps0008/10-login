package project.hexa.professor.application;

import project.hexa.professor.infrastructure.controller.dto.ProfessorInputDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputDto;

import java.util.List;

public interface ProfessorService {
    ProfessorOutputDto addProfessor(ProfessorInputDto professorInputDto);

    ProfessorOutputDto getProfessorById(Integer id, String outputType);

    List<ProfessorOutputDto> getAllProfessors(String outputType);

    ProfessorOutputDto updateProfessor(Integer id, ProfessorInputDto professorInputDto);

    void deleteProfessor(Integer id);
}
