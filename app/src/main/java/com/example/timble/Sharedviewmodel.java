package com.example.timble;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Sharedviewmodel extends ViewModel {
    public boolean isEdit;
    private final MutableLiveData<Task> selectedItem=new MutableLiveData<>();
    public void selectItem(Task task){
        selectedItem.setValue(task);
    }
    public LiveData<Task> getSelectedItem(){
        return selectedItem;
    }
    public void setIsEdit(boolean isEdit){
        this.isEdit=isEdit;
    }
    public boolean getIsEdit(){
        return isEdit;
    }


}
