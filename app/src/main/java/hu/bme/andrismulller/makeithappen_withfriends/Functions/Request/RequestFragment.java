package hu.bme.andrismulller.makeithappen_withfriends.Functions.Request;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import hu.bme.andrismulller.makeithappen_withfriends.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        RequestListAdapter requestListAdapter = new RequestListAdapter(getContext());
        ListView requestListView = view.findViewById(R.id.request_listview);
        requestListView.setAdapter(requestListAdapter);

        TextView infoTV = view.findViewById(R.id.request_information_tv);
        if (requestListAdapter.getCount() > 0) {
            infoTV.setText(requestListAdapter.getCount() + getString(R.string.requestsYouHave));
        } else {
            infoTV.setText(getString(R.string.no_requests));
        }

        return view;
    }

}
