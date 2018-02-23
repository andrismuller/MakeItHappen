package hu.bme.andrismulller.makeithappen_withfriends.Functions.Messaging;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.MainActivity;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.FacebookUser;

/**
 * Created by Muller Andras on 10/11/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private final Activity activity;
    private List<FacebookUser> friends = new ArrayList<>();
    private final MessagingFragment messagingFragment;
    public static int SEND_MESSAGE_REQUEST_CODE = 20;

    public FriendsAdapter(Activity activity, MessagingFragment messagingFragment){
        friends = FacebookUser.listAll(FacebookUser.class);
        this.activity = activity;
        this.messagingFragment = messagingFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.userName.setText(friends.get(position).getUserName());
        holder.iconImageView.setImageBitmap(loadImageFromStorage(friends.get(position).getFacebookId()));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconImageView;
        TextView userName;

        public ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.friend_icon);
            userName = itemView.findViewById(R.id.friend_username);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FragmentManager fm = messagingFragment.getActivity().getSupportFragmentManager();
            SendMessageDialogFragment sendMessageDialog = new SendMessageDialogFragment();
            sendMessageDialog.setName(friends.get(this.getAdapterPosition()).getUserName());
            sendMessageDialog.setUserID(friends.get(this.getAdapterPosition()).getFacebookId());
            sendMessageDialog.setTargetFragment(messagingFragment, SEND_MESSAGE_REQUEST_CODE);
            sendMessageDialog.show(fm, "send message");
        }
    }

    private Bitmap loadImageFromStorage(String userId) {
        Bitmap b = null;
        try {
            File f=new File(MainActivity.IMAGE_DIR_PATH, userId + "_pic.jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }
}
