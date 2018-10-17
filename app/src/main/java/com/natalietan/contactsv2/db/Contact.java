package com.natalietan.contactsv2.db;

import com.google.firebase.firestore.Exclude;

public class Contact {

    @Exclude private String id;
    private String name;
    private int number;

    public Contact() {
        this.name = "";
        this.number = 0;
    }

//    public Contact(int id, String name, int number) {
//        this.id = id;
//        this.name = name;
//        this.number = number;
//    }

    public Contact(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
