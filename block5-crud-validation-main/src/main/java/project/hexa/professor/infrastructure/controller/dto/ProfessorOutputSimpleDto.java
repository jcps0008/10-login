package project.hexa.professor.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.enums.Branch;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorOutputSimpleDto implements ProfessorOutputDto {

    private Integer id_professor;
    private String comments;
    private Branch branch;
}
