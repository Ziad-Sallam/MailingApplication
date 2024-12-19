package com.example.demo.Service;

import com.example.demo.Model.*;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.demo.Service.MailServiceProxy.systemData;

@Service
public class MailService {
    public MailService() {
        getData();
        cleanOldMails();
    }

    public MailService(User user) {
    }

    private static void getData() {
        if (systemData != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                systemData = mapper.readValue(new File("data/data.json"), SystemData.class);
                System.out.println("Users loaded: " + systemData.getUsers());
            } catch (IOException e) {
                System.out.println("Error loading system data: " + e.getMessage());
            }
        }
    }

    public  void writeData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("data/data.json"), systemData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String email, String password, String name) {
        ObjectMapper mapper = new ObjectMapper();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        try {
            ArrayList<String> temp = systemData.getUsers();
            createDirectoriesIfNeeded("data/users/" + email + ".json");
            mapper.writeValue(new File("data/users/" + email + ".json"), user);

            temp.add(email);
            systemData.setUsers(temp);
            writeData();

            System.out.println("User created: " + email);
        } catch (IOException e) {
            System.out.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
        }
    }

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


    public User getUser(String email) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("data/users/" + email + ".json"), User.class);
        } catch (IOException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }

    public Attachment getAttachment(int id) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("data/attachments/" + id + ".json"),Attachment .class);
        } catch (IOException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }


    public Mail createEmail(String from, ArrayList<String> to, String subject, String body, int priority, List<Attachment> attachments) {
        MailBuilder builder = new MailBuilder();
        builder.setSender(from);
        builder.setReceivers(to);
        builder.setSubject(subject);
        builder.setBody(body);
        builder.setId(systemData.getNumberOfMails());
        builder.setPriority(priority);
        builder.setDateSent();

        if (attachments != null && !attachments.isEmpty()) {
            builder.addAttachments(attachments);
        }

        Mail mail = builder.build();

        ObjectMapper mapper = new ObjectMapper();
        Queue<String> receiverQueue = new LinkedList<>(to);

        try {
            createDirectoriesIfNeeded("data/mails/" + systemData.getNumberOfMails() + ".json");
            mapper.writeValue(new File("data/mails/" + systemData.getNumberOfMails() + ".json"), mail);
            User sender = getUser(from);
            if (sender != null) {
                Folder sentFolder = getFolder(sender, "Sent");

                sentFolder.addMail(systemData.getNumberOfMails());
                sender.getSent().add(systemData.getNumberOfMails());
                setUser(sender);
            }

            while (!receiverQueue.isEmpty()) {
                String receiverEmail = receiverQueue.poll();
                User receiver = getUser(receiverEmail);
                if (receiver != null) {
                    Folder inbox = getFolder(receiver, "Inbox");
                    inbox.addMail(systemData.getNumberOfMails());
                    receiver.addReceivedMail(systemData.getNumberOfMails());
                    setUser(receiver);
                }
            }
            assert attachments != null;
            systemData.setNumberOfMails(systemData.getNumberOfMails() + 1);
            writeData();
        } catch (IOException e) {
            System.out.println("Error creating email: " + e.getMessage());
        }
        return mail;
    }

    public Mail getEmail(int id) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("data/mails/" + id + ".json"), Mail.class);
        } catch (IOException e) {
            System.out.println("Error fetching email: " + e.getMessage());
        }
        return null;
    }

    public Folder getFolder(User user, String folderName) {
        return user.getUserFolders().stream()
                .filter(folder -> folder.getName().equalsIgnoreCase(folderName))
                .findFirst()
                .orElse(null);
    }

    public void addFolder(String folderName, String userName){
        User u = getUser(userName);
        u.addFolder(folderName);
        setUser(u);
    }


    public void renamefolder(String userName, String oldname, String newname) {
        User u = getUser(userName);
        u.renameFolder(oldname, newname);
        setUser(u);
    }

    public void deletefolder(String userName, String foldername) {
        User u = getUser(userName);
        u.deleteFolder(foldername);
        setUser(u);
    }


    public void createDirectoriesIfNeeded(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    public List<Mail> getMailsFromFolder(Folder folder) {
        List<Mail> mails = new ArrayList<>();
        for (Map.Entry<Integer, String> m : folder.getFolderMailIds().entrySet()) {
            Mail mail = getEmail(m.getKey());
            mails.add(mail);
        }

        return mails;
    }

    public List<Mail> getMailsFromFolder(String email, String folder) {
        User user = getUser(email);
        Folder f = getFolder(user, folder);
        return getMailsFromFolder(f);

    }


    public ArrayList<String> getUserFolders(String email) {
        User user = getUser(email);
        ArrayList<Folder> folders = user.getUserFolders();
        ArrayList<String> folderNames = new ArrayList<>();
        for (Folder f : folders) {
            folderNames.add(f.getName());
        }
        return folderNames;
    }

    public void cleanOldMails() {
        for (String usermail : systemData.getUsers()) {
            User user = getUser(usermail);
            Folder trash = getFolder(user, "Trash");
            if (trash != null) {
                LocalDateTime timenow = LocalDateTime.now().minusMinutes(2);

                Iterator<Map.Entry<Integer, String>> iterator = trash.getFolderMailIds().entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, String> entry = iterator.next();
                    LocalDateTime mailDate = LocalDateTime.parse(entry.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    if (mailDate.isBefore(timenow)) {
                        iterator.remove();
                    }
                }

                setUser(user);
            }
        }

        writeData();
    }


    public ArrayList<Contact> getContacts(String email) {
        User user = getUser(email);
        return user.getUsercontact();
    }

    public void addContacts(Contact contact, String email) {
        System.out.println(contact.getName());
        User user = getUser(email);
        System.out.println(user.getUsercontact().toString());
        user.addContact(contact);

        setUser(user);
    }


    public List<Mail> sortedAndFiteredpage(String useremail, int page, String foldername, String strategy, boolean isFiltered, String filterType, String filterValue) {

        User user1 = getUser(useremail);
        if (user1 == null) {
            System.out.println("User not found: " + user1.getEmail());
            return Collections.emptyList();
        }

        Folder folder = getFolder(user1, foldername);
        if (folder == null) {
            System.out.println("Folder not found: " + foldername);
            return Collections.emptyList();
        }

        SortStrategy sortStrategy;

        switch (strategy.toLowerCase()) {
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
                sortStrategy = new SortByDate();
                break;
        }


        sortStrategy.sort(folder);


        List<Mail> pagemails = getMailsFromFolder(folder);

        IMailFilter filter = null;
        if (isFiltered) {
            switch (filterType.toLowerCase()) {
                case "sender":
                    filter = new FilterBySender(filterValue);
                    break;
                case "subject":
                    filter = new FilterBySubject(filterValue);
                    break;
                default:
                    System.out.println("Unknown filter type: " + filterType);
            }

            if (filter != null) {
                pagemails = filter.applyFilter(pagemails);
            }
        }
        int start = (page - 1) * 4;
        int end = Math.min(start + 4, pagemails.size());
        if(start>pagemails.size()||end>pagemails.size()){
            return Collections.emptyList();

        }
        List<Mail> paginatedMails = pagemails.subList(start, end);

        return paginatedMails;
    }




}
class TestMailService {
    public static void main(String[] args) {
        MailService mailService = new MailService();


//        mailService.createUser("testUser@example.com", "password123", "Test User");
//        mailService.createUser("recipient1@example.com", "password123", "Test User");
//        mailService.createUser("recipient2@example.com", "password123", "Test User");


        User testUser = mailService.getUser("testUser@example.com");
        if (testUser == null) {
            System.out.println("Test user creation failed.");
            return;
        }


        ArrayList<String> recipients = new ArrayList<>();
        recipients.add("recipient1@example.com");
        recipients.add("recipient2@example.com");
List<Attachment> no =new ArrayList<>();
    //  mailService.createEmail("testUser@example.com", recipients, "Subject A", "Body A", 1, no);
     // mailService.createEmail("testUser@example.com", recipients, "Subject B", "Body B", 3,no);
//        mailService.createEmail("testUser@example.com", recipients, "Subject C", "Body C", 2,no);
//        mailService.createEmail("testUser@example.com", recipients, "Subject D", "Body D", 5, no);
//        mailService.createEmail("testUser@example.com", recipients, "Subject E", "Body E", 5, no);
          mailService.createEmail("testUser@example.com", recipients, "Subject F", "Body F", 1, no);


        // Step 3: Test Sorted and Filtered Pagination
        int page = 4;
        String folderName = "Inbox";
        String sortStrategy = "subject"; // Sorting by sender
        boolean isFiltered = true;
        String filterType = "subject"; // Filtering by subject
        String filterValue = "Subject A"; // Looking for "Subject A"
User user2=mailService.getUser("recipient2@example.com");
System.out.println(user2.getEmail());
        List<Mail> result = mailService.sortedAndFiteredpage("recipient2@example.com", page, folderName, sortStrategy, isFiltered, filterType, filterValue);
        System.out.println(result.isEmpty());
        // Step 4: Display Results
        System.out.println("\nTest Results for Sorted and Filtered Pagination:");
        for (Mail mail : result) {
            System.out.println("here");
            System.out.println("Mail ID: " + mail.getId() + ", Sender: " + mail.getSender() + ", Subject: " + mail.getSubject() + ", Priority: " + mail.getPriority());
        }
//        List<Mail> mails = mailService.getMailsFromFolder("recipient2@example.com", "Inbox");
//        for (Mail mail : mails) {
//            // Retrieve the full mail object using its ID
//            Mail fullMail = mailService.getEmail(mail.getId());
//            if (fullMail != null) {
//                System.out.println("Mail ID: " + fullMail.getId() +
//                        ", Sender: " + fullMail.getSender() +
//                        ", Subject: " + fullMail.getSubject() +
//                        ", Priority: " + fullMail.getPriority());
//            }
//        }
//        // Step 5: Additional Test Cases
//        System.out.println("\nTesting Pagination without Filtering:");
//        isFiltered = false;
//        result = mailService.sortedAndFiteredpage(testUser, page, folderName, sortStrategy, isFiltered, filterType, filterValue);
//        for (Mail mail : result) {
//            System.out.println("Mail ID: " + mail.getId() + ", Sender: " + mail.getSender() + ", Subject: " + mail.getSubject() + ", Priority: " + mail.getPriority());
//        }
    }
}