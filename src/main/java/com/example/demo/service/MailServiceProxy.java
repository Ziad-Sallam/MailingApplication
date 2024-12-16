package com.example.demo.Service;

import com.example.demo.Service.*;
import com.example.demo.Service.MailService;
import com.example.demo.Model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service

public class MailServiceProxy implements IMailServiceProxy {

    public static SystemData systemData = new SystemData();
    MailService mailService = new MailService();

    @Autowired
    public MailServiceProxy(MailService mailService) {
        this.mailService = mailService;
    }

    public MailServiceProxy() {

    }

    @Override
    public void createUser(String email, String password, String name) {
        ArrayList<String> temp = systemData.getUsers();
        System.out.println(temp);
        if (temp.contains(email)) {
            System.out.println("User already exists");
            return;
        } else {
            mailService.createUser(email, password, name);
        }
    }

    @Override
    public User getUser(String email) {
        ArrayList<String> users = systemData.getUsers();
        if (users != null && users.contains(email)) {
            return mailService.getUser(email);

        } else {
            return null;
        }

    }

    @Override
    public Mail createEmail(String from, ArrayList<String> to, String subject, String body, int priority, List<Attachment> attachments) {
        ArrayList<String> users = systemData.getUsers();

        for (String recipient : to) {
            if (users == null || !users.contains(recipient)) {
//                System.out.println("Recipient " + recipient + " does not exist.");
                return null;
            }
        }
        return mailService.createEmail(from, to, subject, body, priority, attachments);
    }


    public void moveEmail(String email, int mailId, String fromFolderName, String toFolderName) {
        User user = getUser(email);
        if (user == null) {
            System.out.println("User not found: " + email);
            return;
        }


        Folder fromFolder = getFolder(user, fromFolderName);
        if (fromFolder == null) {
            System.out.println("Folder not found: " + fromFolderName);
            return;
        }


        Folder toFolder = getFolder(user, toFolderName);
//        if (toFolder == null) {
//
//            addNewFolder(user, toFolderName);
//            toFolder = getFolder(user, toFolderName);
//        }


        if (fromFolder!=null) {
            fromFolder.getFolderMailIds().remove((Integer) mailId);
            toFolder.addMail(mailId);
            setUser(user);
            System.out.println("Mail ID " + mailId + " moved from " + fromFolderName + " to " + toFolderName + " for user: " + email);
        } else {
            System.out.println("Mail ID " + mailId + " not found in " + fromFolderName + " for user: " + email);
        }
    }

    @Override
    public void setUser(User user) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            createDirectoriesIfNeeded("data/users/" + user.getEmail() + ".json");
            mapper.writeValue(new File("data/users/" + user.getEmail() + ".json"), user);
            writeData();
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    @Override
    public void writeData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("data/data.json"), systemData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Mail getEmail(int id) {
//        User user = getUser(email);
//        List<Folder> userfolders = user.getUserFolders();
//        for (Folder folder : userfolders) {
//            for (Map.Entry<Integer, String> m : folder.getFolderMailIds().entrySet()) {
//                if (id == m.getKey()) {
//                    return mailService.getEmail(id);
//                }
//
//            }
//        }


        return mailService.getEmail(id);

    }

    @Override
    public Folder getFolder(User user, String folderName) {
        return mailService.getFolder(user, folderName);
    }

    @Override
    public void addFolder(String folderName, String userName) {
        User u = getUser(userName);
        ArrayList<Folder> f = u.getUserFolders();
        for (Folder name : f) {
            if (name.getName() == folderName) {
                System.out.println("already exist");
                return;
            }
        }
        mailService.addFolder(folderName , userName);

    }


    @Override
    public void renamefolder(User user, String oldname, String newname) {
        List<String> foldernames = getUserFolders(user.getEmail());
        for (String name : foldernames) {
            if (name.equals(newname)) {
                System.out.println("already exist");
                return;
            }
        }
        mailService.renamefolder(user, oldname, newname);


    }

    @Override
    public void deletefolder(User user, String foldername) {
        mailService.deletefolder(user, foldername);

    }

    @Override
    public void createDirectoriesIfNeeded(String filePath) {
        mailService.createDirectoriesIfNeeded(filePath);

    }

//    @Override
//    public List<Mail> getMailsFromFolder(Folder folder) {
//
//        return List.of();
//    }

    @Override
    public List<Mail> getMailsFromFolder(String email, String folder) {
        return mailService.getMailsFromFolder(email, folder);

    }

    @Override
    public List<Mail> filterFolderMails(User user, String folderName, String filterType, String filterValue) {
        User user1 = getUser(user.getEmail());
        if (user1 != null) {
            Folder folder = getFolder(user1, folderName);
            if (folder != null) {
                List<Mail> folderMails = getMailsFromFolder(user.getEmail(), folderName);
                IMailFilter filter = null;
                switch (filterType.toLowerCase()) {
                    case "sender":
                        filter = new FilterBySender(filterValue);
                        break;
                    case "subject":
                        filter = new FilterBySubject(filterValue);
                        break;
                    default:
                        System.out.println("Invalid filter type: " + filterType);
                        return List.of();
                }
                if (filter != null) {
                    List<Mail> filteredMails = filter.applyFilter(folderMails);
                    return filteredMails;
                }
            } else {
                System.out.println("Folder not found: " + folderName);
            }
        } else {
            System.out.println("User not found: " + user.getEmail());
        }
        return List.of();

    }

    @Override
    public ArrayList<String> getUserFolders(String email) {

        ArrayList<String> users = systemData.getUsers();
        if (users != null && users.contains(email)) {
            return mailService.getUserFolders(email);

        } else {
            return null;
        }
    }

    @Override
    public Folder sortFolder(User user, String folderName, String strategy) {
        User user1 = getUser(user.getEmail());
        Folder folder = getFolder(user1, folderName);

        SortStrategy sortStrategy;
        switch (strategy.toLowerCase()) {
            case "date":
                sortStrategy = new SortByDate();
                break;
            case "sender":
                sortStrategy = new SortBySender();
                break;
            case "subject":
                sortStrategy = new SortBySubject();
                break;
            case "importance":
                sortStrategy = new SortByPriority();
                break;
            case "body":
                sortStrategy = new SortByBody();
                break;
            default:
                throw new IllegalArgumentException("Invalid sorting strategy");
        }

        sortStrategy.sort(folder);
        return folder;
    }

    public DraftedMail createDrafted(String id, Mail mail) {
        DraftedMail m = new DraftedMail();
        m.setTemp(id);
        m.setBody(mail.getBody());
        m.setSubject(mail.getSubject());
        m.setSender(mail.getSender());
        m.setPriority(mail.getPriority());
        m.setAttachments(mail.getAttachments());
        m.setDateSent(m.getDateSent());
        m.setReceivers(new ArrayList<>(mail.getReceivers()));
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("data/mails/" + id + ".json"), m);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        User user = getUser(mail.getSender());
        if (!user.getDraft().contains(id)) {
            user.addDraft(id);
            setUser(user);
        }


        return m;

    }

    public DraftedMail getDrafted(String id) {
        ObjectMapper mapper = new ObjectMapper();
        DraftedMail m = new DraftedMail();
        try {
            System.out.println("here");
            m = mapper.readValue(new File("data/mails/" + id + ".json"), DraftedMail.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return m;
    }

    public List<DraftedMail> getUserDrafts(String email) {
        User user = getUser(email);
        List<DraftedMail> drafts = new ArrayList<>();
        for (String i : user.getDraft()) {
            drafts.add(getDrafted(i));
        }
        return drafts;
    }

    public boolean deleteDraft(String id, String email) {
        User user = getUser(email);
        File f = new File("data/mails/" + id + ".json");

        ArrayList<String> n = user.getDraft();
        n.remove(id);
        user.setDraft(n);
        setUser(user);
        return f.delete();
    }

//    @Override
//    public void cleanOldMails() {
//
//    }
//}


    public ArrayList<Contact> getContacts(String email) {
        User user = getUser(email);
        return user.getUsercontact();
    }

    public void addContacts(Contact contact, String email) {
        User user = getUser(email);
        List<Contact> usercontact = getContacts(email);
        for (Contact c : usercontact) {
            if (c.getName().equals(contact.getName())) {
                System.out.println("contact already exists ");
                return;
            }
        }
        mailService.addContacts(contact, email);
    }
}