package project.hexa.professor.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.enums.Branch;
import project.hexa.person.infrastructure.controller.dto.PersonOutputSimpleDto;
import project.hexa.student.infrastructure.controller.dto.StudentOutputWithAsignaturasDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorOutputFullDto implements ProfessorOutputDto {

    private Integer id_professor;
    private String comments;
    private Branch branch;
    private PersonOutputSimpleDto person;
    private List<StudentOutputWithAsignaturasDto> students;
}
