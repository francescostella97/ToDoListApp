package com.example.utente.todolistapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
    public static boolean flag = false;
    public static int count = 0;
    Context context;
    public NoteCardAdapter(Context context){
        this.context = context;
    }
    ArrayList<Note> dataSet = new ArrayList<>();

    public void addNote(Note note){
        if(getItemCount()!=0) {
            dataSet.add(getItemCount(), note);
            notifyItemInserted(getItemCount());
        }
        else {
            dataSet.add(0, note);
            notifyItemInserted(0);
        }
    }
    public void addAtTop(Note note){
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
        //crea layout degli item nell rv
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card,parent,false);
        return new NoteCardVH(view);
    }

    public void onBindViewHolder(final NoteCardAdapter.NoteCardVH holder, int position){
        final Note note = dataSet.get(position);

        if(note.getTitle().equals("")) {
            holder.txt_title.setVisibility(View.GONE);
            holder.txt_title.setText("");
        }
        else {
            holder.txt_title.setVisibility(View.VISIBLE);
            holder.txt_title.setText(note.getTitle());
        }
        if(note.getBody().equals("")) {
            holder.txt_body.setVisibility(View.GONE);
            holder.txt_body.setText("");

        }
        else{
            holder.txt_body.setVisibility(View.VISIBLE);
            holder.txt_body.setText(note.getBody());
        }

        holder.txt_due_date.setText(note.getDueDate());
        holder.txt_edited_date.setText(note.getLastEditDate());
        holder.txt_color.setText(""+note.getColor());
        holder.txt_state.setText(String.valueOf(note.getState().getDescription()));
        holder.txt_body.getRootView().setBackgroundColor(note.getColor());
        holder.txt_body.getRootView().getBackground().setAlpha(150);

        holder.txt_isSpecial.setText(""+note.isSpecial());
        if(holder.txt_isSpecial.getText().toString().equals("true")){
            holder.btn_special.setVisibility(View.VISIBLE);
            holder.btn_special.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bookmark_white_24dp,0,0,0);

            holder.btn_special.setDrawingCacheBackgroundColor(Color.BLUE);
        }
        else {
            holder.btn_special.setVisibility(View.GONE);
        }

        if(note.isSelected()){
            GradientDrawable drawableSelected = new GradientDrawable();

            drawableSelected.setShape(GradientDrawable.RECTANGLE);
            drawableSelected.setStroke(10, holder.btn_special.getContext().getResources().getColor(R.color.mdtp_light_gray));

            drawableSelected.setColor(note.getColor());
            drawableSelected.setAlpha(180);
            holder.btn_special.getRootView().setBackgroundColor(note.getColor());

            holder.btn_special.getRootView().setBackgroundDrawable(drawableSelected);
            drawableSelected.setCornerRadius(5);

        }else{
            GradientDrawable drawableSelectedDeselected = new GradientDrawable();
            drawableSelectedDeselected.setShape(GradientDrawable.RECTANGLE);


            drawableSelectedDeselected.setColor(note.getColor());
            drawableSelectedDeselected.setAlpha(150);
            holder.btn_special.getRootView().setBackgroundColor(note.getColor());

            holder.btn_special.getRootView().setBackgroundDrawable(drawableSelectedDeselected);
            drawableSelectedDeselected.setCornerRadius(5);
        }
    }
    public int getItemCount(){
        return dataSet.size();
    }

    public void deselectAllNotes(){
        for(Note n : dataSet){
            if(n.isSelected()) {
                n.setSelected(false);
            }
        }
        notifyDataSetChanged();
    }
    public class NoteCardVH extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        //final ActionMode mActionMode;
        ActionMode.Callback callback;
        TextView txt_edited_date, txt_state;
        TextView txt_isSpecial; Button btn_special;
        TextView txt_title, txt_body,txt_due_date,txt_color;
        View view;

        public NoteCardVH(View itemView){
            super(itemView);
            view = itemView;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

            //gestione UI
            txt_title = (TextView) itemView.findViewById(R.id.title_note);
            txt_body = (TextView) itemView.findViewById(R.id.body_note);
            txt_due_date = (TextView) itemView.findViewById(R.id.due_date_note);
            txt_edited_date = (TextView) itemView.findViewById(R.id.edited_date);
            txt_color = (TextView) itemView.findViewById(R.id.color_note);
            txt_isSpecial = (TextView) itemView.findViewById(R.id.isSpecial_note);
            btn_special = (Button) itemView.findViewById(R.id.special_btn_note);
            txt_state = (TextView) itemView.findViewById(R.id.state_note);
        }

        @Override
        public boolean onLongClick(View v) {

            /*
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
            txt_state.setText(newState.getDescription());
            ((MainActivity)txt_state.getContext()).db.updateNote(note);
            ///CoordinatorLayout layout = (CoordinatorLayout) v.getRootView().findViewById(R.id.cl_main);
            Snackbar.make(v,"Marked as"+support,Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    note.setState(currentState);
                }
            }).show();
            System.out.println("PRESSED ---> "+note);
            //((MainActivity)txt_title.getContext()).noteCardAdapter.notifyItemChanged(getAdapterPosition());
            return true;*/
            if(flag==false) {
                count++;
                flag = true;
                Note note = dataSet.get(getAdapterPosition());
                note.setSelected(!note.isSelected());
                notifyItemChanged(getAdapterPosition());
                System.out.println("Schede selezionate " + count + " " + flag);
                //calling action mode in main activity
                MainActivity.mActionMode = ((MainActivity) context).mActionMode;
                callback = ((MainActivity) context).mActionModeCallback;
                if (MainActivity.mActionMode != null) {
                    MainActivity.mActionMode.setTitle("" + count);
                    return false;
                }
                MainActivity.mActionMode = ((MainActivity) v.getContext()).startSupportActionMode(callback);

                MainActivity.mActionMode.setTitle("" + count);
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            System.out.println("Schede selezionate "+count+ " "+flag+ " before");
            if(!flag) {
                Intent intent = new Intent(v.getContext(), AddNoteActivity.class);
                intent.putExtra("title", txt_title.getText().toString());
                intent.putExtra("body", txt_body.getText().toString());
                intent.putExtra("due_date", txt_due_date.getText().toString());
                intent.putExtra("last_edited_date", txt_edited_date.getText().toString());
                intent.putExtra("color", txt_color.getText().toString());
                intent.putExtra("position", String.valueOf(getAdapterPosition()));
                intent.putExtra("isSpecial", txt_isSpecial.getText().toString());
                System.out.println("activity intent -> state "+txt_state.getText().toString());
                intent.putExtra("state", txt_state.getText().toString());
                ((AppCompatActivity) v.getContext()).startActivityForResult(intent, 1);
            } else{

                Note note =  dataSet.get(getAdapterPosition());
                note.setSelected(!note.isSelected());
                if(note.isSelected())
                    count++;
                else count --;
                if(MainActivity.mActionMode!=null) MainActivity.mActionMode.setTitle(""+count);
                if(count == 0) {
                    if(MainActivity.mActionMode!=null){ MainActivity.mActionMode.finish();}
                    flag = false;
                }
                notifyItemChanged(getAdapterPosition());
            }
        }
    }
}