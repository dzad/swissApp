package com.swiss4ward.swissapp.models;

import org.json.JSONObject;

public class User {
    private int id;
    private String name,username,email,website,phone;
    private Address address;
    private Company company;

    public User(int id, String name, String username, String email, String website, String phone, Address address, Company company) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.website = website;
        this.phone = phone;
        this.address = address;
        this.company = company;
    }

    public User(int id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public User(JSONObject ob) {

        int id = ob.getInt("id");
        this.name = ob.getString("name");
        this.username = ob.getString("username");
        this.email = ob.getString("email");
        this.website = ob.getString("website");
        this.phone = ob.getString("phone");

        this.address = new Address(ob.getJSONObject("address"));

        this.company = new Company(ob.getJSONObject("company"));


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
