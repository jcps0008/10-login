package project.hexa.person.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import project.hexa.person.domain.Person;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static project.hexa.components.StringConstants.*;


@SpringBootTest
public class PersonRepositoryCustomImplTest {
    @InjectMocks
    private PersonRepositoryCustomImpl personRepositoryCustom;

    @Mock
    protected EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;



    //Sin filtros
    @Test
    void findByCriteriaWithPagination_noFilters_shouldReturnResults() {
        // Arrange
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Person> query = mock(CriteriaQuery.class);
        Root<Person> personRoot = mock(Root.class);
        TypedQuery<Person> typedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Person.class)).thenReturn(query);
        when(query.from(Person.class)).thenReturn(personRoot);
        when(entityManager.createQuery(query)).thenReturn(typedQuery);

        int offset = 0;
        int limit = 10;
        List<Person> expectedResult = List.of(new Person(), new Person());
        when(typedQuery.setFirstResult(offset)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(limit)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedResult);

        // Act
        List<Person> result = personRepositoryCustom.findByCriteriaWithPagination(
                null, null, null, null, null, null, offset, limit
        );

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(query).where(Mockito.any(Predicate[].class));
        verify(entityManager).createQuery(query);
    }

    //verificamos que los filtros para username, name y surname se apliquen correctamente.
    @Test
    void findByCriteriaWithPagination_withFilters_shouldAddPredicates() {
        // Arrange
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Person> query = mock(CriteriaQuery.class);
        Root<Person> personRoot = mock(Root.class);
        TypedQuery<Person> typedQuery = mock(TypedQuery.class);
        Predicate predicate1 = mock(Predicate.class);
        Predicate predicate2 = mock(Predicate.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Person.class)).thenReturn(query);
        when(query.from(Person.class)).thenReturn(personRoot);
        when(cb.like(Mockito.any(), Mockito.anyString()))
                .thenReturn(predicate1, predicate2);
        when(entityManager.createQuery(query)).thenReturn(typedQuery);


        int offset = 0;
        int limit = 10;
        List<Person> expectedResult = List.of(new Person());
        when(typedQuery.setFirstResult(offset)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(limit)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedResult);

        // Act
        List<Person> result = personRepositoryCustom.findByCriteriaWithPagination(
                USERNAME_VARIABLE_TEST, NAME_VARIABLE_TEST, null, null, null, null, offset, limit
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cb).like(personRoot.get(USERNAME_VARIABLE), "%john%");
        verify(cb).like(personRoot.get(NAME_VARIABLE), "%John%");
    }

    //verificamos que el ordenamiento se aplicaque correctamente con orderBy.
    @Test
    void findByCriteriaWithPagination_withOrderBy_shouldApplySorting() {
        // Arrange
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Person> query = mock(CriteriaQuery.class);
        Root<Person> personRoot = mock(Root.class);
        TypedQuery<Person> typedQuery = mock(TypedQuery.class);
        Order order = mock(Order.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Person.class)).thenReturn(query);
        when(query.from(Person.class)).thenReturn(personRoot);
        when(cb.asc(personRoot.get("username"))).thenReturn(order);
        when(entityManager.createQuery(query)).thenReturn(typedQuery);

        String orderBy = "username";
        int offset = 0;
        int limit = 10;
        List<Person> expectedResult = List.of(new Person());
        when(typedQuery.setFirstResult(offset)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(limit)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedResult);

        // Act
        List<Person> result = personRepositoryCustom.findByCriteriaWithPagination(
                null, null, null, null, null, orderBy, offset, limit
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(query).orderBy(order);
    }

    //verificamos que offset y limit se configuren correctamente
    @Test
    void findByCriteriaWithPagination_withPagination_shouldSetOffsetAndLimit() {
        // Arrange
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Person> query = mock(CriteriaQuery.class);
        Root<Person> personRoot = mock(Root.class);
        TypedQuery<Person> typedQuery = mock(TypedQuery.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Person.class)).thenReturn(query);
        when(query.from(Person.class)).thenReturn(personRoot);
        when(entityManager.createQuery(query)).thenReturn(typedQuery);

        int offset = 5;
        int limit = 15;
        List<Person> expectedResult = List.of(new Person(), new Person(), new Person());
        when(typedQuery.setFirstResult(offset)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(limit)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedResult);

        // Act
        List<Person> result = personRepositoryCustom.findByCriteriaWithPagination(
                null, null, null, null, null, null, offset, limit
        );

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(typedQuery).setFirstResult(offset);
        verify(typedQuery).setMaxResults(limit);
    }

    //verificamos si surname no es nulo ni vacío
    @Test
    void findByCriteriaWithPagination_withSurnameFilter_shouldAddSurnamePredicate() {
        // Arrange
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Person> query = mock(CriteriaQuery.class);
        Root<Person> personRoot = mock(Root.class);
        TypedQuery<Person> typedQuery = mock(TypedQuery.class);
        Predicate surnamePredicate = mock(Predicate.class);

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Person.class)).thenReturn(query);
        when(query.from(Person.class)).thenReturn(personRoot);
        when(cb.like(personRoot.get(SURNAME_VARIABLE), "%Doe%")).thenReturn(surnamePredicate);
        when(entityManager.createQuery(query)).thenReturn(typedQuery);

        int offset = 0;
        int limit = 10;

        List<Person> expectedResult = List.of(new Person());
        when(typedQuery.setFirstResult(offset)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(limit)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedResult);

        // Act
        List<Person> result = personRepositoryCustom.findByCriteriaWithPagination(
                null, null, SURNAME_VARIABLE_TEST, null, null, null, offset, limit
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(cb).like(personRoot.get(SURNAME_VARIABLE), "%Doe%");
    }



}
