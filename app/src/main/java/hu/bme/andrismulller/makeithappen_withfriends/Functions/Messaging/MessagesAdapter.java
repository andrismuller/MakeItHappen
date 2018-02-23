package hu.bme.andrismulller.makeithappen_withfriends.Functions.Messaging;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.MyMessage;

/**
 * Created by Muller Andras on 12/4/2017.
 */

public class MessagesAdapter extends FirebaseRecyclerAdapter<MyMessage, MessageHolder> {

    /**
     * Initialize a RecyclerView.Adapter that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MessagesAdapter(FirebaseRecyclerOptions<MyMessage> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(MessageHolder holder, int position, MyMessage message) {
        if (message.getMessage() != null) {
            holder.messageTextView.setText(message.getMessage());

            if (!message.getToUser().equals("me")) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.messageTextView.getLayoutParams();
                layoutParams.gravity = Gravity.RIGHT;
                holder.messageTextView.setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent, false);
        return new MessageHolder(view);
    }
}
