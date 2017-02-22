package com.example.utente.todolistapp.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
    Button btnAddNote, btnColor;
    EditText txt_title, txt_body;
    EditText txt_date,txt_color;
    int [] colors;
    AppCompatButton fab_color;


    public void onStart(){
        super.onStart();
        colors = getResources().getIntArray(R.array.items);
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_body = (EditText) findViewById(R.id.txt_body);
        txt_title = (EditText) findViewById(R.id.txt_title);
        txt_date = (EditText) findViewById(R.id.txt_date);
        txt_color = (EditText) findViewById(R.id.txt_color);
        fab_color = (AppCompatButton) findViewById(R.id.fab_color);
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

                txt_color.setText(intent.getStringExtra("color"));

                int c = Integer.valueOf(intent.getStringExtra("color"));
                fab_color.setBackgroundTintList(ColorStateList.valueOf(c));
                fab_color.setBackgroundColor(c);
                fab_color.setBackgroundDrawable(new ColorDrawable(c));
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


        fab_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();

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
                    }
                });

                colorPickerDialog.show(getFragmentManager(), "ColorPickerDialog");



/*
                LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
                ColorPickerPalette colorPickerPalette = new ColorPickerPalette(v.getContext());
                colorPickerPalette.inflate(v.getContext(),R.layout.colorpickerpalette,null);
                colorPickerPalette.drawPalette(getResources().getIntArray(R.array.color_choices), android.R.color.black);
                colorPickerPalette.init(colors.length, 4, new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        //colorPickerPalette.drawPalette(colors, color);
                    }
                });
                AlertDialog alert = new AlertDialog.Builder(v.getContext(),R.style.MyDialogTheme)
                        .setTitle("Color")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setView(colorPickerPalette)
                        .create();
                alert.show();
*/
                //--------------------------

            }
        });


    }
    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {

        String date =  Day + "/" + (Month+1 )+ "/" + Year;
        txt_date.setText(date);
        //Toast.makeText(AddNoteActivity.this, date, Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case android.R.id.home: finish();break;
            case R.id.menu_done:

                if(type==0){
                    Log.d("Color-->",""+txt_color.getText().toString());
                    //adding...
                    if(validateForm()) {
                        Intent intent = new Intent();
                        intent.putExtra("title", txt_title.getText().toString());
                        intent.putExtra("body", txt_body.getText().toString());
                        intent.putExtra("due_date",txt_date.getText().toString());
                        intent.putExtra("color",txt_color.getText().toString());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        break;
                    }

                }
                else if(type==1){
                    //editing...
                    Intent intent = new Intent();
                    intent.putExtra("title",txt_title.getText().toString());
                    intent.putExtra("body",txt_body.getText().toString());
                    intent.putExtra("due_date",txt_date.getText().toString());
                    intent.putExtra("last_edited_date", Utilities.getCurrentDate());
                    intent.putExtra("color",txt_color.getText().toString());
                    intent.putExtra("position",position);
                    setResult(2,intent);
                    finish();
                    break;
                }
            case R.id.menu_delete:
                if(type==1) {
                    Intent intent = new Intent();
                    intent.putExtra("title", txt_title.getText().toString());
                    intent.putExtra("body", txt_body.getText().toString());
                    intent.putExtra("due_date",txt_date.getText().toString());
                    intent.putExtra("color",txt_color.getText().toString());
                    intent.putExtra("position", position);
                    setResult(3, intent);
                    finish();
                    break;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validateForm(){
        String title = txt_title.getText().toString();
        String body = txt_body.getText().toString();
        if(!title.isEmpty()&&!body.isEmpty()) {

            return true;
        }
        Toast.makeText(this,"Fill the form!",Toast.LENGTH_SHORT).show();
        return false;
    }
}
