package com.example.timble;

import androidx.lifecycle.ViewModel;

public class ScheduleShardeViewmodel extends ViewModel {

    public boolean Edit;

    public boolean isEdit() {
        return Edit;
    }

    public void setEdit(boolean edit) {
        Edit = edit;
    }
}
