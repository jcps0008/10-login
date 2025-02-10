package project.hexa.person.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import project.hexa.professor.infrastructure.controller.dto.ProfessorOutputSimpleDto;


@FeignClient(name = "professorFeignClient", url = "http://localhost:8081/professor")
public interface FeignProfessor {

    @GetMapping("/{id}")
    ProfessorOutputSimpleDto getProfessorById(@PathVariable("id") Integer id);
}
