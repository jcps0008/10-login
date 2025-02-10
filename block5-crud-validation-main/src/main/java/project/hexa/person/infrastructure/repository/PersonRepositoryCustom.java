package project.hexa.person.infrastructure.repository;

import project.hexa.person.domain.Person;

import java.util.Date;
import java.util.List;

public interface PersonRepositoryCustom {
    List<Person> findByCriteriaWithPagination(String username, String name, String surname, Date fechaSuperior, Date fechaInferior, String orderBy, int offset, int limit);
}