package project.hexa.person.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.professor.domain.Professor;
import project.hexa.student.domain.Student;

import java.util.Date;

@Entity
@Table(name="person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_person")
    private Integer id_person;

    @NotNull
    @Size(min = 6, max = 10)
    @Column(name = "username", nullable = false, length = 10)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname")
    private String surname;

    @Email
    @NotNull
    @Column(name = "company_email", nullable = false)
    private String company_email;

    @Email
    @NotNull
    @Column(name = "personal_email", nullable = false)
    private String personal_email;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

    @NotNull
    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date created_date;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "termination_date")
    @Temporal(TemporalType.DATE)
    private Date termination_date;

    // Student and Teacher Relations
    @OneToOne(mappedBy = "person")
    private Student student;

    @OneToOne(mappedBy = "person")
    private Professor professor;
}
