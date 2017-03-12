package com.example.utente.todolistapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.example.utente.todolistapp.R;
import com.example.utente.todolistapp.adapters.NoteCardAdapter;
import com.example.utente.todolistapp.controllers.MyTouchHelper;
import com.example.utente.todolistapp.db.DBHandler;
import com.example.utente.todolistapp.models.Note;
import com.example.utente.todolistapp.models.State;

/**
 * Created by Utente on 20/02/2017.
 */

public class MainActivity extends AppCompatActivity {
    //private constants
    private String KEY_LAYOUT_TYPE = "layout";
    private final int STRAGGERED = 1;
    private final int LINEAR  =0;

    //campo per tipo di layout
    private int layoutType;
    public int getLayoutType() {
        return layoutType;
    }
    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    Toolbar toolbar;
    int [] colors;
    MyTouchHelper touchHelper;
    //recycler view items
    public FloatingActionButton fab;
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    public NoteCardAdapter noteCardAdapter;
    //handler database locale
    public DBHandler db;


    public void onActivityResult(int reqCode, int resCode, Intent data){
        Log.d("request ",String.valueOf(reqCode));
        Log.d("result ",String.valueOf(resCode));

        if(reqCode==1){

            if(resCode == Activity.RESULT_OK){

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
                }

                noteCardAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(position);
            }
            else if(resCode == 3){
                //deleting...
                int position = Integer.valueOf(data.getStringExtra("position"));
                db.deleteNote(noteCardAdapter.getNote(position));
                noteCardAdapter.removeNote(position);
            }
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        colors = getResources().getIntArray(R.array.items);
    }
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
        noteCardAdapter.flag = false;
        noteCardAdapter.count = 0;
        noteCardAdapter.deselectAllNotes();
        mActionMode = null;
        return super.onKeyDown( keyCode, event );
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandler(this);

        //creazione toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        //toolbar.setElevation(0);
        ViewCompat.setElevation(toolbar, 0);

        //gestione rv e adapter
        recyclerView = (RecyclerView) findViewById(R.id.rv_layout_main_activity);
        mLayoutManager = getLayoutManager();
        noteCardAdapter = new NoteCardAdapter(this);
        noteCardAdapter.setDataSet(db.getAllNotes());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(noteCardAdapter);

        //multiselection items

        touchHelper = new MyTouchHelper(noteCardAdapter,this);
        ItemTouchHelper helper = new ItemTouchHelper(touchHelper);
        helper.attachToRecyclerView(recyclerView);

        //animazioni floating action button
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

        //floating action button principale
        fab = (FloatingActionButton) findViewById(R.id.add_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent per creazione nota
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
                startActivityForResult(intent,1);
            }
        });
        //gestione intent da un app esterna
        Intent intent = getIntent();
        if (Intent.ACTION_SEND.equals(intent.getAction()) && intent.getType() != null) {
            if ("text/plain".equals(intent.getType())) {
                Intent newIntent = new Intent(MainActivity.this, AddNoteActivity.class);
                newIntent.putExtra("body", intent.getStringExtra(Intent.EXTRA_TEXT));
                startActivityForResult(newIntent, 1);
            }
        }
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        //acquisizione layout dalle sharedPreferences
        SharedPreferences layoutPref = getSharedPreferences(getString(R.string.preferred_layout),Context.MODE_PRIVATE);
        int type = layoutPref.getInt(KEY_LAYOUT_TYPE, -1);
        //impostazione layout delle sharedPreferences
        if (type == STRAGGERED){
            setLayoutType(STRAGGERED);
            return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        else if(type == LINEAR){
            setLayoutType(LINEAR);
            return new LinearLayoutManager(this);
        }
        return new LinearLayoutManager(this);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        //setta icona in base al layout delle sharedPreferences
        if(getLayoutType() == STRAGGERED)
            toolbar.getMenu().findItem(R.id.menu_layout_type).setIcon(R.drawable.ic_view_list_white_24dp);
        else if(getLayoutType() == LINEAR)
            toolbar.getMenu().findItem(R.id.menu_layout_type).setIcon(R.drawable.ic_view_quilt_white_24dp);

        return super.onCreateOptionsMenu(menu);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //aggiorna icona e layout in base al layout delle sharedPreferences
        if(id == R.id.menu_layout_type){
            if(getLayoutType() == STRAGGERED){
                setLayoutType(LINEAR);
                item.setIcon(R.drawable.ic_view_quilt_white_24dp);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
            else if(getLayoutType() == LINEAR){
                item.setIcon(R.drawable.ic_view_list_white_24dp);
                setLayoutType(STRAGGERED);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        //salvataggio sharedPreferences
        SharedPreferences layoutPref = getSharedPreferences(getString(R.string.preferred_layout), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = layoutPref.edit();
        editor.putInt(KEY_LAYOUT_TYPE,getLayoutType());
        editor.commit();
        super.onStop();

    }

    public static ActionMode mActionMode;
    public ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // chiamato quando l'actionMode si crea
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //impostazione menu dell'action mode
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

        // gestione click su item del menu dell'action mode
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

        // chiamato all'uscita dell'action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            noteCardAdapter.flag = false;
            noteCardAdapter.count = 0;
            noteCardAdapter.deselectAllNotes();
            mActionMode = null;
        }
    };

    //elimina tutte le note selezionate
    public void deleteSelectedNotes(){
        for (int i = 0; i < noteCardAdapter.getDataSet().size() ; i++) {
            Note n = noteCardAdapter.getNote(i);
            if(noteCardAdapter.getDataSet().get(i).isSelected()){
                noteCardAdapter.removeNote(i);
                db.deleteNote(n);
                noteCardAdapter.notifyItemRemoved(i);
                i--;
            }
        }
    }
    //cambia colore alle note selezionate
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
}
