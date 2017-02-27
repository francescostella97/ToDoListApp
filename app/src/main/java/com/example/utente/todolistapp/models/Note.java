package com.example.utente.todolistapp.models;
import com.example.utente.todolistapp.controllers.Utilities;
import com.example.utente.todolistapp.models.State;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Utente on 20/02/2017.
 */

public class Note {
    private int id;
    private static int counter = 0;
    private String title;
    private String creationDate;
    private String lastEditDate;
    private String dueDate;
    private String body;
    private State state;
    private int color;
    private boolean isSelected = false;

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    private boolean isSpecial;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Note(){

    }
    public Note(String title, String body){
        this.id = counter;
        counter++;
        this.title = title;
        this.body = body;
        this.state = State.TODO;
        this.creationDate = Utilities.getCurrentDate();
        this.dueDate = "";
        this.lastEditDate = Utilities.getCurrentDate();
        this.color = android.R.color.white;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(String lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
    public String toString(){
        return  "Ref " +id+"\n"+
                "Title "+title+"\n"+
                "Description "+body+"\n"+
                "Created " +creationDate+"\n"+
                "Edited " +lastEditDate+"\n"+
                "Due " +dueDate+"\n"+
                "State " +state+"\n"+
                "Color "+color+"\n" +
                "isSpecial "+isSpecial+
                "State "+state.getDescription()+"\n";
    }
}
