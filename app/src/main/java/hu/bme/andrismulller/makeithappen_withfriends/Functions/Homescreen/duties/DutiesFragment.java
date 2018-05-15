package hu.bme.andrismulller.makeithappen_withfriends.Functions.Homescreen.duties;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import hu.bme.andrismulller.makeithappen_withfriends.Functions.Controlling.ControllingFragment;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.Controlling;

public class DutiesFragment extends Fragment {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MMM dd. hh:mm");

    public DutiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_duties, container, false);

	    Controlling controlling;
	    TextView dutiesTV = view.findViewById(R.id.duties_tv);
	    TextView dutiesControllingTV = view.findViewById(R.id.duties_controlling_tv);

	    if (Controlling.find(Controlling.class, "is_active = 1") != null && Controlling.find(Controlling.class, "is_active = 1").size() > 0){
	    	controlling = Controlling.find(Controlling.class, "is_active = 1").get(0);
		    dutiesTV.setText(getString(R.string.you_have_an_active_controlling));
			dutiesControllingTV.setText(getString(R.string.your_aim) + controlling.getName() + "\n"
										+ getString(R.string.is_over) + dateFormat.format(new Date(controlling.getStartedTime()+controlling.getDurationTimeInSec()*1000)));
	    } else {
	    	dutiesTV.setText(getContext().getString(R.string.you_have_no_active_controlling));
	    }

        return view;
    }

}
