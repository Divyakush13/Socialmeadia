package com.example.socialmeadia;

public class user {
    private  String name, profession, email,password;
    private String userID;
    private int follwerCount;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile=profile;
    }

    private String profile;

    public user() {
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto=coverPhoto;
    }

    private String coverPhoto;
    public user(String name, String profession, String email, String password) {
        this.name=name;
        this.profession=profession;
        this.email=email;
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession=profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password=password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID=userID;
    }

    public int getFollwerCount() {
        return follwerCount;
    }

    public void setFollwerCount(int follwerCount) {
        this.follwerCount=follwerCount;
    }
}
