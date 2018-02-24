package hu.bme.andrismulller.makeithappen_withfriends.Functions.Calendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.bme.andrismulller.makeithappen_withfriends.Functions.Calendar.CalendarFragment.OnListFragmentInteractionListener;
import hu.bme.andrismulller.makeithappen_withfriends.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a String and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyCalendarRecyclerViewAdapter extends RecyclerView.Adapter<MyCalendarRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;

    public MyCalendarRecyclerViewAdapter(List<String > items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
