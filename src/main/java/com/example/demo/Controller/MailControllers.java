package com.example.demo.Controller;

import com.example.demo.Model.Contact;
import com.example.demo.Model.DraftedMail;
import com.example.demo.Model.Mail;
import com.example.demo.Model.User;
import com.example.demo.Service.MailService;
import com.example.demo.Service.MailServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class MailControllers {
    private final MailServiceProxy mailService;

    @Autowired
    public MailControllers(MailServiceProxy mailService) {
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

    @PostMapping(value = "/addContact/{mail}")
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact,@PathVariable("mail") String mail){
        System.out.println(contact.getName());
        mailService.addContacts(contact,mail);
        return ResponseEntity.ok(contact);
    }

    @GetMapping(value = "/getContacts/{mail}")
    public ResponseEntity<ArrayList<Contact>> getContacts(@PathVariable("mail") String mail){
        User u = mailService.getUser(mail);
        return ResponseEntity.ok(u.getUsercontact());
    }

    @PostMapping(value = "/addFolder/{email}")
    public ResponseEntity<String> addFolder(@RequestBody String  folderName,@PathVariable("email") String email){
        folderName = folderName.replace("+"," ");
        folderName = folderName.replace("="," ");
        mailService.addFolder(folderName,email);
        return ResponseEntity.ok(folderName);
    }

    @PostMapping(value = "/moveFolder/{email}", params = {"mailId","fromFolder","toFolder"})
    public ResponseEntity<String> moveFolder(@PathVariable("email") String email, @RequestParam("mailId") int id,@RequestParam("fromFolder") String fromFolder,@RequestParam("toFolder") String toFolder){
        mailService.moveEmail(email,id,fromFolder,toFolder);
        return ResponseEntity.ok(email);
    }

    @PostMapping(value = "/deleteContact/{name}/{email}")
    public ResponseEntity<Boolean> deleteContact(@PathVariable("name") String name, @PathVariable("email") String email){
        Boolean b = mailService.deleteContact(name ,email);
        return ResponseEntity.ok(b);
    }

        @PostMapping("/editContact/{user}")
        public ResponseEntity<?> editContact(@PathVariable String user,
                                             @RequestBody Map<String, Contact> contacts) {
            Contact oldContact = contacts.get("oldContact");
            Contact newContact = contacts.get("newContact");

            mailService.editContact(oldContact,newContact,user);

            return ResponseEntity.ok("Contact updated successfully");
        }



    @PostMapping(value = "/deleteFolder/{name}/{email}")
    public ResponseEntity<String> deleteFolder(@PathVariable("name") String name, @PathVariable("email") String email){
        mailService.deletefolder(email ,name);
        return ResponseEntity.ok(email);
    }

    @PostMapping(value = "/renameFolder/{userName}/{oldname}/{newname}")
    public ResponseEntity<String> renameFolder(@PathVariable("userName") String name, @PathVariable("oldname") String oldname, @PathVariable("newname") String newname){
        mailService.renamefolder(name,oldname,newname);
        return ResponseEntity.ok(name);
    }

}
