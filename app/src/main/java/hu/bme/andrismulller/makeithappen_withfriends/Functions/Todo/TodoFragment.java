package hu.bme.andrismulller.makeithappen_withfriends.Functions.Todo;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Alarm;
import hu.bme.andrismulller.makeithappen_withfriends.model.Todo;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {

    ListView todoListView;
    TodoListAdapter todoListAdapter;
    Spinner todoFilterSpinner;

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
        todoFilterSpinner = view.findViewById(R.id.todo_filter_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.todo_filters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        todoFilterSpinner.setAdapter(adapter);
        todoFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
		        if (i != 0){
		        	sortTodos(todoFilterSpinner.getSelectedItem().toString());
		        	todoFilterSpinner.setSelection(0);
		        }
	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> adapterView) {

	        }
        });

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

    public void alarmAdded(Alarm alarm, long id){
    	todoListAdapter.alarmAdded(alarm, id);
    }

    private void sortTodos(String filter){
		todoListAdapter.sortTodos(filter);
    }

	@Override
	public void onResume() {
		super.onResume();
		todoListAdapter.refreshTodos();
	}
}
