package project.hexa.subject.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectOutputSimpleDto implements SubjectOutputDto {

    private Integer id_subject;
    private String subject;
    private String comments;
    private Date initial_date;
    private Date finish_date;
}
