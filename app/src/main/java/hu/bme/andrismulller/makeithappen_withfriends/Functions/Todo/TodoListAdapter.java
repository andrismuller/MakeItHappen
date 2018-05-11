package hu.bme.andrismulller.makeithappen_withfriends.Functions.Todo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm.AlarmFragment;
import hu.bme.andrismulller.makeithappen_withfriends.Functions.Alarm.AlarmReceiver;
import hu.bme.andrismulller.makeithappen_withfriends.MainActivity;
import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Alarm;
import hu.bme.andrismulller.makeithappen_withfriends.model.Todo;

/**
 * Created by Muller Andras on 9/19/2017.
 */

public class TodoListAdapter extends BaseAdapter{
	private static final String TAG = "TodoListAdapter";
	private static List<Todo> todos = new ArrayList<>();
	private final AlarmFragment.OnNewAlarmListener onNewAlarmListener;
	private Context context;

    private LayoutInflater layoutInflater;
    private TextView titleTextView;
    private TextView requiredTextView;
    private TextView descriptionTextView;
    private ImageButton deleteImgButton;
    private ImageButton editImgButton;
    private CheckBox doneCheckBox;
	Button setAlarmButton;


	String[] todoPriorityArray;
	String[] todoCategoryArray;

	public TodoListAdapter(Context applicationContext) {
        this.context = applicationContext;
        layoutInflater = (LayoutInflater.from(applicationContext));
        try {
            todos = Todo.listAll(Todo.class);
        } catch (Exception e){
            e.printStackTrace();
        }

		onNewAlarmListener = (AlarmFragment.OnNewAlarmListener) context;

		todoPriorityArray = context.getResources().getStringArray(R.array.newtodo_priority_array);
		todoCategoryArray = context.getResources().getStringArray(R.array.newtodo_category_array);
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

        setAlarmButton = view.findViewById(R.id.todo_set_alarm_button);
        if (todos.get(i).getAlarm_id() > 0) {
        	setAlarmButton.setText(context.getString(R.string.cancel_alarm));
        } else {
        	setAlarmButton.setText(context.getString(R.string.set_alarm));
        }
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	        	if (setAlarmButton.getText().toString().equals(context.getString(R.string.set_alarm))) {
			        if (Alarm.find(Alarm.class, "is_used = 0") != null && Alarm.find(Alarm.class, "is_used = 0").get(0) != null)
				        onNewAlarmListener.onNewAlarm(todos.get(i).getId());
			        else {
				        Toast.makeText(context, context.getString(R.string.no_more_alarm), Toast.LENGTH_LONG).show();
			        }
		        } else if (setAlarmButton.getText().toString().equals(context.getString(R.string.cancel_alarm))){
	        		Alarm alarm = Alarm.findById(Alarm.class, todos.get(i).getAlarm_id());
	        		if (alarm != null){
	        			cancelAlarm(alarm);
	        			alarm.setRunning(false);
	        			alarm.setUsed(false);
	        			alarm.save();
			        }
			        todos.get(i).setAlarm_id(-1);
	        		todos.get(i).save();
			        setAlarmButton.setText(context.getString(R.string.set_alarm));

			        notifyDataSetChanged();
		        }
	        }
        });

        if (todos.get(i).getPriority().equals(todoPriorityArray[2])){
        	view.setBackground(context.getDrawable(R.drawable.high_priority_background));
        } else if (todos.get(i).getPriority().equals(todoPriorityArray[1])){
        	view.setBackground(context.getDrawable(R.drawable.medium_priority_background));
        } else {
	        view.setBackground(context.getDrawable(R.drawable.low_priority_background));
        }

        doneCheckBox = view.findViewById(R.id.todo_checkbox);
        doneCheckBox.setChecked(todos.get(i).isDone());

        deleteImgButton = view.findViewById(R.id.todoItem_delete_imgButton);
        deleteImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    public void sortTodos(String filter) {
    	String[] filterArray = context.getResources().getStringArray(R.array.todo_filters);

	    if (filter.equals(filterArray[1])){
	        Collections.sort(todos, new Comparator<Todo>() {
		        @Override
		        public int compare(Todo t1, Todo t2) {
			        if (t1.getPriority().equals(t2.getPriority()))
			            return 0;
			        else if (t1.getPriority().equals(todoPriorityArray[2]) || t2.getPriority().equals(todoPriorityArray[0]))
				        return 1;
			        else
				        return -1;
		        }
	        });
	        notifyDataSetChanged();
        } else if (filter.equals(filterArray[2])){
	        Collections.sort(todos, new Comparator<Todo>() {
		        @Override
		        public int compare(Todo todo, Todo t1) {
			        if (todo.getTodoCategory().equals(t1.getTodoCategory()))
				        return 0;
			        else if (todo.getTodoCategory().equals(todoCategoryArray[0]))
				        return 1;
			        else
				        return -1;
		        }
	        });
	        notifyDataSetChanged();
        } else if (filterArray.equals(filterArray[3])){
	        Toast.makeText(context, context.getString(R.string.not_implemented), Toast.LENGTH_LONG).show();
        } else{
	        Toast.makeText(context, context.getString(R.string.not_implemented), Toast.LENGTH_LONG).show();
        }
    }

	public void alarmAdded(Alarm alarm, long id) {
    	Todo todo = Todo.findById(Todo.class, id);
    	if (todo != null){
    		Alarm mAlarm = Alarm.find(Alarm.class, "is_used = 0").get(0);
		    if (mAlarm != null){
			    mAlarm.setUsed(true);
			    mAlarm.setRunning(true);
			    mAlarm.setMyId(alarm.getMyId());
			    mAlarm.setTime(alarm.getTime());
			    mAlarm.setNote(alarm.getNote() + "\n" + todo.getDescription());
			    mAlarm.setType(Constants.ALARM_TYPE_TODO);
			    mAlarm.save();

			    startAlarm(mAlarm);
			    todo.setAlarm_id(mAlarm.getId());
			    todo.save();
			    todos = Todo.listAll(Todo.class);
		    }

		    notifyDataSetChanged();
	    }
	}

	private void startAlarm(Alarm alarm){
		Intent myIntent = new Intent(context, AlarmReceiver.class);
		myIntent.putExtra("note", alarm.getNote());
		myIntent.putExtra("id", alarm.getId());
		myIntent.putExtra("type", "alarm");
		Log.d(TAG, "extras: " + alarm.getNote() + ", " + alarm.getId() + "," + "alarm");
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarm.getUniqueID() , myIntent, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intentMain = new Intent(context, MainActivity.class);
		PendingIntent pendingIntentMain = PendingIntent.getActivity(context, 0, intentMain, 0);
		alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarm.getTime(), pendingIntentMain), alarmIntent);

		Snackbar mySnackbar = Snackbar.make(((Activity)context).findViewById(R.id.fragment_todo),
				R.string.alarm_set, Snackbar.LENGTH_SHORT);
		mySnackbar.show();
	}

	private void cancelAlarm(Alarm alarm){
		Intent myIntent = new Intent(context, AlarmReceiver.class);
		myIntent.putExtra("note", alarm.getNote());
		myIntent.putExtra("id", alarm.getId());
		myIntent.putExtra("type", "alarm");
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context, alarm.getUniqueID() , myIntent, PendingIntent.FLAG_ONE_SHOT);

		alarmIntent.cancel();

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(alarmIntent);

		Snackbar mySnackbar = Snackbar.make(((Activity)context).findViewById(R.id.fragment_todo),
				R.string.alarm_cancelled, Snackbar.LENGTH_SHORT);
		mySnackbar.show();
	}

	public void refreshTodos() {
		try {
			todos = Todo.listAll(Todo.class);
			notifyDataSetChanged();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
