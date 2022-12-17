package pl.toponavigator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pl.toponavigator.repository.UsersRepository;

@RestController
@Slf4j
@CrossOrigin(maxAge = 36000)
@RequestMapping(path = "test")
public class TestController {
    UsersRepository usersRepository;

    @GetMapping
    public String test(@RequestHeader("Authorization") final String bearerToken) {
        log.error(bearerToken);
//        log.error(SecurityContextHolder.getContext().toString());
//        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.error(userDetails.getUsername());
//        log.error(userDetails.getAuthorities().toString());
        return "test";
    }

    @GetMapping("user")
    public String test1() {
//        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.error(userDetails.getUsername());
//        log.error(userDetails.getAuthorities().toString());
        return "user";
    }

    @GetMapping("admin")
    public String test2() {
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.error(userDetails.getUsername());
        log.error(userDetails.getAuthorities().toString());
        return "admin";
    }

    @Autowired
    public void setUsersRepository(final UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
}
