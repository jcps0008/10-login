package project.hexa.professor.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.enums.Branch;
import project.hexa.student.infrastructure.controller.dto.StudentOutputWithAsignaturasDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorOutputWithStudentsDto implements ProfessorOutputDto {

    private Integer id_professor;
    private String comments;
    private Branch branch;
    private List<StudentOutputWithAsignaturasDto> student;
}
