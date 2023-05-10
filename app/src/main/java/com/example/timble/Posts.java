package com.example.timble;

public class Posts {

    String post_image;
    String post_time;
    String p_image;
    String p_name;
    String p_uid;
    String type;
    String description;

    public Posts() {
    }

    public Posts(String post_image, String post_time, String p_image, String p_name,String p_uid,String type,String description) {
        this.post_image = post_image;
        this.post_time = post_time;
        this.p_image = p_image;
        this.p_name = p_name;
        this.p_uid=p_uid;
        this.type=type;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPost_image() {
        return post_image;
    }

    public String getP_uid() {
        return p_uid;
    }

    public void setP_uid(String p_uid) {
        this.p_uid = p_uid;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getP_image() {
        return p_image;
    }

    public void setP_image(String p_image) {
        this.p_image = p_image;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }
}
