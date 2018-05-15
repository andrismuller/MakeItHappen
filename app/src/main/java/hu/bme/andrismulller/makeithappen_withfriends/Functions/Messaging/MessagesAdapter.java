package hu.bme.andrismulller.makeithappen_withfriends.Functions.Messaging;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.Profile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.MyMessage;

/**
 * Created by Muller Andras on 12/4/2017.
 */

public class MessagesAdapter extends FirebaseRecyclerAdapter<MyMessage, MessageHolder> {

	Context context;

    /**
     * Initialize a RecyclerView.Adapter that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MessagesAdapter(FirebaseRecyclerOptions<MyMessage> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(MessageHolder holder, int position, MyMessage message) {
        if (message.getMessage() != null) {
            holder.messageTextView.setText(message.getMessage());

            if (!message.getToUser().equals(Profile.getCurrentProfile().getId())) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.messageTextView.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.messageTextView.setLayoutParams(layoutParams);
                holder.itemView.setBackground(context.getDrawable(R.drawable.main_item_background));
            } else {
	            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.messageTextView.getLayoutParams();
	            layoutParams.gravity = Gravity.LEFT;
	            holder.messageTextView.setLayoutParams(layoutParams);
	            holder.itemView.setBackground(context.getDrawable(R.drawable.low_priority_background));
            }
        }
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent, false);
        return new MessageHolder(view);
    }
}
