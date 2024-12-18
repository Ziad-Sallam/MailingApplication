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
        int x = mailService.createUser(user.getEmail(), user.getPassword(), user.getName());
        if(x==-1) return ResponseEntity.status(403).build();
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
        if(m==null) return ResponseEntity.status(403).build();
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
        int x = mailService.addContacts(contact,mail);
        if(x==-1) return ResponseEntity.status(403).build();
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
        folderName = folderName.replace("=","");

        int x = mailService.addFolder(folderName,email);
        if(x==-1) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(folderName);
    }

    @PostMapping(value = "/moveFolder/{email}", params = {"mailId", "fromFolder", "toFolder"})
    public ResponseEntity<String> moveFolder(@PathVariable("email") String email, @RequestParam("mailId") List<Integer> id, @RequestParam("fromFolder") String fromFolder, @RequestParam("toFolder") String toFolder) {
        for (int i : id) {
            mailService.moveEmail(email, i, fromFolder, toFolder);
        }
        return ResponseEntity.ok(email);
    }

    @DeleteMapping(value = "/deleteContact/{name}/{email}")
    public ResponseEntity<Boolean> deleteContact(@PathVariable("name") String name, @PathVariable("email") String email){
        Boolean b = mailService.deleteContact(name ,email);
        return ResponseEntity.ok(b);
    }

        @PostMapping("/editContact/{user}")
        public ResponseEntity<?> editContact(@PathVariable String user,
                                             @RequestBody Map<String, Contact> contacts) {
            Contact oldContact = contacts.get("oldContact");
            Contact newContact = contacts.get("newContact");

            int x = mailService.editContact(oldContact,newContact,user);
            if(x==-1) return ResponseEntity.status(403).build();
            return ResponseEntity.ok("Contact updated successfully");
        }



    @PostMapping(value = "/deleteFolder/{name}/{email}")
    public ResponseEntity<String> deleteFolder(@PathVariable("name") String name, @PathVariable("email") String email){
        mailService.deletefolder(email ,name);
        return ResponseEntity.ok(email);
    }

    @PostMapping(value = "/renameFolder/{userName}/{oldname}/{newname}")
    public ResponseEntity<String> renameFolder(@PathVariable("userName") String name, @PathVariable("oldname") String oldname, @PathVariable("newname") String newname){
        int x = mailService.renamefolder(name,oldname,newname);
        if(x==-1) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(name);
    }

    @GetMapping(value = "/numberPages/{userName}", params = {"foldername"})
    public ResponseEntity<Integer> numberPages(@PathVariable("userName") String name,@Param("foldername") String foldername) {
        int num =mailService.numberofpages(name , foldername);
        return ResponseEntity.ok(num);
    }
    @GetMapping(value = "/emailPages/{userName}", params = {"page","foldername","strategy","isFiltered","filterType","filterValue"})
    public ResponseEntity<List<Mail>> emailPages(@PathVariable("userName") String name,
                                                 @Param("page") int page,
                                                 @Param("foldername") String foldername,
                                                 @Param("strategy") String strategy,
                                                 @Param("isFiltered") boolean isFiltered,
                                                 @Param("filterType") String filterType,
                                                 @Param("filterValue") String filterValue) {
        List<Mail> m =mailService.sortedAndFiteredpage(name, page, foldername,strategy,isFiltered,filterType,filterValue);
        return ResponseEntity.ok(m);
    }
}
