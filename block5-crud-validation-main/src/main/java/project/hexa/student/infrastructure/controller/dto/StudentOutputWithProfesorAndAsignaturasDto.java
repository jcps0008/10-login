package project.hexa.student.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.enums.Branch;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputSimpleDto;
import project.hexa.subject.infrastructure.controller.dto.SubjectOutputSimpleDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentOutputWithProfesorAndAsignaturasDto implements StudentOutputDto{

    private Integer id_student;
    private Integer num_hours_week;
    private String comments;
    private Branch branch;

    private List<SubjectOutputSimpleDto> subjects;
    private ProfessorOutputSimpleDto professor;
}
