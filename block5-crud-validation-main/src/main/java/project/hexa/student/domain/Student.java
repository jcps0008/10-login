package project.hexa.student.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.enums.Branch;
import project.hexa.person.domain.Person;
import project.hexa.professor.domain.Professor;
import project.hexa.subject.domain.Subject;

import java.util.List;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_student")
    private Integer id_student;

    @NotNull
    @OneToOne
    @JoinColumn(name="id_person")
    private Person person;

    @NotNull
    @Column(name = "num_hours_week", nullable = false)
    private Integer num_hours_week;

    @Column(name = "comments")
    private String comments;

    //Relationship with teacher
    @ManyToOne
    @JoinColumn(name = "id_professor")
    private Professor professor;

    @NotNull
    @Column(name = "branch", nullable = false)
    private Branch branch;

    //Relationship with subject
    @ManyToMany
    @JoinTable(
            name = "student_subject",
            joinColumns = @JoinColumn(name = "id_student"),
            inverseJoinColumns = @JoinColumn(name = "id_subject")
    )
    private List<Subject> subjects;
}
