package ua.javaexternal_shulzhenko.repair_agency.services.editing.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.UserEditingForm;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.Editor;

import java.util.LinkedList;
import java.util.List;

@Service
@Scope("prototype")
public class UserEditor implements Editor {

    private UserEditingForm form;
    private User user;
    private final List<UserEdits> edits;

    public UserEditor() {
        edits = new LinkedList<>();
    }

    public UserEditor compareFirstName(){
        if(!form.getFirstName().equals(user.getFirstName())){
            edits.add(UserEdits.FIRST_NAME);
        }
        return this;
    }

    public UserEditor compareLastName(){
        if(!form.getLastName().equals(user.getLastName())){
            edits.add(UserEdits.LAST_NAME);
        }
        return this;
    }

    public UserEditor compareEmail(){
        if(!form.getEmail().equals(user.getEmail())){
            edits.add(UserEdits.EMAIL);
        }
        return this;
    }

    public UserEditor compareRole(){
        if(!form.getRole().equals(user.getRole())){
            edits.add(UserEdits.ROLE);
        }
        return this;
    }

    public UserEditor setForm(UserEditingForm form) {
        this.form = form;
        return this;
    }

    public UserEditor setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public List<UserEdits> getEdits() {
        return edits;
    }

    public enum UserEdits{
        FIRST_NAME,
        LAST_NAME,
        EMAIL,
        ROLE
    }
}