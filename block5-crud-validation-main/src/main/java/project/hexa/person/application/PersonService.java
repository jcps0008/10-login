package project.hexa.person.application;

import project.hexa.person.infrastructure.controller.dto.PersonInputDto;
import project.hexa.person.infrastructure.controller.dto.PersonOutputDto;

import java.util.Date;
import java.util.List;

public interface PersonService {
    PersonOutputDto addPerson(PersonInputDto personInputDTO);

    PersonOutputDto getPersonById(Integer id, String outputType);

    List<PersonOutputDto> getPersonByUsername(String username, String outputType);

    List<PersonOutputDto> getAllPerson(String outputType);

    PersonOutputDto updatePerson(Integer id, PersonInputDto personInputDTO);

    void deletePerson(Integer id);

    List<PersonOutputDto> getPersonsByCriteria(String username, String name, String surname, Date fechaSuperior, Date fechaInferior, String orderBy, int page, int size);}
