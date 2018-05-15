package hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;

public class ControllingFragment extends Fragment {
    private static final String TAG = "ControllingFragment";

    private TextView infoTextView;
    private FloatingActionButton addControllingFab;
    private RecyclerView controllingRecyclerView;
    private ControllingAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
	private ProgressBar newControllingProgressBar;

	OnNewControllingListener onNewControllingListener;

    public interface OnNewControllingListener{
        void onNewControlling();
    }
    public static ControllingFragment newInstance(){
        ControllingFragment fragment = new ControllingFragment();
        return fragment;
    }

    public ControllingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_controlling, container, false);

        newControllingProgressBar = view.findViewById(R.id.new_controlling_progressbar);
        addControllingFab = view.findViewById(R.id.new_controlling_action_button);
        addControllingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	if (newControllingProgressBar.getVisibility() == View.GONE){
		            newControllingProgressBar.setVisibility(View.VISIBLE);
		            onNewControllingListener.onNewControlling();
	            } else {
		            Toast.makeText(getContext(), getString(R.string.wait_for_dialog), Toast.LENGTH_SHORT).show();
	            }
            }
        });
        controllingRecyclerView = view.findViewById(R.id.controlling_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        controllingRecyclerView.setLayoutManager(layoutManager);
        adapter = new ControllingAdapter(getContext());
        controllingRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onNewControllingListener = (OnNewControllingListener) context;
    }

    public void update(Controlling controlling){
    	if (controlling != null)
            adapter.onControllingAdded(controlling);

        newControllingProgressBar.setVisibility(View.GONE);
    }

	@Override
	public void onResume() {
		super.onResume();
		newControllingProgressBar.setVisibility(View.GONE);
	}
}
