package project.hexa.subject.application;

import project.hexa.subject.infrastructure.controller.dto.SubjectInputDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputDto;

import java.util.List;

public interface SubjectService {
    SubjectOutputDto addSubject(SubjectInputDto subjectInputDto);

    SubjectOutputDto getSubjectById(Integer id, String outputType);

    List<SubjectOutputDto> getAllSubjects(String outputType);

    SubjectOutputDto updateSubject(Integer id, SubjectInputDto subjectInputDto);

    void deleteSubject(Integer id);
}
