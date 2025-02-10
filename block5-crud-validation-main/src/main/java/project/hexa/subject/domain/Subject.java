package project.hexa.subject.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.hexa.student.domain.Student;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "subject")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subject")
    private Integer id_subject;

    @Column(name = "subject")
    private String subject;

    @Column(name = "comments")
    private String comments;

    @NotNull
    @Column(name = "initial_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date initial_date;

    @NotNull
    @Column(name = "finish_date")
    @Temporal(TemporalType.DATE)
    private Date finish_date;

    //Relacion con estudiantes
    @ManyToMany(mappedBy = "subjects")
    private List<Student> students;

}
