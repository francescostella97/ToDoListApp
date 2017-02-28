package com.example.utente.todolistapp.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerPalette;
import com.android.colorpicker.ColorPickerSwatch;
import com.example.utente.todolistapp.R;
import com.example.utente.todolistapp.controllers.Utilities;
import com.example.utente.todolistapp.models.Note;
import com.example.utente.todolistapp.models.State;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import static android.R.attr.numColumns;

/**
 * Created by Utente on 20/02/2017.
 */

public class AddNoteActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {
    DatePickerDialog datePickerDialog ;
    Button changeDate;
    Calendar calendar ;
    int type;
    int Year, Month, Day ;
    String position;
    TextView txt_edited_date;
    Button btnAddNote, btnColor;
    EditText txt_title, txt_body;
    EditText txt_date,txt_color;
    int [] colors;
    boolean isSpecial;

    Toolbar toolbar;
    String state = "To do";
    public void onStart(){
        super.onStart();
        colors = getResources().getIntArray(R.array.items);
    }
    //back button saving
    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )  {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                if(type==0) createAddIntent();
                else if(type==1) createEditIntent();
                return true;
        }
        return super.onKeyDown( keyCode, event );
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setTitle("");
        toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        toolbar.setElevation(50);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_body = (EditText) findViewById(R.id.txt_body);
        txt_title = (EditText) findViewById(R.id.txt_title);
        txt_date = (EditText) findViewById(R.id.txt_date);
        txt_color = (EditText) findViewById(R.id.txt_color);
        txt_edited_date = (TextView) findViewById(R.id.edited_date_note);

        txt_color.setText("-1");
        Intent intent = getIntent();
        if(intent!=null){
            if(intent.getStringExtra("title")!=null){
                //editing...
                type = 1;
                position = intent.getStringExtra("position");
                txt_title.setText(intent.getStringExtra("title"));
                txt_body.setText(intent.getStringExtra("body"));
                txt_date.setText(intent.getStringExtra("due_date"));
                txt_edited_date.setText("Edited "+intent.getStringExtra("last_edited_date"));
                txt_color.setText(intent.getStringExtra("color"));
                isSpecial = Boolean.valueOf(intent.getStringExtra("isSpecial"));
                state = intent.getStringExtra("state");
                System.out.println("MY STATE IS ----> " +state);
                System.out.println("TITLE ---->"+intent.getStringExtra("title"));
                int c = Integer.valueOf(intent.getStringExtra("color"));

                txt_body.getRootView().setBackgroundColor(c);
                //txt_body.getRootView().getBackground().setAlpha(80);
                toolbar.setBackgroundColor(c);
                //toolbar.getBackground().setAlpha(80);


                Window window = ((AddNoteActivity)txt_body.getContext()).getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(((AddNoteActivity)txt_body.getContext()), R.color.mdtp_transparent_black));
            }
            else{
                Log.d("external intent got "," -");
                type = 0;
                txt_body.setText(intent.getStringExtra("body"));

            }
        }

            calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        //changeDate = (Button) findViewById(R.id.change_date_button);
        txt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = DatePickerDialog.newInstance(AddNoteActivity.this, Year, Month, Day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);
                String color = "#" + Integer.toHexString(ContextCompat.getColor
                        (getApplicationContext(), R.color.colorPrimary) & 0x00ffffff);
                datePickerDialog.setAccentColor(Color.parseColor(color));

                datePickerDialog.setTitle("Select due date for your item");

                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
            }
        });


        //fab_color.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View v) {

                /*final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();

                colorPickerDialog.initialize(
                        R.string.app_name, colors, R.color.colorAccent, 4, colors.length);
                colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        //btnColor.getRootView().setBackgroundColor(color);
                        fab_color.setBackgroundTintList(ColorStateList.valueOf(color));
                        fab_color.setBackgroundColor(color);
                        fab_color.setBackgroundDrawable(new ColorDrawable(color));

                        txt_color.setText(""+color);
                        Log.d("COLOR ",""+color);
               //     }
                });

                colorPickerDialog.show(getFragmentManager(), "ColorPickerDialog");

*/



        //    }
        //});


    }
    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {

        String date =  Day + "/" + (Month+1 )+ "/" + Year;
        txt_date.setText(date);
        //Toast.makeText(AddNoteActivity.this, date, Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        if(type==0) inflater.inflate(R.menu.add_menu,menu);
        else inflater.inflate(R.menu.menu,menu);
        System.out.println("-------------->" + isSpecial);
        //if(isSpecial) menu.getItem(0).setIcon(R.drawable.ic_bookmark_black_24dp);
        //else menu.getItem(0).setIcon(R.drawable.ic_bookmark_border_black_24dp);
        if(isSpecial) toolbar.getMenu().findItem(R.id.menu_special).setIcon(R.drawable.ic_bookmark_black_24dp);
            else toolbar.getMenu().findItem(R.id.menu_special).setIcon(R.drawable.ic_bookmark_border_black_24dp);

        if(state.equals("To do")) toolbar.getMenu().findItem(R.id.menu_state).setIcon(R.drawable.ic_unarchive_black_24dp);
            else toolbar.getMenu().findItem(R.id.menu_state).setIcon(R.drawable.ic_archive_black_24dp);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.menu_state:
                if(state.equals("Done")) state = "To do";
                else state = "Done";
                if(state.equals("To do")) item.setIcon(R.drawable.ic_unarchive_black_24dp);
                else item.setIcon(R.drawable.ic_archive_black_24dp);
                /*
                final Note note = ((MainActivity)txt_body.getContext()).noteCardAdapter.getNote(Integer.valueOf(position));
                final State currentState = note.getState();
                State newState;
                String support;
                if(currentState==State.DONE) {
                    newState = State.TODO;
                    support = " to do ";
                }
                else {
                    newState = State.DONE;
                    support = " done ";
                }
                note.setState(newState);
                Snackbar.make(txt_body.getRootView(),"Marked as"+support,Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        note.setState(currentState);
                    }
                }).show();*/
                break;
            case android.R.id.home:
                if(type==0) createAddIntent();
                else if(type==1) createEditIntent();

                finish();break;
            case R.id.menu_done:

                if(type==0){
                    Log.d("Color-->",""+txt_color.getText().toString());
                    //adding...
                    if(validateForm()) {
                        createAddIntent();
                        break;
                    }

                }
                else if(type==1){
                    //editing...
                    createEditIntent();
                    break;
                }
                break;
            case R.id.menu_delete:
                if(type==1) {
                    Intent intent = new Intent();
                    intent.putExtra("title", txt_title.getText().toString());
                    intent.putExtra("body", txt_body.getText().toString());
                    intent.putExtra("due_date",txt_date.getText().toString());
                    intent.putExtra("color",txt_color.getText().toString());
                    intent.putExtra("isSpecial",String.valueOf(isSpecial));
                    intent.putExtra("position", position);
                    setResult(3, intent);
                    finish();
                    break;
                }
            case R.id.menu_special:
                isSpecial = !isSpecial;
                if(isSpecial) item.setIcon(R.drawable.ic_bookmark_black_24dp);
                else item.setIcon(R.drawable.ic_bookmark_border_black_24dp);
                //Note note = ((MainActivity)txt_body.getContext()).noteCardAdapter.getNote(Integer.valueOf(position));
                //((MainActivity)txt_body.getContext()).db.updateNote(note);
                break;
            case R.id.menu_colors:
                final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();

                colorPickerDialog.initialize(
                        R.string.app_name, colors, R.color.colorAccent, 4, colors.length);
                colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        //btnColor.getRootView().setBackgroundColor(color);
                        //fab_color.setBackgroundTintList(ColorStateList.valueOf(color));
                        //fab_color.setBackgroundColor(color);
                        //fab_color.setBackgroundDrawable(new ColorDrawable(color));
                        txt_body.getRootView().setBackgroundColor(color);


                        toolbar.setBackgroundColor(color);
                        //toolbar.getBackground().setAlpha(80);
                        toolbar.setElevation(50);
                        colorPickerDialog.setSelectedColor(color);
                        txt_color.setText(""+color);
                        Log.d("COLOR ",""+color);
                        Window window = ((AddNoteActivity)txt_body.getContext()).getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(ContextCompat.getColor(((AddNoteActivity)txt_body.getContext()), R.color.mdtp_transparent_black));

                    }
                });

                colorPickerDialog.show(getFragmentManager(), "ColorPickerDialog");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validateForm(){
        /*String title = txt_title.getText().toString();
        String body = txt_body.getText().toString();
        if(!title.isEmpty()&&!body.isEmpty()) {

            return true;
        }
        Toast.makeText(this,"Fill the form!",Toast.LENGTH_SHORT).show();
        return false;*/ return true;
    }

    public void createEditIntent(){
        Intent intent = new Intent();
        intent.putExtra("title",txt_title.getText().toString());
        intent.putExtra("body",txt_body.getText().toString());
        intent.putExtra("due_date",txt_date.getText().toString());
        intent.putExtra("last_edited_date", Utilities.getCurrentDate());
        intent.putExtra("color",txt_color.getText().toString());
        intent.putExtra("isSpecial",String.valueOf(isSpecial));
        intent.putExtra("state",state);
        intent.putExtra("position",position);
        setResult(2,intent);
        finish();
    }

    public void createAddIntent(){
        Intent intent = new Intent();
        intent.putExtra("title", txt_title.getText().toString());
        intent.putExtra("body", txt_body.getText().toString());
        intent.putExtra("due_date",txt_date.getText().toString());
        intent.putExtra("color",txt_color.getText().toString());
        intent.putExtra("isSpecial",String.valueOf(isSpecial));
        intent.putExtra("state",state);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
