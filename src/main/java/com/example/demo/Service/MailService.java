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
    // static SystemData systemData = new SystemData();

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
//            if (temp.contains(email)) {
//                System.out.println("User already exists");
//                return;
//            }

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

    public void setAttachment(Attachment attachment) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            createDirectoriesIfNeeded("data/attachments/" + attachment.getId() + ".json");
            mapper.writeValue(new File("data/users/" + attachment.getId() + ".json"), attachment);
            writeData();
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    public void createAttachment(int mailId,String fileName,String fileType ,byte[] fileContent ) {
        ObjectMapper mapper = new ObjectMapper();
        Attachment attachment = new Attachment(fileName,fileType,fileContent);
        try{
            mapper.writeValue(new File("data/attachments/" + systemData.getNumberOfAttachments() + ".json"), attachment);
            systemData.setNumberOfAttachments(systemData.getNumberOfAttachments() + 1);
            writeData();
        }catch(Exception e){
            e.printStackTrace();
        }
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

        // Add attachments
        if (attachments != null && !attachments.isEmpty()) {
            builder.addAttachments(attachments);
        }

        Mail mail = builder.build();

        ObjectMapper mapper = new ObjectMapper();
        Queue<String> receiverQueue = new LinkedList<>(to);

        try {
            // Create directories and save email as JSON
            createDirectoriesIfNeeded("data/mails/" + systemData.getNumberOfMails() + ".json");
            mapper.writeValue(new File("data/mails/" + systemData.getNumberOfMails() + ".json"), mail);

            // Handle sender
            User sender = getUser(from);
            if (sender != null) {
                Folder sentFolder = getFolder(sender, "Sent");
//                if (sentFolder == null) {
//                    addFolder(sender, "Sent");
//                    sentFolder = getFolder(sender, "Sent"); // Ensure it's retrieved after creation
//                }
                sentFolder.addMail(systemData.getNumberOfMails());
                sender.getSent().add(systemData.getNumberOfMails());
                setUser(sender);
            }

            // Handle receivers
            while (!receiverQueue.isEmpty()) {
                String receiverEmail = receiverQueue.poll();
                User receiver = getUser(receiverEmail);
                if (receiver != null) {
                    Folder inbox = getFolder(receiver, "Inbox");
//                    if (inbox == null) {
//                        addNewFolder(receiver, "Inbox");
//                        inbox = getFolder(receiver, "Inbox"); // Ensure it's retrieved after creation
//                    }
                    inbox.addMail(systemData.getNumberOfMails());
                    receiver.addReceivedMail(systemData.getNumberOfMails());
                    setUser(receiver);
                }
            }
            assert attachments != null;
            for(Attachment a : attachments){
                createAttachment(systemData.getNumberOfMails(),a.getFileName(), a.getFileType(), a.getFileContent());
            }

            // Update system data and persist changes
            systemData.setNumberOfMails(systemData.getNumberOfMails() + 1);
            writeData();
        } catch (IOException e) {
            System.out.println("Error creating email: " + e.getMessage());
        }
        return mail;
    }

//    public void moveToTrash(String email, int mailId, String fromFolderName) {
//        User user = getUser(email);
//        if (user == null) {
//            System.out.println("User not found: " + email);
//            return;
//        }
//
//        Folder trash = getFolder(user, "Trash");
//        if (trash == null) {
//            addNewFolder(user, "Trash");
//            trash = getFolder(user, "Trash");
//        }
//
//        Folder fromFolder = getFolder(user, fromFolderName);
//        if (fromFolder == null) {
//            System.out.println("Folder not found: " + fromFolderName);
//            return;
//        }
//
//        if (fromFolder!=null) {
//
//            fromFolder.getFolderMailIds().remove((Integer) mailId);
//            assert trash!=null;
//            trash.addMail(mailId);
//            setUser(user);
//            System.out.println("Mail ID " + mailId + " moved from " + fromFolderName + " to Trash for user: " + email);
//        } else {
//            System.out.println("Mail ID " + mailId + " not found in " + fromFolderName + " for user: " + email);
//        }
//    }

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

//        folder.getFolderMailIds().forEach(mailID -> {
//            Mail mail = getEmail((Integer) mailId);
//            if (mail != null) {
//                mails.add(mail);
//            }
//        });
        return mails;
    }

    public List<Mail> getMailsFromFolder(String email, String folder) {
        User user = getUser(email);
//        if (user == null) {
//            System.out.println("User not found: " + email);
//            return null;
//        }
        Folder f = getFolder(user, folder);
        return getMailsFromFolder(f);

    }

//    public List<Mail> filterFolderMails(User user, String folderName, String filterType, String filterValue) {
//        User user1 = getUser(user.getEmail());
//        if (user1 != null) {
//            Folder folder = getFolder(user1, folderName);
//            if (folder != null) {
//                List<Mail> folderMails = getMailsFromFolder(folder);
//                IMailFilter filter = null;
//                switch (filterType.toLowerCase()) {
//                    case "sender":
//                        filter = new FilterBySender(filterValue);
//                        break;
//                    case "subject":
//                        filter = new FilterBySubject(filterValue);
//                        break;
//                    default:
//                        System.out.println("Invalid filter type: " + filterType);
//                        return List.of();
//                }
//                if (filter != null) {
//                    List<Mail> filteredMails = filter.applyFilter(folderMails);
//                    return filteredMails;
//                }
//            } else {
//                System.out.println("Folder not found: " + folderName);
//            }
//        } else {
//            System.out.println("User not found: " + user.getEmail());
//        }
//        return List.of();
//
//    }

    public ArrayList<String> getUserFolders(String email) {
        User user = getUser(email);
//        if (user == null) {
//            System.out.println("User not found: " + email);
//            return null;
//        }
        ArrayList<Folder> folders = user.getUserFolders();
        ArrayList<String> folderNames = new ArrayList<>();
        for (Folder f : folders) {
            folderNames.add(f.getName());
        }
        return folderNames;
    }


//    public Folder sortFolder(User user, String folderName, String strategy) {
//        User user1 = getUser(user.getEmail());
//        Folder folder = getFolder(user1, folderName);
//
//        SortStrategy sortStrategy;
//        switch (strategy.toLowerCase()) {
//            case "date":
//                sortStrategy = new SortByDate();
//                break;
//            case "sender":
//                sortStrategy = new SortBySender();
//                break;
//            case "subject":
//                sortStrategy = new SortBySubject();
//                break;
//            case "importance":
//                sortStrategy = new SortByPriority();
//                break;
//            case "body":
//                sortStrategy = new SortByBody();
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid sorting strategy");
//        }
//
//        sortStrategy.sort(folder);
//        return folder;
//    }

//    public DraftedMail createDrafted(String id, Mail mail) {
//        DraftedMail m = new DraftedMail();
//        m.setTemp(id);
//        m.setBody(mail.getBody());
//        m.setSubject(mail.getSubject());
//        m.setSender(mail.getSender());
//        m.setPriority(mail.getPriority());
//        m.setAttachments(mail.getAttachments());
//        m.setDateSent(m.getDateSent());
//        m.setReceivers(new ArrayList<>(mail.getReceivers()));
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            mapper.writeValue(new File("data/mails/" + id + ".json"), m);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        User user = getUser(mail.getSender());
//        if (!user.getDraft().contains(id)) {
//            user.addDraft(id);
//            setUser(user);
//        }
//
//
//        return m;
//
//    }

//    public DraftedMail getDrafted(String id) {
//        ObjectMapper mapper = new ObjectMapper();
//        DraftedMail m = new DraftedMail();
//        try {
//            System.out.println("here");
//            m = mapper.readValue(new File("data/mails/" + id + ".json"), DraftedMail.class);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return m;
//    }

//    public List<DraftedMail> getUserDrafts(String email) {
//        User user = getUser(email);
//        List<DraftedMail> drafts = new ArrayList<>();
//        for (String i : user.getDraft()) {
//            drafts.add(getDrafted(i));
//        }
//        return drafts;
//    }

//    public boolean deleteDraft(String id, String email) {
//        User user = getUser(email);
//        File f = new File("data/mails/" + id + ".json");
//
//        ArrayList<String> n = user.getDraft();
//        n.remove(id);
//        user.setDraft(n);
//        setUser(user);
//        return f.delete();
//    }


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




        int start = (page - 1) * 2;
        int end = Math.min(start + 2, pagemails.size());

        if(start>pagemails.size()||end>pagemails.size()){
//            List<Mail> paginateds=new ArrayList<>();

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
//        mailService.createEmail("testUser@example.com", recipients, "Subject F", "Body F", 1, no);


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