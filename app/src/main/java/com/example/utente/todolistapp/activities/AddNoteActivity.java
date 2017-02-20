package com.example.utente.todolistapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utente.todolistapp.R;

/**
 * Created by Utente on 20/02/2017.
 */

public class AddNoteActivity extends AppCompatActivity {
    int type;
    String position;
    Button btnAddNote;
    EditText txt_title, txt_body;
    public void onStart(){
        super.onStart();
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

        Intent intent = getIntent();
        if(intent!=null){
            if(intent.getStringExtra("title")!=null){
                //editing...
                type = 1;
                position = intent.getStringExtra("position");
                txt_title.setText(intent.getStringExtra("title"));
                txt_body.setText(intent.getStringExtra("body"));
            }
        }



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
                    //adding...
                    if(validateForm()) {
                        Intent intent = new Intent();
                        intent.putExtra("title", txt_title.getText().toString());
                        intent.putExtra("body", txt_body.getText().toString());
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
