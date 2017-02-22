package com.example.utente.todolistapp.adapters;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utente.todolistapp.activities.AddNoteActivity;
import com.example.utente.todolistapp.activities.MainActivity;
import com.example.utente.todolistapp.models.Note;
import com.example.utente.todolistapp.R;
import com.example.utente.todolistapp.models.State;

import java.util.ArrayList;

/**
 * Created by Utente on 20/02/2017.
 */

public class NoteCardAdapter extends RecyclerView.Adapter<NoteCardAdapter.NoteCardVH> {
    ArrayList<Note> dataSet = new ArrayList<>();

    public void addNote(Note note){
        dataSet.add(0,note);
        notifyItemInserted(0);
    }
    public void removeNote(int position){
        dataSet.remove(position);
        notifyItemRemoved(position);
    }
    public Note getNote(int position){
        return dataSet.get(position);
    }
    public ArrayList<Note> getDataSet(){
        return new ArrayList(dataSet);
    }
    public ArrayList<Note> getDataSetByState(State state){
        ArrayList<Note> result = new ArrayList<>();
        for( Note n : dataSet){
            if(n.getState()==state){
                result.add(n);
            }
        }
        return result;
    }
    public void setDataSet(ArrayList<Note> dataSet){
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public NoteCardAdapter.NoteCardVH onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card,parent,false);
        return new NoteCardVH(view);
    }

    public void onBindViewHolder(NoteCardAdapter.NoteCardVH holder, int position){

        Note note = dataSet.get(position);
        holder.txt_title.setText(note.getTitle());
        holder.txt_body.setText(note.getBody());
        holder.txt_due_date.setText(note.getDueDate());
        holder.txt_color.setText(""+note.getColor());
        holder.txt_body.getRootView().setBackgroundColor(note.getColor());

    }
    public int getItemCount(){
        return dataSet.size();
    }

    public class NoteCardVH extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView txt_title, txt_body,txt_due_date,txt_color;
        public NoteCardVH(View itemView){
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            txt_title = (TextView) itemView.findViewById(R.id.title_note);
            txt_body = (TextView) itemView.findViewById(R.id.body_note);
            txt_due_date = (TextView) itemView.findViewById(R.id.due_date_note);
            txt_color = (TextView) itemView.findViewById(R.id.color_note);
        }



        @Override
        public boolean onLongClick(View v) {

            final Note note = dataSet.get(getAdapterPosition());
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
            Snackbar.make(v,"Marked as"+support,Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    note.setState(currentState);
                }
            }).show();

            return true;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(),AddNoteActivity.class);
            intent.putExtra("title",txt_title.getText().toString());
            intent.putExtra("body",txt_body.getText().toString());
            intent.putExtra("due_date",txt_due_date.getText().toString());
            intent.putExtra("color",txt_color.getText().toString());
            intent.putExtra("position",String.valueOf(getAdapterPosition()));
            ((AppCompatActivity)v.getContext()).startActivityForResult(intent,1);

        }
    }
}
