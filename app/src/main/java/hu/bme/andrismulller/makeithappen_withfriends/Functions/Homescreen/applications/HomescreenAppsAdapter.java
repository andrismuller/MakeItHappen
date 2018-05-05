package hu.bme.andrismulller.makeithappen_withfriends.Functions.Homescreen.applications;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.ApplicationInfo;

/**
 * Created by Muller Andras on 9/27/2017.
 */

public class HomescreenAppsAdapter extends RecyclerView.Adapter<HomescreenAppsAdapter.ViewHolder> {

    private Activity activity;
    private final List<ApplicationInfo> homescreenAppList;
    private LayoutInflater mInflater;

    public HomescreenAppsAdapter(Activity activity, List<ApplicationInfo> applications) {
        this.mInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.homescreenAppList = applications;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_app_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ApplicationInfo app = homescreenAppList.get(position);
        holder.nameTV.setText(app.getTitle());
        holder.iconIV.setImageDrawable(app.getIcon());

        holder.applicationsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(app.getIntent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return homescreenAppList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout applicationsLL;
        public TextView nameTV;
        public ImageView iconIV;

        public ViewHolder(View itemView) {
            super(itemView);
            applicationsLL = itemView.findViewById(R.id.applicationLL);
            nameTV = itemView.findViewById(R.id.nameTV);
            iconIV = itemView.findViewById(R.id.iconIV);
        }
    }
}
