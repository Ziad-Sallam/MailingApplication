package com.example.demo.Controller;

import com.example.demo.Model.Mail;
import com.example.demo.Model.User;
import com.example.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

    @GetMapping(value = "/signin", params = {"email", "password"})
    public ResponseEntity<User> signIn(@Param("email") String email, @Param("password") String password) {
        User user = mailService.getUser(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/send")
    public ResponseEntity<Mail> send(@RequestBody  Mail mail) {
        Mail m =mailService.createEmail(mail.getSender(), new ArrayList<>(mail.getReceivers()) ,mail.getSubject(),mail.getBody(), mail.getPriority());
        return ResponseEntity.ok(m);

    }


}
