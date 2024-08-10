package com.example.dury.Adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dury.Model.Note;
import com.example.dury.R;

import java.util.ArrayList;
public class NoteAdapter extends ArrayAdapter {

    private ArrayList<Note> listNotes;
    private Context context;

    public NoteAdapter(Context context, ArrayList<Note> listNotes) {
        super(context, R.layout.activity_note_adapter_layout, listNotes);
        this.context = context;
        this.listNotes = listNotes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_note_adapter_layout, null);
        }

        Note note = listNotes.get(position);

        TextView titleTextView = convertView.findViewById(R.id.txtViewNoteAdapterTitle);

        titleTextView.setText(note.getTitle());

        return convertView;
    }
}
