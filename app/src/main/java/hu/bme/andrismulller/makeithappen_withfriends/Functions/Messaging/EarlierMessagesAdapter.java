package hu.bme.andrismulller.makeithappen_withfriends.Functions.Messaging;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.MyMessage;

/**
 * Created by Muller Andras on 10/18/2017.
 */

public class EarlierMessagesAdapter extends RecyclerView.Adapter<EarlierMessagesAdapter.ViewHolder> {

    private static List<MyMessage> messages;

    public EarlierMessagesAdapter(String fromID, String meID){
//        messages = MyMessage.find(MyMessage.class, "from_user = ? or to_user = ?", fromID, fromID);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.messageTextView.setText(messages.get(position).getMessage());
        if (!messages.get(position).getToUser().equals("me")){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.messageTextView.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.messageTextView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(MyMessage message) {
        messages.add(message);
//        message.save();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_textview);
        }
    }

    public static ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            MyMessage message = dataSnapshot.getValue(MyMessage.class);
            messages.add(message);
//            message.save();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("The read failed: " + databaseError.getCode());
        }
    };
}
