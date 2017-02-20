package com.example.utente.todolistapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.utente.todolistapp.R;
import com.example.utente.todolistapp.adapters.NoteCardAdapter;
import com.example.utente.todolistapp.models.Note;

import java.util.ArrayList;

/**
 * Created by Utente on 20/02/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //recycler view items
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    NoteCardAdapter noteCardAdapter;

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if(reqCode==1){
            if(resCode == Activity.RESULT_OK){
                String title = data.getStringExtra("title");
                String body = data.getStringExtra("body");
                noteCardAdapter.addNote(new Note(title,body));
                recyclerView.scrollToPosition(0);
            }
            else if(resCode == 2){
                //editing...
                String title = data.getStringExtra("title");
                String body = data.getStringExtra("body");
                int position = Integer.valueOf(data.getStringExtra("position"));

                Note editedNote = noteCardAdapter.getNote(position);
                editedNote.setBody(body);
                editedNote.setTitle(title);
                noteCardAdapter.notifyItemChanged(position);
                recyclerView.scrollToPosition(position);
            }
            else if(resCode == 3){
                //deleting...
                String title = data.getStringExtra("title");
                String body = data.getStringExtra("body");
                int position = Integer.valueOf(data.getStringExtra("position"));
                noteCardAdapter.removeNote(position);
            }
        }
    }
    public void onStart(){
        super.onStart();
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        recyclerView = (RecyclerView) findViewById(R.id.rv_layout_main_activity);
        layoutManager = new LinearLayoutManager(this);
        noteCardAdapter = new NoteCardAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteCardAdapter);

        findViewById(R.id.add_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);

                startActivityForResult(intent,1);
            }
        });

        noteCardAdapter.setDataSet(getNoteCards());
    }

    public boolean onCreateOptionsMenu(Menu menu){

        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){
        Log.d("MainActivity","Presed");
    }

    public ArrayList<Note> getNoteCards(){
        ArrayList<Note> noteCards = new ArrayList<>();
        Note one = new Note("Registro Pranzi","Fare reg pranzi ogni sabato");
        Note two = new Note("Ritirare biancheria", "Mauris facilisis pulvinar accumsan. Etiam at tortor fermentum, scelerisque nunc non, consectetur mi. Etiam commodo dictum leo, ac malesuada quam efficitur ut. Fusce efficitur arcu vestibulum mollis congue. Nunc nec elit sed libero maximus auctor. Aliquam purus dui, molestie non gravida id, rutrum vel elit. Pellentesque imperdiet, erat eget faucibus accumsan, eros enim varius sem, et molestie orci nulla a erat. Nulla sodales consequat dolor. Proin quis massa et sem tristique finibus. Aenean consequat feugiat quam, sed eleifend tortor porta eget. Phasellus at venenatis felis. Fusce sed mauris nec metus congue ullamcorper. Nam consequat enim vitae orci finibus, commodo ultricies eros finibus. Nulla malesuada sagittis risus eget pharetra.");
        //noteCards.add(one);
        //noteCards.add(two);

        return  noteCards;
    }
}
