package project.hexa.subject.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.student.infrastructure.controller.dto.StudentOutputSimpleDto;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectOutputFullDto implements SubjectOutputDto {

    private Integer id_subject;
    private String subject;
    private String comments;
    private Date initial_date;
    private Date finish_date;
    private List<StudentOutputSimpleDto> students;
}
