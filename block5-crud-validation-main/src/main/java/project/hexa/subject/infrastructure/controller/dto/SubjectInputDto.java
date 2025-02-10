package project.hexa.subject.infrastructure.controller.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectInputDto {

    private String subject;

    private String comments;

    @NotNull(message = "{error.subject.validation.initialDate.not.null}")
    @Temporal(TemporalType.DATE)
    private Date initial_date;

    @NotNull(message = "{error.subject.validation.finish_date.not.null}")
    @Temporal(TemporalType.DATE)
    private Date finish_date;
}
