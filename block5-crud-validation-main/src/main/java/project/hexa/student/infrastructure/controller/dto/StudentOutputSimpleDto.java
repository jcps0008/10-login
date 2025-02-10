package project.hexa.student.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.enums.Branch;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentOutputSimpleDto implements StudentOutputDto{

    private Integer id_student;
    private Integer num_hours_week;
    private String comments;
    private Branch branch;
}
