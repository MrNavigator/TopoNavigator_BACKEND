package pl.toponavigator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.toponavigator.repository.UsersRepository;

@RestController
@Slf4j
@CrossOrigin(maxAge = 36000)
@RequestMapping(path = "test")
public class TestController {
    UsersRepository usersRepository;

    @GetMapping(path = "", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test() {
        return "XDDD";
    }

    @Autowired
    public void setUsersRepository(final UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
