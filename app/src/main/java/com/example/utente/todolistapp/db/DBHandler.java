package com.example.utente.todolistapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.utente.todolistapp.models.Note;
import com.example.utente.todolistapp.models.State;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Utente on 22/02/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDoListApp.db";
    public static final String NOTE_COLUMN_ID = "id";
    public static final String NOTE_COLUMN_TITLE = "title";
    public static final String NOTE_COLUMN_BODY = "body";
    public static final String NOTE_COLUMN_COLOR = "color";
    public static final String NOTE_COLUMN_TITLE_CREATION = "creation";
    public static final String NOTE_COLUMN_TITLE_EDITED = "edited";
    public static final String NOTE_COLUMN_TITLE_DUE = "due";
    public static final String NOTE_COLUMN_SPECIAL = "special";
    public static final String NOTE_COLUMN_STATE = "state";

    public int version = 1;
    public DBHandler(Context context){
        super(context,DATABASE_NAME,null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table note "+
                "(id integer primary key, title text, body text,"+
                    "creation text, due text, edited text, color integer, special text, state text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS note");
        onCreate(db);
        //db.execSQL("ALTER TABLE note ADD boolean isSpecial");
    }

    //add new element
    public boolean addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_TITLE,note.getTitle());
        values.put(NOTE_COLUMN_BODY,note.getBody());
        values.put(NOTE_COLUMN_COLOR,note.getColor());
        values.put(NOTE_COLUMN_TITLE_CREATION,note.getCreationDate());
        values.put(NOTE_COLUMN_TITLE_EDITED,note.getLastEditDate());
        values.put(NOTE_COLUMN_TITLE_DUE,note.getDueDate());
        values.put(NOTE_COLUMN_SPECIAL,String.valueOf(note.isSpecial()));
        values.put(NOTE_COLUMN_STATE, note.getState().getDescription());
        long id = db.insert("note",null,values);
        db.close();
        note.setId((int)id);
        return true;
    }
    //get all items in note table
    public ArrayList<Note> getAllNotes(){
        String state = null;
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from note", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            System.out.print("con stringa ");
            System.out.println(Integer.parseInt(res.getString(res.getColumnIndex(NOTE_COLUMN_ID))));
            Note note = new Note();
            note.setId(Integer.parseInt(res.getString(res.getColumnIndex(NOTE_COLUMN_ID))));
            System.out.print("con index ");
            System.out.println(res.getString(0));
            note.setTitle(res.getString(res.getColumnIndex(NOTE_COLUMN_TITLE)));
            note.setBody(res.getString(res.getColumnIndex(NOTE_COLUMN_BODY)));
            note.setColor(Integer.parseInt(res.getString(res.getColumnIndex(NOTE_COLUMN_COLOR))));
            note.setCreationDate(res.getString(res.getColumnIndex(NOTE_COLUMN_TITLE_CREATION)));
            note.setLastEditDate(res.getString(res.getColumnIndex(NOTE_COLUMN_TITLE_EDITED)));
            note.setDueDate(res.getString(res.getColumnIndex(NOTE_COLUMN_TITLE_DUE)));
            note.setSpecial(Boolean.valueOf(res.getString(res.getColumnIndex(NOTE_COLUMN_SPECIAL))));
            if (res.getString(res.getColumnIndex(NOTE_COLUMN_STATE)).equals(State.TODO.getDescription()))
                note.setState(State.TODO);
            else note.setState(State.DONE);
            if(note.isSpecial()) notes.add(0,note);
            else notes.add(note);
            res.moveToNext();
        }
        db.close();
        for(Note n:notes)
            System.out.println(n);
        return notes;
    }

    //delete element
    public int deleteNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        int r = db.delete("note",
                "id = ? ",
                new String[] { Integer.toString(note.getId()) });
        db.close();
        return r;
    }

    public boolean updateNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_COLUMN_TITLE,note.getTitle());
        values.put(NOTE_COLUMN_BODY,note.getBody());
        values.put(NOTE_COLUMN_COLOR,note.getColor());
        values.put(NOTE_COLUMN_TITLE_CREATION,note.getCreationDate());
        values.put(NOTE_COLUMN_TITLE_EDITED,note.getLastEditDate());
        values.put(NOTE_COLUMN_TITLE_DUE,note.getDueDate());
        values.put(NOTE_COLUMN_SPECIAL,String.valueOf(note.isSpecial()));
        values.put(NOTE_COLUMN_STATE, note.getState().getDescription());
        db.update("note", values, "id = ? ", new String[] { Integer.toString(note.getId()) } );
        db.close();
        return true;
    }

}
