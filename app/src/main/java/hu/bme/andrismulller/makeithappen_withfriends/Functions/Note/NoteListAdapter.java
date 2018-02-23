package hu.bme.andrismulller.makeithappen_withfriends.Functions.Note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Note;

/**
 * Created by Muller Andras on 9/20/2017.
 */

public class NoteListAdapter extends BaseAdapter{

    private List<Note> notes = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    private TextView dateTextView;
    private TextView noteTextView;
    private ImageButton deleteNoteButton;

    public NoteListAdapter(Context context){
        this.context = context;
        inflater = (LayoutInflater.from(context));
        try{
            notes = Note.listAll(Note.class);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return notes.get(i).hashCode();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_item_note, null);

        dateTextView = view.findViewById(R.id.note_listitem_datetv);
        dateTextView.setText(getDate(notes.get(i).getTimeCreated()));
        noteTextView = view.findViewById(R.id.note_listitem_notetv);
        noteTextView.setText(notes.get(i).getNote());

        deleteNoteButton = view.findViewById(R.id.note_listitem_deletebutton);
        deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notes.get(i).delete();

                notes.remove(i);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private String getDate(long milliSeconds)
    {
        String dateFormat = "yyyy.mm.dd. hh:mm";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void addNote(Note note){
        notes.add(note);
        notifyDataSetChanged();
    }
}
