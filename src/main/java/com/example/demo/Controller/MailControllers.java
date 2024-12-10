package com.example.demo.Controller;

import com.example.demo.Model.User;
import com.example.demo.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class MailControllers {
    private final MailService mailService;

    @Autowired
    public MailControllers(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        mailService.createUser(user.getEmail(), user.getPassword(), user.getName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{Mail}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = mailService.getUser(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
