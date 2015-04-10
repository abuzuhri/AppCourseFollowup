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
import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;
import ae.ac.adec.coursefollowup.views.view.YearViewHolder;

/**
 * Created by Tareq on 03/04/2015.
 */
public class YearAdapter extends RecyclerView.Adapter<YearViewHolder>  {

    private final Typeface tf;
    public List<Year> mDataset;
    private Context context;
    private IClickCardView mListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public YearAdapter(List<Year> myDataset,Typeface tf, Context context, IClickCardView mListener) {
        mDataset = myDataset;
        this.context=context;
        this.mListener=mListener;
        this.tf=tf;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public YearViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardview_year, null);

        // create ViewHolder
        YearViewHolder viewHolder = new YearViewHolder(itemLayoutView, new IClickCardView() {
            @Override
            public void onClick(View v,long ID) {
                mListener.onClick(v, ID);
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(YearViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Year year= mDataset.get(position);

        viewHolder.setID(year.getId());
        viewHolder.titleTextView.setText(year.Name);
        viewHolder.titleTextView.setTypeface(tf);

        String startDate= ConstantVariable.getDateString(year.StartDate);
        viewHolder.txtFromDate.setText(context.getString(R.string.holiday_start_date_hint) +": "+startDate);
        viewHolder.txtFromDate.setTypeface(tf);

        String endDate= ConstantVariable.getDateString(year.EndDate);
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
