package com.example.utente.todolistapp.adapters;

import android.content.Intent;
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

    }
    public int getItemCount(){
        return dataSet.size();
    }

    public class NoteCardVH extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        TextView txt_title, txt_body;
        public NoteCardVH(View itemView){
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            txt_title = (TextView) itemView.findViewById(R.id.title_note);
            txt_body = (TextView) itemView.findViewById(R.id.body_note);
        }



        @Override
        public boolean onLongClick(View v) {

            return true;
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(),AddNoteActivity.class);
            intent.putExtra("title",txt_title.getText().toString());
            intent.putExtra("body",txt_body.getText().toString());
            intent.putExtra("position",String.valueOf(getAdapterPosition()));
            ((AppCompatActivity)v.getContext()).startActivityForResult(intent,1);

        }
    }
}
