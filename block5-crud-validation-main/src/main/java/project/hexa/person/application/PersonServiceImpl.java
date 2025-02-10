package project.hexa.person.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.hexa.components.StringConstants;
import project.hexa.exception.EntityNotFoundException;
import project.hexa.exception.UnprocessableEntityException;
import project.hexa.exception.WrongOutputTypeException;
import project.hexa.mapper.PersonMapper;
import project.hexa.person.domain.Person;
import project.hexa.person.infrastructure.controller.dto.PersonInputDto;
import project.hexa.person.infrastructure.controller.dto.PersonOutputDto;
import project.hexa.person.infrastructure.repository.PersonRepository;

import java.util.Date;
import java.util.List;

import static project.hexa.components.StringConstants.*;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;


    @Override
    public PersonOutputDto addPerson(PersonInputDto personInputDto)
            throws UnprocessableEntityException {

        return personMapper.toPersonaOutputSimpleDto(
                personRepository.save(
                        personMapper.toPersonaEntity(personInputDto)
                )
        );
    }

    @Override
    public PersonOutputDto getPersonById(Integer id, String outputType) throws EntityNotFoundException {
        outputType=checkoutputType(outputType);
        if(outputType.equals(OUTPUT_TYPE_SIMPLE)){
            return personMapper.toPersonaOutputSimpleDto(findByIdOrThrow(id));
        } else if (outputType.equals(OUTPUT_TYPE_FULL)) {
            return personMapper.toPersonaOutputFullDto(findByIdOrThrow(id));
        }
        throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
    }

    @Override
    public List<PersonOutputDto> getPersonByUsername(String username, String outputType) throws EntityNotFoundException {
        List<Person> people = personRepository.findAllByUsernameContaining(username);
        checkEmpty(people);

        outputType=checkoutputType(outputType);

        if(outputType.equals(OUTPUT_TYPE_SIMPLE)){
            return people.stream()
                    .map(person -> (PersonOutputDto) personMapper.toPersonaOutputSimpleDto(person))
                    .toList();
        } else if (outputType.equals(OUTPUT_TYPE_FULL)) {
            return people.stream()
                    .map(person -> (PersonOutputDto) personMapper.toPersonaOutputFullDto(person))
                    .toList();
        }
        throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
    }


    @Override
    public List<PersonOutputDto> getAllPerson(String outputType) throws EntityNotFoundException {
        List<Person> people = personRepository.findAll();
        checkEmpty(people);

        outputType=checkoutputType(outputType);

        if(outputType.equals(OUTPUT_TYPE_SIMPLE)){
            return people.stream()
                    .map(person -> (PersonOutputDto) personMapper.toPersonaOutputSimpleDto(person))
                    .toList();
        } else if (outputType.equals(OUTPUT_TYPE_FULL)) {
            return people.stream()
                    .map(person -> (PersonOutputDto) personMapper.toPersonaOutputFullDto(person))
                    .toList();
        }
        throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
    }



    @Override
    public PersonOutputDto updatePerson(Integer id, PersonInputDto personInputDTO) throws EntityNotFoundException, UnprocessableEntityException {

        Person updatePerson = findByIdOrThrow(id);
        personMapper.updatePersonaEntityFromDto(personInputDTO, updatePerson);

        return personMapper.toPersonaOutputSimpleDto(personRepository.save(updatePerson));
    }

    @Override
    public void deletePerson(Integer id) throws EntityNotFoundException, UnprocessableEntityException {
        Person person = findByIdOrThrow(id);

        if(person.getProfessor()!=null){
            throw new UnprocessableEntityException(PROFESSOR_EXIST);
        }
        if(person.getStudent()!=null){
            throw new UnprocessableEntityException(STUDENT_EXIST);
        }
        personRepository.deleteById(id);
    }

    private Person findByIdOrThrow(Integer id) throws EntityNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StringConstants.PERSON_ID_NOT_FOUND + id));
    }

    private void checkEmpty(List<Person> people) throws EntityNotFoundException {
        if (people.isEmpty()) {
            throw new EntityNotFoundException(StringConstants.PERSON_EMPTY);
        }
    }

    protected String checkoutputType(String outputType){
        if(outputType==null){
            return StringConstants.OUTPUT_TYPE_SIMPLE;
        }
        if(outputType.isEmpty()){
            throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
        }
        return outputType;
    }


    /*
    Using CriteriaBuilder
    EXERCISE 9 - New endpoint getPersonsByCriteria using CriteriaBuilder
     */
    @Override
    public List<PersonOutputDto> getPersonsByCriteria(String username, String name, String surname, Date dateTop, Date dateBelow, String orderBy, int page, int size) {
        // Validations
        validateDateRange(dateBelow, dateTop);
        validateOrderBy(orderBy);

        if (page < 0) {
            throw new RuntimeException(PATTERN_PAGE);
        }
        if (size <= 0) {
            throw new RuntimeException(CORRECT_SIZE);
        }

        // Calculate initial and limit index for pagination
        int offset = page * size; // Initial index
        int limit = size;         // Maximum result size

        // Repository paged query
        List<Person> persons = personRepository.findByCriteriaWithPagination(username, name, surname, dateTop, dateBelow, orderBy, offset, limit);

        //  Convert to DTO and return
        return persons.stream()
                .map(person -> (PersonOutputDto) personMapper.toPersonaOutputSimpleDto(person))
                .toList();
    }


    private void validateDateRange(Date dateBelow, Date dateTop) {
        if (dateBelow != null && dateTop != null && dateBelow.after(dateTop)) {
            throw new IllegalArgumentException(CORRECT_DATE);
        }
    }

    private void validateOrderBy(String orderBy) {
        if (orderBy != null && !orderBy.equalsIgnoreCase(USERNAME_VARIABLE) && !orderBy.equalsIgnoreCase(NAME_VARIABLE)) {
            throw new IllegalArgumentException(ERROR_MSG_ORDERBY);
        }
    }

}
