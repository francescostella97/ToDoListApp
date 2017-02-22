package com.example.utente.todolistapp.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Utente on 21/02/2017.
 */

public class Utilities {
    public static String getCurrentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

}
