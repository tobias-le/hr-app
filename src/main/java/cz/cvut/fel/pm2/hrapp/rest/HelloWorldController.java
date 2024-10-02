package cz.cvut.fel.pm2.hrapp.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping
    public String test() {
        return "Hello world!";
    }
}
