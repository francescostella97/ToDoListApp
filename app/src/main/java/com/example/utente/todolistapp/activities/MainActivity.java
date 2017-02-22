package com.example.utente.todolistapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.utente.todolistapp.R;
import com.example.utente.todolistapp.adapters.NoteCardAdapter;
import com.example.utente.todolistapp.controllers.Utilities;
import com.example.utente.todolistapp.models.Note;
import com.example.utente.todolistapp.models.State;

import java.util.ArrayList;

/**
 * Created by Utente on 20/02/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnToDo, btnDone;
    //recycler view items
    FloatingActionButton fab;
    RecyclerView recyclerView;
    //LinearLayoutManager layoutManager;
    RecyclerView.LayoutManager mLayoutManager;
    NoteCardAdapter noteCardAdapter;
    LinearLayout mRelativeLayout;
    ArrayList<Note> myData;

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if(reqCode==1){
            if(resCode == Activity.RESULT_OK){
                String title = data.getStringExtra("title");
                String body = data.getStringExtra("body");
                String due_date = data.getStringExtra("due_date");
                String color = data.getStringExtra("color");
                Note note = new Note(title,body);
                note.setColor(Integer.valueOf(color));
                note.setDueDate(due_date);
                noteCardAdapter.addNote(note);
                recyclerView.scrollToPosition(0);
                Log.d("MainActivity",note.toString());
            }
            else if(resCode == 2){
                //editing...
                String title = data.getStringExtra("title");
                String body = data.getStringExtra("body");
                String due_date = data.getStringExtra("due_date");
                String last_edited_date = data.getStringExtra("last_edited_date");
                int position = Integer.valueOf(data.getStringExtra("position"));
                String color = data.getStringExtra("color");

                Note editedNote = noteCardAdapter.getNote(position);
                editedNote.setBody(body);
                editedNote.setTitle(title);
                editedNote.setDueDate(due_date);
                editedNote.setLastEditDate(last_edited_date);
                editedNote.setColor(Integer.valueOf(color));
                noteCardAdapter.notifyItemChanged(position);
                recyclerView.scrollToPosition(position);
                Log.d("MainActivity",editedNote.toString());

            }
            else if(resCode == 3){
                //deleting...
                String title = data.getStringExtra("title");
                String body = data.getStringExtra("body");
                String due_date = data.getStringExtra("due_date");
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

        mRelativeLayout = (LinearLayout) findViewById(R.id.linear_layout_main_activity);
        recyclerView = (RecyclerView) findViewById(R.id.rv_layout_main_activity);
        //layoutManager = new LinearLayoutManager(this);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        noteCardAdapter = new NoteCardAdapter();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(noteCardAdapter);

        fab = (FloatingActionButton) findViewById(R.id.add_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);

                startActivityForResult(intent,1);
            }
        });

        btnDone = (Button) findViewById(R.id.done_btn);
        btnDone.setOnClickListener(this);
        btnToDo = (Button) findViewById(R.id.to_do_btn);
        btnToDo.setOnClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        //noteCardAdapter.setDataSet(getNoteCards());

    }

    public boolean onCreateOptionsMenu(Menu menu){

        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view){/*
        //myData = noteCardAdapter.getDataSet();
        if(view.getId() == R.id.done_btn){
            ArrayList<Note> list = noteCardAdapter.getDataSetByState(State.DONE);
            noteCardAdapter.getDataSet().clear();
            noteCardAdapter.getDataSet().addAll(list);

            noteCardAdapter.setDataSet(list);
        }
        else if(view.getId() == R.id.to_do_btn){
            ArrayList<Note> list = noteCardAdapter.getDataSetByState(State.TODO);

            noteCardAdapter.getDataSet().clear();
            noteCardAdapter.getDataSet().addAll(list);

            noteCardAdapter.setDataSet(list);
        }
        noteCardAdapter.notifyDataSetChanged();
        //noteCardAdapter.setDataSet(myData);*/
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
