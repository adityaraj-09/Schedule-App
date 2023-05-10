package com.example.timble;

import java.util.ArrayList;

public class ParentModel {
    String title;
    ArrayList<Books> listofbooklist;

    public ParentModel() {
    }

    public ParentModel(String title, ArrayList<Books> listofbooklist) {
        this.title = title;
        this.listofbooklist = listofbooklist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Books> getListofbooklist() {
        return listofbooklist;
    }

    public void setListofbooklist(ArrayList<Books> listofbooklist) {
        this.listofbooklist = listofbooklist;
    }
}
