package project.hexa.person.infrastructure.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.hexa.person.domain.Person;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>, PersonRepositoryCustom
{
    List<Person> findAllByUsernameContaining(String username);
}
