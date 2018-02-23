package hu.bme.andrismulller.makeithappen_withfriends.Functions.Todo;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import hu.bme.andrismulller.makeithappen_withfriends.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {

    ListView todoListView;
    TodoListAdapter todoListAdapter;

    OnNewTodoListener onNewTodoListener;

    public interface OnNewTodoListener{
        void onNewTodo();
    }

    public static TodoFragment newInstance() {
        TodoFragment fragment = new TodoFragment();
        return fragment;
    }

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoListAdapter = new TodoListAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        todoListView = view.findViewById(R.id.todo_listView);
        todoListView.setAdapter(todoListAdapter);

        FloatingActionButton fab = view.findViewById(R.id.todo_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewTodoListener.onNewTodo();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onNewTodoListener = (OnNewTodoListener) context;
    }

    public void update(){
        todoListAdapter.onTodoAdded();
    }
}
