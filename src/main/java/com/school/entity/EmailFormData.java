package com.school.entity;


public class EmailFormData {

    private String name;
    

    private String password;
    

    private String email;
    

    private String subject;
    

    private String message;


    public EmailFormData() {
    }


    public EmailFormData(String name, String password, String email, String subject, String message) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getSubject() {
        return subject;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }
}

