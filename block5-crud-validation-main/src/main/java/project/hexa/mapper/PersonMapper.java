package project.hexa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import project.hexa.person.domain.Person;
import project.hexa.person.infrastructure.controller.dto.PersonInputDto;
import project.hexa.person.infrastructure.controller.dto.PersonOutputFullDto;
import project.hexa.person.infrastructure.controller.dto.PersonOutputSimpleDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PersonMapper {
    @Named("PersonOutputSimpleDto")
    PersonOutputSimpleDto toPersonaOutputSimpleDto(Person person);

    @Named("PersonOutputFullDto")
    PersonOutputFullDto toPersonaOutputFullDto(Person person);

    Person toPersonaEntity(PersonInputDto personInputDto);

    void updatePersonaEntityFromDto(PersonInputDto personInputDto, @MappingTarget Person person);
}
