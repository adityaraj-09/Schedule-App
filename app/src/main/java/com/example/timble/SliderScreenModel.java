package com.example.timble;

public class SliderScreenModel {

    String title;
    String desc;
    int ImageUrl;

    public SliderScreenModel() {
    }

    public SliderScreenModel(String title, String desc, Integer imageUrl) {
        this.title = title;
        this.desc = desc;
        ImageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        ImageUrl = imageUrl;
    }
}
