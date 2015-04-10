package ae.ac.adec.coursefollowup.views.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;
import ae.ac.adec.coursefollowup.views.view.HolidayViewHolder;

/**
 * Created by Tareq on 03/04/2015.
 */
public class HolidayAdapter extends RecyclerView.Adapter<HolidayViewHolder>  {

    public List<Holiday> mDataset;
    private Context context;
    private IClickCardView mListener;
    private Typeface tf;

    // Provide a suitable constructor (depends on the kind of dataset)
    public HolidayAdapter(List<Holiday> myDataset,Typeface tf,Context context,IClickCardView mListener) {
        mDataset = myDataset;
        this.context=context;
        this.mListener=mListener;
        this.tf=tf;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HolidayViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardview_holiday, null);

        // create ViewHolder
        HolidayViewHolder viewHolder = new HolidayViewHolder(itemLayoutView, new IClickCardView() {
            @Override
            public void onClick(View v,long ID) {
                mListener.onClick(v, ID);
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(HolidayViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Holiday holiday= mDataset.get(position);

        viewHolder.setID(holiday.getId());
        viewHolder.titleTextView.setText(holiday.Name);
        viewHolder.titleTextView.setTypeface(tf);

        String startDate= ConstantVariable.getDateString(holiday.StartDate);
        viewHolder.txtFromDate.setText(context.getString(R.string.holiday_start_date_hint) +": "+startDate);
        viewHolder.txtFromDate.setTypeface(tf);

        String endDate= ConstantVariable.getDateString(holiday.EndDate);
        viewHolder.txtToDate.setText(context.getString(R.string.holiday_end_date_hint) +": "+endDate);
        viewHolder.txtToDate.setTypeface(tf);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        AppLog.i("mDataset ==> " + mDataset.size());
        return mDataset.size();

    }


}
