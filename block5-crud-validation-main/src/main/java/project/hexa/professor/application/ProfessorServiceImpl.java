package project.hexa.professor.application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.hexa.components.StringConstants;
import project.hexa.exception.EntityNotFoundException;
import project.hexa.exception.UnprocessableEntityException;
import project.hexa.exception.WrongOutputTypeException;
import project.hexa.mapper.ProfessorMapper;
import project.hexa.person.domain.Person;
import project.hexa.person.infrastructure.repository.PersonRepository;
import project.hexa.professor.domain.Professor;
import project.hexa.professor.infrastructure.controller.dto.ProfessorInputDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputDto;
import project.hexa.professor.infrastructure.repository.ProfessorRepository;

import java.util.List;

import static project.hexa.components.StringConstants.*;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProfessorMapper professorMapper;


    @Override
    public ProfessorOutputDto addProfessor(ProfessorInputDto professorInputDto)
            throws UnprocessableEntityException {


        Professor professor = professorMapper.toProfesorEntity(professorInputDto);

        professor.setPerson(checkPersonaIsProfessorOrStudent(
                findPersonaByIdOrThrow(
                        professorInputDto.getId_person())));

        return professorMapper.toProfesorOutputSimpleDto(
                professorRepository.save(professor)
        );
    }


    @Override
    public ProfessorOutputDto getProfessorById(Integer id, String outputType) throws EntityNotFoundException {
        outputType=checkoutputType(outputType);
        if (OUTPUT_TYPE_SIMPLE.equals(outputType)) {
            return professorMapper.toProfesorOutputSimpleDto(findByIdOrThrow(id));
        } else if (OUTPUT_TYPE_FULL.equals(outputType)) {
            return professorMapper.toProfesorOutputFullDto(findByIdOrThrow(id));
        } else {
            throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
        }
    }


    @Override
    public List<ProfessorOutputDto> getAllProfessors(String outputType) throws EntityNotFoundException {
        List<Professor> professors = professorRepository.findAll();
        checkEmpty(professors);
        outputType=checkoutputType(outputType);

        if (OUTPUT_TYPE_SIMPLE.equals(outputType)) {
            return professors.stream()
                    .map(professor -> (ProfessorOutputDto) professorMapper.toProfesorOutputSimpleDto(professor))
                    .toList();
        } else if (OUTPUT_TYPE_FULL.equals(outputType)) {
            return professors.stream()
                    .map(professor -> (ProfessorOutputDto) professorMapper.toProfesorOutputFullDto(professor))
                    .toList();
        } else {
            throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
        }
    }


    @Override
    public ProfessorOutputDto updateProfessor(Integer id, ProfessorInputDto professorInputDto) throws EntityNotFoundException, UnprocessableEntityException {

        Professor updateProfessor = findByIdOrThrow(id);
        updateProfessor.setPerson(checkPersonaIsProfessorOrStudent(
                findPersonaByIdOrThrow(
                        professorInputDto.getId_person())));

        professorMapper.updateProfesorEntityFromDto(professorInputDto, updateProfessor);

        return professorMapper.toProfesorOutputSimpleDto(professorRepository.save(updateProfessor));
    }

    @Override
    public void deleteProfessor(Integer id) throws EntityNotFoundException {
        Professor professor = findByIdOrThrow(id);

        if(!professor.getStudents().isEmpty()){
            professor.getStudents().forEach(
                    student -> student.setProfessor(null)
            );
        }

        professor.getPerson().setProfessor(null);
        professorRepository.deleteById(id);
    }


    private Professor findByIdOrThrow(Integer id) throws EntityNotFoundException {
        return professorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StringConstants.PROFESSOR_ID_NOT_FOUND + id));
    }

    private Person findPersonaByIdOrThrow(Integer id) throws EntityNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StringConstants.PERSON_ID_NOT_FOUND + id));
    }

    private void checkEmpty(List<Professor> professors) throws EntityNotFoundException {
        if (professors.isEmpty()) {
            throw new EntityNotFoundException(StringConstants.PROFESSOR_EMPTY);
        }
    }

    private Person checkPersonaIsProfessorOrStudent(Person person){
        if(person.getStudent()!=null){
            throw new UnprocessableEntityException(PERSON_IS_STUDENT);
        }
        if(person.getProfessor()!=null){
            throw new UnprocessableEntityException(PERSON_IS_PROFESOR);
        }
        return person;
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
}
