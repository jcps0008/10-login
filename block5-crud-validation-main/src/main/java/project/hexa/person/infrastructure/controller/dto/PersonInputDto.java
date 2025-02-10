package project.hexa.person.infrastructure.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonInputDto {

    @NotBlank(message = "{error.person.validation.not.null.user}")
    @Size(min = 6, max = 10, message = "{error.person.validation.size.user}")
    private String username;

    @NotBlank(message = "{error.person.validation.not.null.password}")
    private String password;

    @NotBlank(message = "{error.person.validation.not.null.name}")
    private String name;

    private String surname;

    @Email(message = "{error.person.validation.email}")
    @NotBlank(message = "{error.person.validation.not.null.email}")
    private String company_email;

    @Email(message = "{error.person.validation.emailpersonal}")
    @NotBlank(message = "{error.person.validation.not.null.emailpersonal}")
    private String personal_email;

    @NotBlank(message = "{error.person.validation.not.null.city}")
    private String city;

    @NotNull(message = "{error.person.validation.not.null.state}")
    private boolean active;

    @NotNull(message = "{error.person.validation.not.null.created_date}")
    private Date created_date;

    private String image_url;

    private Date termination_date;
}