package project.hexa.person.infrastructure.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project.hexa.person.application.PersonService;
import project.hexa.person.infrastructure.controller.dto.PersonInputDto;
import project.hexa.person.infrastructure.controller.dto.PersonOutputDto;
import project.hexa.person.infrastructure.feign.FeignProfessor;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputDto;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputSimpleDto;

import java.util.Date;
import java.util.List;

import static project.hexa.components.StringConstants.*;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    /*
    * Part 4
    * */
    @Autowired
    private RestTemplate restTemplate;

    /*
     * Part 4
     * */
    @Autowired
    private FeignProfessor feignProfessor;


    @PostMapping
    public ResponseEntity<PersonOutputDto> addPerson(@Valid @RequestBody PersonInputDto personInputDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.addPerson(personInputDTO));
    }

    @GetMapping("/{id_person}")
    public ResponseEntity<PersonOutputDto> getPersonById(@PathVariable Integer id_person, @RequestParam(required=false) String outputType) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.getPersonById(id_person, outputType));
    }


    @GetMapping("/username/{username}")
    public ResponseEntity<List<PersonOutputDto>> getPersonByUsername(@PathVariable String username, @RequestParam(required=false) String outputType) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.getPersonByUsername(username, outputType));
    }

    @GetMapping
    public ResponseEntity<List<PersonOutputDto>> getAllPerson(@RequestParam(required=false) String outputType) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.getAllPerson(outputType));
    }


    @PutMapping("/{id_person}")
    public ResponseEntity<PersonOutputDto> updatePerson(@PathVariable Integer id_person, @Valid @RequestBody PersonInputDto personInputDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(personService.updatePerson(id_person, personInputDTO));
    }

    @DeleteMapping("/{id_person}")
    public ResponseEntity<String> deletePersonById(@PathVariable Integer id_person) {
        personService.deletePerson(id_person);
        return ResponseEntity.status(HttpStatus.OK).body(DELETE_SUCCESSFULLY + id_person);
    }

    /*
    Part 4 - New endpoint in Person to get the professor by calling port 8081
     */
    @GetMapping("/professor/{id_professor}")
    public ResponseEntity<ProfessorOutputDto> getProfesor(@PathVariable int id_professor) {
        ResponseEntity<ProfessorOutputSimpleDto> response = restTemplate.getForEntity(URL_GET_PROFESSOR + id_professor, ProfessorOutputSimpleDto.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    /*
    Using Feign
    Part 4 - New endpoint in Person to get the professor by calling port 8081
     */
    @GetMapping("/professor/feign/{id_professor}")
    public ResponseEntity<ProfessorOutputDto> getProfessorUsingFeign(@PathVariable Integer id_professor) {
        ProfessorOutputDto professorOutputDto = feignProfessor.getProfessorById(id_professor);
        return ResponseEntity.ok(professorOutputDto);
    }


    /*
    Using CriteriaBuilder
    EXERCISE 9 - New endpoint getPersonsByCriteria using CriteriaBuilder
     */
    @GetMapping("/search")
    public ResponseEntity<List<PersonOutputDto>> getPersonsByCriteria(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = PATTERN_DATE) Date dateTop,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = PATTERN_DATE) Date dateBelow,
            @RequestParam(required = false) String orderBy,
            @RequestParam int page, // Page number (required)
            @RequestParam(required = false, defaultValue = SIZE_DEFAULT_VALUE) int size // Page size (optional, default 10)
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(personService.getPersonsByCriteria(username, name, surname, dateTop, dateBelow, orderBy, page, size));
    }

}


