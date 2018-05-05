package hu.bme.andrismulller.makeithappen_withfriends.Functions.Wallet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.bme.andrismulller.makeithappen_withfriends.R;
import hu.bme.andrismulller.makeithappen_withfriends.model.WalletItem;

import java.text.SimpleDateFormat;
import java.util.List;

public class MyWalletRecyclerViewAdapter extends RecyclerView.Adapter<MyWalletRecyclerViewAdapter.ViewHolder> {

    private final List<WalletItem> mValues;
    WalletUpdatedListener walletUpdatedListener;

    public interface WalletUpdatedListener {
    	void updated(int balance);
    }

    public MyWalletRecyclerViewAdapter(WalletUpdatedListener listener) {
        mValues = WalletItem.listAll(WalletItem.class);
        walletUpdatedListener = listener;
	    walletUpdatedListener.updated(countBalance());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_wallet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDescriptionView.setText(mValues.get(position).getDescription());
        holder.mValueView.setText(mValues.get(position).getErtek() + " " + mValues.get(position).getValuta());
        holder.mCategoryView.setText(mValues.get(position).getCategory());

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");
        holder.mDateTimeView.setText(dateTimeFormat.format(mValues.get(position).getDateTime()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void update(WalletItem walletItem) {
        mValues.add(walletItem);
        notifyDataSetChanged();

        walletUpdatedListener.updated(countBalance());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDescriptionView;
        public final TextView mValueView;
        public final TextView mCategoryView;
        public final TextView mDateTimeView;
        public WalletItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDescriptionView = (TextView) view.findViewById(R.id.wallet_item_description_tv);
            mValueView = (TextView) view.findViewById(R.id.wallet_item_value_tv);
            mCategoryView = (TextView) view.findViewById(R.id.wallet_item_category_tv);
            mDateTimeView = (TextView) view.findViewById(R.id.wallet_item_date_tv);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mValueView.getText() + "'";
        }
    }

    public int countBalance(){
	    int balance = 0;
	    for (WalletItem item : mValues){
		    if (item.isBevetel()){
			    balance += item.getErtek();
		    } else {
			    balance -= item.getErtek();
		    }
	    }
	    return balance;
    }
}
