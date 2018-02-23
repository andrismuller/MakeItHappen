package hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.App;

/**
 * Created by Muller Andras on 9/24/2017.
 */

public class AppSpinnerAdapter extends BaseAdapter {
    List<App> apps = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public AppSpinnerAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
        getAppsInstalled();
    }
    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int i) {
        return apps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return apps.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_item_app_controlling, null);
        ImageView icon = view.findViewById(R.id.app_icon_imageview);
        TextView names = view.findViewById(R.id.app_label_textview);
        icon.setImageDrawable(apps.get(i).getIcon());
        names.setText(apps.get(i).getLabel());
        return view;
    }

    private void getAppsInstalled(){
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
//            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
                String appPackageName = p.applicationInfo.packageName;
                Drawable icon = p.applicationInfo.loadIcon(context.getPackageManager());
                apps.add(new App(appName, appPackageName, icon));
//            }
        }
    }
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }
}
