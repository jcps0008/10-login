package project.hexa.subject.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.hexa.components.StringConstants;
import project.hexa.exception.EntityNotFoundException;
import project.hexa.exception.UnprocessableEntityException;
import project.hexa.exception.WrongOutputTypeException;
import project.hexa.mapper.SubjectMapper;
import project.hexa.subject.domain.Subject;
import project.hexa.subject.infrastructure.controller.dto.SubjectInputDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputDto;
import project.hexa.subject.infrastructure.repository.SubjectRepository;

import java.util.List;

import static project.hexa.components.StringConstants.*;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectMapper subjectMapper;


    @Override
    public SubjectOutputDto addSubject(SubjectInputDto subjectInputDto)
            throws UnprocessableEntityException {

        return subjectMapper.toAsignaturaOutputSimpleDto(
                subjectRepository.save(
                        subjectMapper.toAsignaturaEntity(subjectInputDto)
                )
        );
    }

    @Override
    public SubjectOutputDto getSubjectById(Integer id, String outputType) throws EntityNotFoundException {
        outputType=checkoutputType(outputType);
        if(outputType.equals(OUTPUT_TYPE_SIMPLE)){
            return subjectMapper.toAsignaturaOutputSimpleDto(findByIdOrThrow(id));
        } else if (outputType.equals(OUTPUT_TYPE_FULL)) {
            return subjectMapper.toAsignaturaOutputFullDto(findByIdOrThrow(id));
        }
        throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
    }

    @Override
    public List<SubjectOutputDto> getAllSubjects(String outputType) throws EntityNotFoundException {
        outputType=checkoutputType(outputType);
        List<Subject> subjects = subjectRepository.findAll();
        checkEmpty(subjects);

        if(outputType.equals(OUTPUT_TYPE_SIMPLE)) {
            return subjects.stream()
                    .map(subject -> (SubjectOutputDto) subjectMapper.toAsignaturaOutputSimpleDto(subject))
                    .toList();
        }else if (outputType.equals(OUTPUT_TYPE_FULL)) {
            return subjects.stream()
                    .map(subject -> (SubjectOutputDto) subjectMapper.toAsignaturaOutputFullDto(subject))
                    .toList();
        }
        throw new WrongOutputTypeException(WRONG_OUTPUT_TYPE);
    }

    @Override
    public SubjectOutputDto updateSubject(Integer id, SubjectInputDto subjectInputDto) throws EntityNotFoundException, UnprocessableEntityException {

        Subject updateSubject = findByIdOrThrow(id);
        subjectMapper.updateAsignaturaEntityFromDto(subjectInputDto, updateSubject);

        return subjectMapper.toAsignaturaOutputSimpleDto(subjectRepository.save(updateSubject));
    }

    @Override
    public void deleteSubject(Integer id) throws EntityNotFoundException {
        Subject subject = findByIdOrThrow(id);

        subject.getStudents().forEach(
                student -> student.getSubjects().remove(subject));

        subjectRepository.deleteById(id);
    }

    private Subject findByIdOrThrow(Integer id) throws EntityNotFoundException {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(StringConstants.SUBJECT_ID_NOT_FOUND + id));
    }

    private void checkEmpty(List<Subject> subjects) throws EntityNotFoundException {
        if (subjects.isEmpty()) {
            throw new EntityNotFoundException(StringConstants.SUBJECT_EMPTY);
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
}
