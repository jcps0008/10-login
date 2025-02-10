package project.hexa.professor.infrastructure.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.enums.Branch;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorInputDto {

    @NotNull(message = "{error.professor.validation.persona.not.null}")
    private Integer id_person;

    private String comments;

    @NotNull(message = "{error.professor.validation.branch.not.null}")
    private Branch branch;
}
