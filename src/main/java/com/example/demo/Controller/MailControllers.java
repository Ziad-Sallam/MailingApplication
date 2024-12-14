package com.example.demo.Controller;

import com.example.demo.Model.DraftedMail;
import com.example.demo.Model.Mail;
import com.example.demo.Model.User;
import com.example.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        Mail m =mailService.createEmail(mail.getSender(), new ArrayList<>(mail.getReceivers()) ,mail.getSubject(),mail.getBody(), mail.getPriority(),mail.getAttachments());
        return ResponseEntity.ok(m);

    }

    @GetMapping(value = "/getEmails" ,params = {"email", "folder"})
    public ResponseEntity<List<Mail>> getUserMails(@Param("email") String email,@Param("folder") String folder){
        List<Mail> l = mailService.getMailsFromFolder(email, folder);
        return ResponseEntity.ok(l);

    }

    @GetMapping(value = "/getEmail", params = {"id"})
    public ResponseEntity<Mail> getUserMail(@Param("id") int id){
        Mail m = mailService.getEmail(id);
        return ResponseEntity.ok(m);
    }

    @GetMapping(value = "/getFolders", params = {"email"})
    public ResponseEntity<ArrayList<String>> getUserFolders(@Param("email") String email){
        ArrayList<String> m = mailService.getUserFolders(email);
        return ResponseEntity.ok(m);
    }

    @PostMapping(value = "/createDraft")
    public ResponseEntity<Mail> createDraft(@RequestParam("id") String id, @RequestBody Mail mail){
        DraftedMail m = mailService.createDrafted(id, mail);
        return ResponseEntity.ok(m);
    }

    @GetMapping(value = "/getDrafts" ,params = {"email"})
    public ResponseEntity<List<DraftedMail>> getUserMails(@Param("email") String email){
        List<DraftedMail> m = mailService.getUserDrafts(email);
        return ResponseEntity.ok(m);

    }

    @GetMapping(value = "/getDraft", params = {"id"})
    public ResponseEntity<DraftedMail> getUserDraft(@Param("id") String id){
        DraftedMail m = mailService.getDrafted(id);
        System.out.println("got it");
        return ResponseEntity.ok(m);
    }

    @PostMapping(value = "/deleteDraft/{id}/{email}")
    public ResponseEntity<Boolean> deleteDraft(@PathVariable("id") String id, @PathVariable("email") String email){
        Boolean b = mailService.deleteDraft(id,email);
        return ResponseEntity.ok(b);
    }

}
