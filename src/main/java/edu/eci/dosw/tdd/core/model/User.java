package edu.eci.dosw.tdd.core.model;

public class User {

    private String name;
    private String ID;

    public User(String name, String ID) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

}