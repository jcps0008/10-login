package project.hexa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import project.hexa.subject.domain.Subject;
import project.hexa.subject.infrastructure.controller.dto.SubjectInputDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputFullDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputSimpleDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface SubjectMapper {
    @Named("toSubjectOutputSimpleDto")
    SubjectOutputSimpleDto toAsignaturaOutputSimpleDto(Subject subject);

    @Named("toSubjectOutputFullDto")
    SubjectOutputFullDto toAsignaturaOutputFullDto(Subject subject);

    Subject toAsignaturaEntity(SubjectInputDto subjectInputDto);

    void updateAsignaturaEntityFromDto(SubjectInputDto subjectInputDto, @MappingTarget Subject subject);
}
