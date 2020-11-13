package com.swiss4ward.swissapp;

import com.swiss4ward.swissapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class Skeleton {

    private static Skeleton instance;

    private List<User> users;

    public Skeleton() {
        this.users = new ArrayList<>();
    }

    public static synchronized Skeleton getInstance() {
        if (instance == null)
            instance = new Skeleton();
        return instance;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User u) {
        this.users.add(u);
    }
}
