package ae.ac.adec.coursefollowup.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IHolidayViewHolderClicks;
import ae.ac.adec.coursefollowup.views.view.HolidayViewHolder;

/**
 * Created by Tareq on 03/04/2015.
 */
public class HolidayAdapter extends RecyclerView.Adapter<HolidayViewHolder>  {

    public String[] mDataset;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public HolidayAdapter(String[] myDataset,Context context) {
        mDataset = myDataset;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HolidayViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardview_holiday, null);

        // create ViewHolder
        HolidayViewHolder viewHolder = new HolidayViewHolder(itemLayoutView, new IHolidayViewHolderClicks() {

            @Override
            public void onDelete(View v) {
                Log.i("TG", "D E F");
                Toast.makeText(v.getContext(), "on Delete Click", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onEdit(View v) {

            }

            @Override
            public void onClick(View v) {
                Log.i("TG","A B C");
                Toast.makeText(v.getContext(), "on Click", Toast.LENGTH_LONG).show();

            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(HolidayViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.tvtinfo_text.setText(mDataset[position].toString());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }


}
