package hu.bme.andrismulller.makeithappen_withfriends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Muller Andras on 9/16/2017.
 */

public class TipsDialogFragment extends AppCompatDialogFragment {
    String tips;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tips = getString(R.string.useful_tips_for_todos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.tips_fragment_title);

        View view = inflater.inflate(R.layout.dialogfragment_tips, container, false);

        TextView textView = view.findViewById(R.id.tips_fragment_textview);
        textView.setText(tips);

        return view;
    }
}
