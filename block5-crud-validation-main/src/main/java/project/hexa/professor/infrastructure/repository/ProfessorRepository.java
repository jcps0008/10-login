package project.hexa.professor.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.hexa.professor.domain.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
}
