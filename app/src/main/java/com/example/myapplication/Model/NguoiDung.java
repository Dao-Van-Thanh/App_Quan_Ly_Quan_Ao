package com.example.myapplication.Model;

import java.io.Serializable;

public class NguoiDung implements Serializable {
    private String id;
    private String image;
    private String fullname;
    private String email;
    private String password;
    private String admin;

    public NguoiDung() {
    }

    public NguoiDung(String id,String image, String fullname, String email, String password, String admin) {
        this.id = id;
        this.image = image;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "NguoiDung{" +
                "image='" + image + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", admin='" + admin + '\'' +
                '}';
    }
}
