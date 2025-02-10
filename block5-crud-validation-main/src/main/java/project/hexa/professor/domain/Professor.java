package project.hexa.professor.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.enums.Branch;
import project.hexa.person.domain.Person;
import project.hexa.student.domain.Student;

import java.util.List;

@Entity
@Table(name = "professor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_professor")
    private Integer id_professor;

    @Column(name = "comments")
    private String comments;

    @NotNull
    @Column(name = "branch", nullable = false)
    private Branch branch;

    //Relación con persona
    @NotNull
    @OneToOne
    @JoinColumn(name="id_person")
    private Person person;

    //Relación con estudiantes
    @OneToMany(mappedBy = "professor")
    private List<Student> students;
}
