package hu.bme.andrismulller.makeithappen_withfriends.Functions.Wallet;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import hu.bme.andrismulller.makeithappen_withfriends.MyUtils.Constants;
import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.WalletItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewWalletDialogFragment extends DialogFragment {

	private static final String TAG = "NewWalletDialogFragment";
	EditText descriptionET;
    EditText valueET;
    Spinner categorySpinner;
    OnWalletItemAddedListener onWalletItemAddedListener;

    public interface OnWalletItemAddedListener{
        void onWalletItemAdded(WalletItem walletItem);
    }

    boolean bevetel;

    public NewWalletDialogFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bevetel = getArguments().getBoolean("bevetel");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.wallet_page_title))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    	try {
                    		int value = Integer.parseInt(valueET.getText().toString());
		                    Log.i(TAG, "value: " + value);

		                    WalletItem walletItem = new WalletItem(descriptionET.getText().toString(), bevetel,
				                    Integer.parseInt(valueET.getText().toString()), Constants.FT,
				                    categorySpinner.getSelectedItem().toString(), Calendar.getInstance().getTimeInMillis());
		                    walletItem.setId(walletItem.save());

		                    dialogInterface.dismiss();

		                    onWalletItemAddedListener.onWalletItemAdded(walletItem);

	                    } catch (NumberFormatException nfe) {
	                    	nfe.printStackTrace();
		                    Toast.makeText(getContext(), getString(R.string.too_high_value), Toast.LENGTH_LONG).show();
	                    } catch (Exception e) {
		                    e.printStackTrace();
		                    Toast.makeText(getContext(), getString(R.string.too_high_value), Toast.LENGTH_LONG).show();
	                    }
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_new_wallet, null);

        descriptionET = view.findViewById(R.id.wallet_item_description_et);
        valueET = view.findViewById(R.id.wallet_item_value_et);
        categorySpinner = view.findViewById(R.id.wallet_item_category_spinner);
        ArrayAdapter<CharSequence> adapter;
        if (bevetel){
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.wallet_item_income_category, android.R.layout.simple_spinner_item);
        } else {
            adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.wallet_item_expense_category, android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.onWalletItemAddedListener = (OnWalletItemAddedListener) context;
    }
}
