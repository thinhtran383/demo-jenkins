package online.thinhtran.jenkinsdemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ResponseEntity<?> sayHello() {
        return ResponseEntity.ok("Hello Jenkins V1");
    }
}
