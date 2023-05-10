package com.example.timble;

public class Users {
    String uid;
    String name;
    String phone ;
    String imageUri;
    String Group;
    String Hostel;

    public Users() {
    }

    public Users(String uid, String name, String phone, String imageUri,String Group,String Hostel) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.imageUri = imageUri;
        this.Group=Group;
        this.Hostel=Hostel;
    }

    public String getUid() {
        return uid;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getHostel() {
        return Hostel;
    }

    public void setHostel(String hostel) {
        Hostel = hostel;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
