package hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;

public class ControllingFragment extends Fragment {
    private static final String TAG = "ControllingFragment";

    private TextView infoTextView;
    private FloatingActionButton addControllingFab;
    private RecyclerView controllingRecyclerView;
    private ControllingAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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

        infoTextView = view.findViewById(R.id.controlling_information_textview);
        addControllingFab = view.findViewById(R.id.new_controlling_action_button);
        addControllingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNewControllingListener.onNewControlling();
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
        adapter.onControllingAdded(controlling);
    }

}
