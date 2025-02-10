package project.hexa.person.infrastructure.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import project.hexa.person.domain.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static project.hexa.components.StringConstants.*;

@Repository
public class PersonRepositoryCustomImpl implements PersonRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Person> findByCriteriaWithPagination(String username, String name, String surname, Date fechaSuperior, Date fechaInferior, String orderBy, int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> query = cb.createQuery(Person.class);
        Root<Person> person = query.from(Person.class);

        // Lista de predicados (condiciones de filtro)
        List<Predicate> predicates = new ArrayList<>();

        // Agregar filtros dinámicos
        if (username != null && !username.isEmpty()) {
            predicates.add(cb.like(person.get(USERNAME_VARIABLE), "%" + username + "%"));
        }
        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(person.get(NAME_VARIABLE), "%" + name + "%"));
        }
        if (surname != null && !surname.isEmpty()) {
            predicates.add(cb.like(person.get(SURNAME_VARIABLE), "%" + surname + "%"));
        }
        if (fechaInferior != null) {
            predicates.add(cb.greaterThan(person.get(CREATEDDATE_VARIABLE), fechaInferior));
        }
        if (fechaSuperior != null) {
            predicates.add(cb.lessThan(person.get(CREATEDDATE_VARIABLE), fechaSuperior));
        }

        // Combinar predicados en la consulta
        query.where(predicates.toArray(new Predicate[0]));

        // Ordenar resultados (si se especifica un ordenamiento)
        if (USERNAME_VARIABLE.equalsIgnoreCase(orderBy)) {
            query.orderBy(cb.asc(person.get(USERNAME_VARIABLE)));
        } else if (NAME_VARIABLE.equalsIgnoreCase(orderBy)) {
            query.orderBy(cb.asc(person.get(NAME_VARIABLE)));
        }

        // Crear y configurar la consulta con paginación
        return entityManager.createQuery(query)
                .setFirstResult(offset) // Índice inicial
                .setMaxResults(limit)   // Tamaño de la página
                .getResultList();
    }
}