package project.hexa.student.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.hexa.student.domain.Student;



@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {


}


