package com.example.utente.todolistapp.controllers;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.utente.todolistapp.activities.MainActivity;
import com.example.utente.todolistapp.adapters.NoteCardAdapter;
import com.example.utente.todolistapp.models.Note;
import com.example.utente.todolistapp.models.State;

import java.util.Collections;

/**
 * Created by Utente on 01/03/2017.
 */

public class MyTouchHelper extends ItemTouchHelper.Callback {
    NoteCardAdapter mAdapter;
    Context context;

    public MyTouchHelper(NoteCardAdapter mAdapter, Context context){
        this.mAdapter = mAdapter;
        this.context = context;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN|ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
        //return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.DOWN|ItemTouchHelper.UP|ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT|swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        /*if (MainActivity.mActionMode != null) {
            MainActivity.mActionMode.finish();

        }*/
        System.out.println("..............");
        final int fromPos = viewHolder.getAdapterPosition();
        final int toPos = target.getAdapterPosition();
        // move item in `fromPos` to `toPos` in adapter.

        if(fromPos<toPos){
            for (int i = fromPos; i < toPos; i++) {
                Collections.swap(mAdapter.getDataSet(),i,i+1);
            }

        } else {
            for (int i = fromPos; i > toPos; i--) {
                Collections.swap(mAdapter.getDataSet(),i,i-1);
            }
        }
        mAdapter.notifyItemMoved(fromPos,toPos);
        return true;// true if moved, false otherwise
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


        final Note note = mAdapter.getDataSet().get(viewHolder.getAdapterPosition());
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
        //viewHolder.itemView.txt.setText(newState.getDescription());
        //((MainActivity)txt_state.getContext()).db.updateNote(note);
        ///CoordinatorLayout layout = (CoordinatorLayout) v.getRootView().findViewById(R.id.cl_main);
        Snackbar.make(viewHolder.itemView,"Marked as"+support,Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setState(currentState);
            }
        }).show();
    }
}
