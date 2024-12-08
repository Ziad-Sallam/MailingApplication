package com.example.demo.service;

import com.example.demo.Model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.stereotype.Service;
import com.example.demo.Model.Mail;
import com.example.demo.Model.MailBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Service
public class MailService {
    static SystemData systemData = new SystemData();

    private static void getData(){
        if(systemData != null){
            ObjectMapper mapper = new ObjectMapper();
            try {

                systemData = mapper.readValue(new File("data/data.json"), SystemData.class);
                System.out.println(systemData.getUsers());
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
    private static void writeData(){
        ObjectMapper mapper = new ObjectMapper();
        try{
            mapper.writeValue(new File("data/data.json"), systemData);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createUser(String email, String password,String name) {
        ObjectMapper mapper = new ObjectMapper();

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        try{
            ArrayList<String> temp =  systemData.getUsers();
            if(temp.contains(email)){
                System.out.println("User already exists");
                return;
            }
            mapper.writeValue(new File("data/users/"+email+".json"), user);


            temp.add(email);
            systemData.setUsers(temp);
            writeData();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setUser(User user){
        ObjectMapper mapper = new ObjectMapper();

        try{
            mapper.writeValue(new File("data/users/"+user.getEmail()+".json"), user);

        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    public void createEmail(String from, ArrayList<String> to, String subject, String body) {
        MailBuilder builder = new MailBuilder();
        builder.setsender(from);
        builder.setrecivers(to);
        builder.setsubject(subject);
        builder.setbody(body);
        builder.setID(systemData.getNumberOfMails());
        Mail mail = builder.Build();
       // mail.setId(systemData.getNumberOfMails());
        ObjectMapper mapper = new ObjectMapper();
        try{
            mapper.writeValue(new File("data/mails/"+systemData.getNumberOfMails()+".json"), mail);

            User sent = getUser(from);

            ArrayList<Integer> arrSent = (sent.getSent() == null ? new ArrayList<>() : sent.getSent());
            arrSent.add(systemData.getNumberOfMails());
            sent.setSent(arrSent);
            for(String i : to){
                User received = getUser(i);
                ArrayList<Integer> arrReceived = received.getReceived();
                arrReceived.add(systemData.getNumberOfMails());
                received.setReceived(arrReceived);
                setUser(received);
            }
            setUser(sent);
            systemData.setNumberOfMails(systemData.getNumberOfMails()+1);
            writeData();

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public User getUser(String email) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File("data/users/"+ email +".json"), User.class);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new User();
    }

    public MailService(){
        getData();
    }
}

class test{
    public static void main(String[] args) {

    MailService x = new MailService();
    x.createUser("admin111","admin","admin");
    x.createUser("admin222","admin","admin");
    ArrayList<String> z = new ArrayList<>();
    z.add("admin222");

    x.createEmail("admin111",z,"heello","hello");



    }
}
