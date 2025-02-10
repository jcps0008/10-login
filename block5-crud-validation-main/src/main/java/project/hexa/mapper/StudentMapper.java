package project.hexa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import project.hexa.student.domain.Student;
import project.hexa.student.infrastructure.controller.dto.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface StudentMapper {
    @Named("StudentOutputSimpleDto")
    StudentOutputSimpleDto toStudentOutputSimpleDto(Student student);

    @Named("StudentOutputFullDto")
    StudentOutputFullDto toStudentOutputFullDto(Student student);

    @Named("StudentOutputWithAsignaturasDto")
    StudentOutputWithAsignaturasDto toStudentOutputWithAsignaturasDto(Student student);

    @Named("StudentOutputWithProfesorAndAsignaturasDto")
    StudentOutputWithProfesorAndAsignaturasDto toStudentOutputWithProfesorAndAsignaturasDto(Student student);

    Student toStudentEntity(StudentInputDto studentInputDto);

    void updateStudentEntityFromDto(StudentInputDto studentInputDto, @MappingTarget Student student);
}