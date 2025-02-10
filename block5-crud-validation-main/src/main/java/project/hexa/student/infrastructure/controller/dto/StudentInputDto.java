package project.hexa.student.infrastructure.controller.dto;

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
public class StudentInputDto {

    @NotNull(message = "{error.student.validation.id_persona.not.null}")
    private Integer id_person;

    private Integer id_professor;

    @NotNull(message = "{error.student.validation.num_hours_week.not.null}")
    private Integer num_hours_week;

    private String comments;

    @NotNull(message = "{error.student.validation.branch.not.null}")
    private Branch branch;
}
