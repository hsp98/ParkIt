package com.example.android.parkit;

public class Data {

    private int id;

    private String name;

    private String email;

    private String phone_num;

    private String password;

    private String response;

    public Data(int id, String name, String email, String phone_num, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone_num = phone_num;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public String getPassword() {
        return password;
    }

    public String getResponse() {
        return response;
    }
}
