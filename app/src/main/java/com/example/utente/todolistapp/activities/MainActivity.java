package com.example.utente.todolistapp.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.example.utente.todolistapp.R;
import com.example.utente.todolistapp.adapters.NoteCardAdapter;
import com.example.utente.todolistapp.controllers.Utilities;
import com.example.utente.todolistapp.db.DBHandler;
import com.example.utente.todolistapp.models.Model;
import com.example.utente.todolistapp.models.Note;
import com.example.utente.todolistapp.models.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Utente on 20/02/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String KEY_LAYOUT_TYPE = "layout";
    private final int STRAGGERED = 1;
    private final int LINEAR  =0;
    private int layoutType;

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    Toolbar toolbar;
    Button btnToDo, btnDone;
    //recycler view items
    public FloatingActionButton fab;
    public RecyclerView recyclerView;
    //LinearLayoutManager layoutManager;
    RecyclerView.LayoutManager mLayoutManager;
    public NoteCardAdapter noteCardAdapter;
    LinearLayout mRelativeLayout;
    ArrayList<Note> myData;
    public DBHandler db;
    int [] colors;
    public void onActivityResult(int reqCode, int resCode, Intent data){
        Log.d("request ",String.valueOf(reqCode));
        Log.d("result ",String.valueOf(resCode));

        if(reqCode==1){
            Log.d("checking",".----...-.-");
            if(resCode == Activity.RESULT_OK){
                Log.d("MainActivity","ENTERED");
                String title = data.getStringExtra("title");
                String body = data.getStringExtra("body");
                String due_date = data.getStringExtra("due_date");
                String color = data.getStringExtra("color");
                boolean isSpecial = Boolean.valueOf(data.getStringExtra("isSpecial"));
                String state = data.getStringExtra("state");

                Note note = new Note();
                note.setTitle(title);
                note.setBody(body);
                note.setColor(Integer.valueOf(color));
                note.setDueDate(due_date);
                note.setSpecial(isSpecial);
                if(state.equals(State.DONE.getDescription())) note.setState(State.DONE);
                else note.setState(State.TODO);

                db.addNote(note);
                if(isSpecial) noteCardAdapter.addAtTop(note);
                else noteCardAdapter.addNote(note);
                //noteCardAdapter.notifyItemRangeChanged(0, noteCardAdapter.getItemCount());
                recyclerView.scrollToPosition(0);
                Log.d("MainActivity",note.toString());
                System.out.println("Added note \n"+note);
            }
            else if(resCode == 2){
                //editing...
                String title = data.getStringExtra("title");
                String body = data.getStringExtra("body");
                String due_date = data.getStringExtra("due_date");
                String last_edited_date = data.getStringExtra("last_edited_date");
                int position = Integer.valueOf(data.getStringExtra("position"));
                String color = data.getStringExtra("color");
                boolean isSpecial = Boolean.valueOf(data.getStringExtra("isSpecial"));
                String state = data.getStringExtra("state");

                Note editedNote = noteCardAdapter.getNote(position);
                editedNote.setBody(body);
                editedNote.setTitle(title);
                editedNote.setDueDate(due_date);
                editedNote.setLastEditDate(last_edited_date);
                editedNote.setColor(Integer.valueOf(color));
                editedNote.setSpecial(isSpecial);
                if(state.equals(State.DONE.getDescription())) editedNote.setState(State.DONE);
                else editedNote.setState(State.TODO);
                db.updateNote(editedNote);

                if(isSpecial) {
                    noteCardAdapter.removeNote(position);
                    noteCardAdapter.addAtTop(editedNote);
                    recyclerView.scrollToPosition(0);
                    //noteCardAdapter.notifyItemInserted(0);
                    //noteCardAdapter.notifyItemChanged(position);
                }
                //else noteCardAdapter.addNote(note);
                //noteCardAdapter.notifyItemRangeChanged(0, noteCardAdapter.getItemCount());

                noteCardAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(position);
                Log.d("MainActivity",editedNote.toString());

            }
            else if(resCode == 3){
                //deleting...
                String title = data.getStringExtra("title");
                String body = data.getStringExtra("body");
                String due_date = data.getStringExtra("due_date");
                int position = Integer.valueOf(data.getStringExtra("position"));
                db.deleteNote(noteCardAdapter.getNote(position));
                noteCardAdapter.removeNote(position);
            }
        }
    }
    public void onStart(){
        super.onStart();
        colors = getResources().getIntArray(R.array.items);
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandler(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        //toolbar.startActionMode(this);
        //toolbar.startActionMode((android.view.ActionMode.Callback) mActionModeCallback);
        toolbar.setTitle(R.string.app_name);
        toolbar.setElevation(0);

        mRelativeLayout = (LinearLayout) findViewById(R.id.linear_layout_main_activity);
        recyclerView = (RecyclerView) findViewById(R.id.rv_layout_main_activity);
        //layoutManager = new LinearLayoutManager(this);
        //mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager = getLayoutManager();
        System.out.println("layout type " +layoutType);
        noteCardAdapter = new NoteCardAdapter(this);
        noteCardAdapter.setDataSet(db.getAllNotes());
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
        Intent intent = getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction()) && intent.getType() != null) {
            if ("text/plain".equals(intent.getType())) {
                Intent newIntent = new Intent(MainActivity.this, AddNoteActivity.class);
                newIntent.putExtra("body", intent.getStringExtra(Intent.EXTRA_TEXT));
                Log.d("contenuto ", intent.getStringExtra(Intent.EXTRA_TEXT));
                startActivityForResult(newIntent, 1);
            }

        }
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        SharedPreferences layoutPref = getSharedPreferences(getString(R.string.preferred_layout),Context.MODE_PRIVATE);
        int type = layoutPref.getInt(KEY_LAYOUT_TYPE, -1);
        System.out.println("preferred layout got " + type);
        if (type == STRAGGERED){
            setLayoutType(STRAGGERED);
            Log.d("LAYOUT APPLIED ",""+type);
            return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        else if(type == LINEAR){
            setLayoutType(LINEAR);
            Log.d("LAYOUT APPLIED ",""+type);

            return new LinearLayoutManager(this);
        }
        return new LinearLayoutManager(this);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        if(getLayoutType() == STRAGGERED)
            toolbar.getMenu().findItem(R.id.menu_layout_type).setIcon(R.drawable.ic_view_list_white_24dp);
        else if(getLayoutType() == LINEAR)
            toolbar.getMenu().findItem(R.id.menu_layout_type).setIcon(R.drawable.ic_view_quilt_white_24dp);

        return super.onCreateOptionsMenu(menu);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_layout_type){
            if(getLayoutType() == STRAGGERED){
                setLayoutType(LINEAR);
                System.out.println("new layout type " +getLayoutType());
                item.setIcon(R.drawable.ic_view_quilt_white_24dp);

                recyclerView.setLayoutManager(new LinearLayoutManager(this));

            }
            else if(getLayoutType() == LINEAR){
                item.setIcon(R.drawable.ic_view_list_white_24dp);
                setLayoutType(STRAGGERED);
                System.out.println("new layout type " +getLayoutType());
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            }
        }
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


    @Override
    public void onStop() {
        Log.d("APP STOPPED ", "---------");
        SharedPreferences layoutPref = getSharedPreferences(getString(R.string.preferred_layout), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = layoutPref.edit();
        editor.putInt(KEY_LAYOUT_TYPE,getLayoutType());
        editor.commit();
        super.onStop();

    }
    private List<Note> getListData() {

        return noteCardAdapter.getDataSet();
    }
    public static ActionMode mActionMode;
    public ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.actionmode_menu, menu);
            mActionMode = mode;
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.act_menu_delete:
                    deleteSelectedNotes();
                    mode.finish();
                    break;
                case android.R.id.home:
                    System.out.println("hello .............>");
                    if(mActionMode!=null) mActionMode.finish();
                    System.out.println("closed action mode");
                    noteCardAdapter.flag = false;
                    noteCardAdapter.count = 0;
                    noteCardAdapter.deselectAllNotes();
                    mode.finish();
                    break;
                case R.id.act_menu_colors:
                    final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();

                    colorPickerDialog.initialize(
                            R.string.app_name, colors, R.color.colorAccent, 4, colors.length);
                    colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int color) {
                            changeColorOnSelectedNotes(color);
                            mode.finish();

                        }
                    });

                    colorPickerDialog.show(getFragmentManager(), "ColorPickerDialog");
                    break;
                default:
                    System.out.println("default act" );
            }
            return true;
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            noteCardAdapter.flag = false;
            noteCardAdapter.count = 0;
            noteCardAdapter.deselectAllNotes();
            mActionMode = null;
        }
    };

    public void deleteSelectedNotes(){
        int size = noteCardAdapter.getDataSet().size();
        for (int i = 0; i < noteCardAdapter.getDataSet().size() ; i++) {
            System.out.println("-------------------->\n"+noteCardAdapter.getDataSet().get(i));
            Note n = noteCardAdapter.getNote(i);
            if(noteCardAdapter.getDataSet().get(i).isSelected()){
                noteCardAdapter.removeNote(i);
                db.deleteNote(n);
                noteCardAdapter.notifyItemRemoved(i);
                i--;
            }
        }
    }

    public void changeColorOnSelectedNotes(int color){
        for (int i = 0; i< noteCardAdapter.getDataSet().size();i++){
            Note n = noteCardAdapter.getNote(i);
            if(n.isSelected()){
                n.setColor(color);
                db.updateNote(n);
            }
        }
        noteCardAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        noteCardAdapter.flag = false;
        noteCardAdapter.count = 0;
        noteCardAdapter.deselectAllNotes();

        return super.onKeyDown( keyCode, event );
    }

}
