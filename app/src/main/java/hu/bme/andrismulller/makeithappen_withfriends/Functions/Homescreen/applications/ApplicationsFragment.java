package hu.bme.andrismulller.makeithappen_withfriends.Functions.Homescreen.applications;


import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.ApplicationInfo;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;

public class ApplicationsFragment extends Fragment {

    private List<ApplicationInfo> homescreenApps;
    private Controlling controlling;
    private String[] appsBlocked;

    HomescreenAppsAdapter homescreenAppsAdapter;

    public static ApplicationsFragment newInstance(long id) {

        Bundle args = new Bundle();
        args.putLong("id", id);
        ApplicationsFragment fragment = new ApplicationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ApplicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = getArguments().getLong("id");
	    controlling = Controlling.findById(Controlling.class, id);
	    if (controlling != null){
            getApps();
        } else
            appsBlocked = null;

        loadApplications();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applications, container, false);

        RecyclerView homescreenAppsRV = view.findViewById(R.id.homescreen_app_RV);
        homescreenAppsRV.setLayoutManager(new GridLayoutManager(getContext(), 4));
        homescreenAppsAdapter = new HomescreenAppsAdapter(getActivity(), homescreenApps);
        homescreenAppsRV.setAdapter(homescreenAppsAdapter);

        return view;
    }

    private void loadApplications() {
        PackageManager manager = getActivity().getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

        if (apps != null) {
            final int count = apps.size();

            if (homescreenApps == null) {
                homescreenApps = new ArrayList<ApplicationInfo>(count);
            }
            homescreenApps.clear();

            for (int i = 0; i < count; i++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = apps.get(i);

                if (!isAppInList(info.activityInfo.applicationInfo.packageName) && !info.loadLabel(manager).equals("Settings")){
                    application.setTitle(info.loadLabel(manager));
                    application.setActivity(
                            new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name),
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    application.setIcon(info.activityInfo.loadIcon(manager));

                    homescreenApps.add(application);
                }
            }
        }
    }

    private void getApps(){
        appsBlocked = controlling.getApps().split(";");
    }

    private boolean isAppInList(String packageName){
        if (appsBlocked != null && appsBlocked.length > 0) {
            for (String app : appsBlocked) {
                if (packageName.equals(app)) {
                    return true;
                }
            }
        }
        return false;
    }

	@Override
	public void onResume() {
		super.onResume();

		controlling = Controlling.findById(Controlling.class, getArguments().getLong("id"));

		if (controlling != null && controlling.isActive()){
			if (appsBlocked == null) {
				getApps();
				loadApplications();
				homescreenAppsAdapter.update(homescreenApps);
			}
		} else {
			if (appsBlocked != null) {
				appsBlocked = null;
				loadApplications();
				homescreenAppsAdapter.update(homescreenApps);
			}
		}
	}
}
