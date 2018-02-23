package hu.bme.andrismulller.makeithappen_withfriends.Functions.Todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Todo;

/**
 * Created by Muller Andras on 9/19/2017.
 */

public class TodoListAdapter extends BaseAdapter{
    private static List<Todo> todos = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private TextView titleTextView;
    private TextView requiredTextView;
    private TextView descriptionTextView;
    private ImageButton deleteImgButton;
    private ImageButton editImgButton;
    private CheckBox doneCheckBox;

    public TodoListAdapter(Context applicationContext) {
        this.context = applicationContext;
        layoutInflater = (LayoutInflater.from(applicationContext));
//        for (int i = 0; i < todos.size(); ++i){
//            todos.get(i).delete();
//        }
        try {
            todos = Todo.listAll(Todo.class);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    @Override
    public int getCount() {
        return todos.size();
    }

    @Override
    public Object getItem(int i) {
        return todos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return todos.get(i).hashCode();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.list_item_todo, null);

        titleTextView = view.findViewById(R.id.todoItem_title_textView);
        titleTextView.setText(todos.get(i).getTitle());
        descriptionTextView = view.findViewById(R.id.todoItem_description_textView);
        descriptionTextView.setText(todos.get(i).getDescription());
        requiredTextView = view.findViewById(R.id.todoItem_required_textView);
        requiredTextView.setText(todos.get(i).getRequiredInformation());

        doneCheckBox = view.findViewById(R.id.todo_checkbox);
        doneCheckBox.setChecked(todos.get(i).isDone());

        deleteImgButton = view.findViewById(R.id.todoItem_delete_imgButton);
        deleteImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DBHelper dbHelper = new DBHelper(context);
//                dbHelper.deleteTodo(todos.get(i).getId());
//                Todo todo = Todo.findById(Todo.class, todos.get(i).getMyId());
                todos.get(i).delete();

                todos.remove(i);
                notifyDataSetChanged();
            }
        });
        editImgButton = view.findViewById(R.id.todoItem_edit_imgButton);

        return view;
    }

    public void onTodoAdded(){
        todos = Todo.listAll(Todo.class);
        notifyDataSetChanged();
    }
}
