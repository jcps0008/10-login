package project.hexa.subject.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.hexa.subject.domain.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

}

