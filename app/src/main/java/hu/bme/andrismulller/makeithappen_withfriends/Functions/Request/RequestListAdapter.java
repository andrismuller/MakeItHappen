package hu.bme.andrismulller.makeithappen_withfriends.Functions.Request;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling.ShareControllingDialogFragment;
import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.FacebookUser;
import hu.bme.andrismulller.makeithappen_withfriends.model.MyMessage;
import hu.bme.andrismulller.makeithappen_withfriends.model.Note;

/**
 * Created by Muller Andras on 9/20/2017.
 */

public class RequestListAdapter extends BaseAdapter{

    private List<MyMessage> requests = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    private TextView typeTextView;
    private TextView messageTextView;
    private Button doItButton;
	private TextView fromTextView;

	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy. MMM dd. hh:mm");

	public RequestListAdapter(Context context){
        this.context = context;
        inflater = (LayoutInflater.from(context));
        try{
            requests = MyMessage.find(MyMessage.class, "is_request = ?", "1");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Override
    public Object getItem(int i) {
        return requests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return requests.get(i).hashCode();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_item_request, null);

        String[] parameters = requests.get(i).getMessage().split(Constants.DELIMITER_REQUEST);

        fromTextView = view.findViewById(R.id.request_from_tv);
        fromTextView.setText(context.getString(R.string.from_user) + parameters[ShareControllingDialogFragment.REQUEST_PARAM_NAME_FROM]);
        typeTextView = view.findViewById(R.id.request_type_tv);
        if (parameters[ShareControllingDialogFragment.REQUEST_PARAM_TYPE].equals(Constants.REQUEST_TYPE_CALL)) {
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTimeInMillis(Long.parseLong(parameters[ShareControllingDialogFragment.REQUEST_PARAM_TIME]));
	        typeTextView.setText(context.getString(R.string.call_back_your_friend_at) + simpleDateFormat.format(calendar.getTime()));
        }
        messageTextView = view.findViewById(R.id.request_message_tv);
        messageTextView.setText(parameters[0]);
        if (parameters[1].equals(Constants.REQUEST_TYPE_CALL)){

        }
        doItButton = view.findViewById(R.id.request_do_action);
        doItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
	            Toast.makeText(context, context.getString(R.string.not_implemented), Toast.LENGTH_LONG).show();
            }
        });

        if (requests.get(i).getToUser().equals(Profile.getCurrentProfile().getId())){
        	view.setBackground(context.getDrawable(R.drawable.high_priority_background));
        } else {
        	view.setBackground(context.getDrawable(R.drawable.main_item_background));
        }

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

    public void addRequest(MyMessage message) {
		requests.add(message);
		notifyDataSetChanged();
	}

	public void removeRequest(MyMessage message) {
		requests.remove(message);
		notifyDataSetChanged();
	}
}
