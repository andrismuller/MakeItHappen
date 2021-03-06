package hu.bme.andrismulller.makeithappen_withfriends.Functions.Wallet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.WalletItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNewWalletItemListener}
 * interface.
 */
public class WalletFragment extends Fragment {

    private int mColumnCount = 1;
    private List<WalletItem> walletItems;
    private MyWalletRecyclerViewAdapter mAdapter;

    private OnNewWalletItemListener mListener;

    TextView balanceTV;
    Button incomeExpenseButton;
    Button createNewItemButton;

    boolean bevetel;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WalletFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        walletItems = WalletItem.listAll(WalletItem.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        balanceTV = view.findViewById(R.id.wallet_fragment_balance_tv);

        incomeExpenseButton = view.findViewById(R.id.wallet_fragment_income_expense_button);
        incomeExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (incomeExpenseButton.getText().toString().equals(getString(R.string.income))){
                    incomeExpenseButton.setText(getString(R.string.expense));
                    bevetel = false;
                } else {
                    incomeExpenseButton.setText(getString(R.string.income));
                    bevetel = true;
                }
            }
        });
        createNewItemButton = view.findViewById(R.id.wallet_fragment_new_item_button);
        createNewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNewWalletItem(bevetel);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.list_wallet);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        mAdapter = new MyWalletRecyclerViewAdapter(walletItems);
        recyclerView.setAdapter(mAdapter);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewWalletItemListener) {
            mListener = (OnNewWalletItemListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewWalletItemListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void update(WalletItem walletItem) {
        walletItems.add(walletItem);
        mAdapter.update(walletItem);
    }

    public interface OnNewWalletItemListener {
        void onNewWalletItem(boolean bevetel);
    }
}
