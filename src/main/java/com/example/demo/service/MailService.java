package com.example.demo.service;

import com.example.demo.Model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class MailService {
    static SystemData systemData = new SystemData();

    public MailService() {
        getData();
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

    private static void writeData() {
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
            if (temp.contains(email)) {
                System.out.println("User already exists");
                return;
            }

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

    public Mail createEmail(String from, ArrayList<String> to, String subject, String body, int priority) {
        MailBuilder builder = new MailBuilder();
        builder.setSender(from);
        builder.setReceivers(to);
        builder.setSubject(subject);
        builder.setBody(body);
        builder.setId(systemData.getNumberOfMails());
        builder.setPriority(priority);
        builder.setDateSent();
        Mail mail = builder.build();

        ObjectMapper mapper = new ObjectMapper();
        Queue<String> receiverQueue = new LinkedList<>(to);

        try {
            createDirectoriesIfNeeded("data/mails/" + systemData.getNumberOfMails() + ".json");
            mapper.writeValue(new File("data/mails/" + systemData.getNumberOfMails() + ".json"), mail);
            User sender = getUser(from);
            if (sender != null) {
                Folder sentFolder = getFolder(sender, "Sent");
                if (sentFolder == null) {
                    addNewFolder(sender, "Sent");
                }
                sentFolder.addMail(systemData.getNumberOfMails());
                sender.getSent().add(systemData.getNumberOfMails());
                setUser(sender);
            }

            while (!receiverQueue.isEmpty()) {
                String receiverEmail = receiverQueue.poll();
                User receiver = getUser(receiverEmail);
                if (receiver != null) {
                    Folder inbox = getFolder(receiver, "Inbox");
                    if (inbox == null) {
                        addNewFolder(receiver, "Inbox");
                    }
                    inbox.addMail(systemData.getNumberOfMails());
                    receiver.addReceivedMail(systemData.getNumberOfMails());
                    setUser(receiver);
                }
            }
            systemData.setNumberOfMails(systemData.getNumberOfMails() + 1);
            writeData();
        } catch (IOException e) {
            System.out.println("Error creating email: " + e.getMessage());
        }
        return mail;
    }

    public void moveToTrash(String email, int mailId) {
        User user = getUser(email);
        if (user == null) {
            System.out.println("User not found: " + email);
            return;
        }

        Folder trash = getFolder(user, "Trash");
        if (trash == null) {
            addNewFolder(user, "Trash");
        }

        Folder inbox = getFolder(user, "Inbox");

        if (inbox != null && inbox.getFolderMailIds().remove((Integer) mailId)) {
            trash.addMail(mailId);
            setUser(user);
            System.out.println("Mail ID " + mailId + " moved to Trash for user: " + email);
        } else {
            System.out.println("Mail ID " + mailId + " not found in Inbox for user: " + email);
        }
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

    public void addNewFolder(User user, String folderName) {
        user.addFolder(folderName);
        setUser(user);
        System.out.println("Folder '" + folderName + "' created for user: " + user.getEmail());
    }


    public void renamefolder(User user, String oldname, String newname) {
        user.renameFolder(oldname, newname);
        setUser(user);
        System.out.println("name before :oldnme   name after: newname ");
    }

    public void deletefolder(User user, String foldername) {
        user.deleteFolder(foldername);
        setUser(user);
    }


    private void createDirectoriesIfNeeded(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
    private List<Mail> getMailsFromFolder(Folder folder) {
        List<Mail> mails = new ArrayList<>();
        folder.getFolderMailIds().forEach(mailId -> {
            Mail mail = getEmail(mailId);
            if (mail != null) {
                mails.add(mail);
            }
        });
        return mails;
    }
    public List<Mail> filterFolderMails(User user, String folderName, String filterType, String filterValue) {
        User user1 = getUser(user.getEmail());
        if (user1 != null) {
            Folder folder = getFolder(user1, folderName);
            if (folder != null) {
                List<Mail> folderMails = getMailsFromFolder(folder);
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


}
class TestMailService {
    public static void main(String[] args) {
        MailService mailService = new MailService();

        User user = mailService.getUser("z@Z.com");
        System.out.println(user);


//        mailService.createUser("user550@example.com", "password123", "User One");
//        mailService.createUser("user551@example.com", "password123", "User One");
//        mailService.createUser("user552@example.com", "password123", "User One");
//        //mailService.createUser("user60@example.com", "password456", "User Two");
//
//
//        ArrayList<String> recipients = new ArrayList<>();
//        recipients.add("user550@example.com");
//        recipients.add("user551@example.com");
//
//
//
//      mailService.createEmail("user552@example.com", recipients, "caroline", "hello!", 3);
       // mailService.createEmail("user50@example.com", recipients, "shosho", "Don't forget our meeting tomorrow!", 3);

      //  mailService.createEmail("user50@example.com", recipients, "mommon", "Don't forget our meeting tomorrow!", 3);


//        System.out.println("\nInbox for user60@example.com:");
       // User user2 = mailService.getUser("user60@example.com");
//        if (user2 != null) {
//            Folder inbox = user2.getUserFolders().stream()
//                    .filter(folder -> folder.getName().equalsIgnoreCase("Inbox"))
//                    .findFirst()
//                    .orElse(null);
//
//            if (inbox != null) {
//                inbox.getFolderMailIds().forEach(mailId -> {
//                    Mail mail = mailService.getEmail(mailId);
//                    if (mail != null) {
//                        System.out.println("Mail ID: " + mail.getId() + ", Subject: " + mail.getSubject());
//                    }
//                });
//            } else {
//                System.out.println("Inbox not found.");
//            }
//        }


        //System.out.println("\nMoving mail to Trash for user2@example.com...");
       // mailService.moveToTrash("user20@example.com", 30);

        // Step 6: Display user2's Trash folder
       // System.out.println("\nTrash for user60@example.com:");
//        if (user2 != null) {
//            Folder trash = user2.getUserFolders().stream()
//                    .filter(folder -> folder.getName().equalsIgnoreCase("Trash"))
//                    .findFirst()
//                    .orElse(null);
//
//            if (trash != null) {
//                trash.getFolderMailIds().forEach(mailId -> {
//                    Mail mail = mailService.getEmail(mailId);
//                    if (mail != null) {
//                        System.out.println("Mail ID: " + mail.getId() + ", Subject: " + mail.getSubject());
//                    }
//                });
//            } else {
//                System.out.println("Trash folder not found.");
//            }
//        }
//
//
//        System.out.println("\nSent folder for user100@example.com:");
//        User user1 = mailService.getUser("user50@example.com");
//        if (user1 != null) {
//            Folder sent = user1.getUserFolders().stream()
//                    .filter(folder -> folder.getName().equalsIgnoreCase("Sent"))
//                    .findFirst()
//                    .orElse(null);
//
//            if (sent != null) {
//                sent.getFolderMailIds().forEach(mailId -> {
//                    Mail mail = mailService.getEmail(mailId);
//                    if (mail != null) {
//                        System.out.println("Mail ID: " + mail.getId() + ", Subject: " + mail.getSubject());
//                    }
//                });
//            } else {
//                System.out.println("Sent folder not found.");
//            }
//        }
//

        //mailService.addNewFolder(user1,"new");
        // mailService.renamefolder(user1,"new","updatedname");
        //mailService.deletefolder(user1,"updatedname");
       // Folder sorted= mailService.sortFolder(user2,"inbox","subject");
//        List<Mail> filteredMails = mailService.filterFolderMails(user2, "Inbox", "sender", "user50@example.com");
//
//        for (Mail mail : filteredMails) {
//            System.out.println("Mail ID: " + mail.getId() + ", Subject: " + mail.getSubject() + ", Sender: " + mail.getSender() + ", Date: " + mail.getDateSent() + ", Priority: " + mail.getPriority());
//        }

       // System.out.println("\nSorted Folder: " + sorted.getName());
//        sorted.getFolderMailIds().forEach(mailId -> {
//            Mail mail = mailService.getEmail(mailId);
//            if (mail != null) {
//                System.out.println("Mail ID: " + mail.getId() + ", Subject: " + mail.getSubject() + ", Sender: " + mail.getSender() + ", Date: " + mail.getDateSent() + ", Priority: " + mail.getPriority());
//            }
//        });

    }
}