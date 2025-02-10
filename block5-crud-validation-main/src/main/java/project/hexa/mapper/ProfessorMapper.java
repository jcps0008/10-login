package project.hexa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import project.hexa.professor.domain.Professor;
import project.hexa.professor.infrastructure.controller.dto.ProfessorInputDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputFullDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputSimpleDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputWithStudentsDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ProfessorMapper {
    @Named("ProfessorOutputSimpleDto")
    ProfessorOutputSimpleDto toProfesorOutputSimpleDto(Professor professor);

    @Named("ProfessorOutputFullDto")
    ProfessorOutputFullDto toProfesorOutputFullDto(Professor professor);

    @Named("ProfessorOutputWithStudentsDto")
    ProfessorOutputWithStudentsDto toProfesorOutputWithStudentsDto(Professor professor);

    Professor toProfesorEntity(ProfessorInputDto professor);

    void updateProfesorEntityFromDto(ProfessorInputDto professorInputDto, @MappingTarget Professor professor);
}