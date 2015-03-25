package ae.ac.adec.coursefollowup.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;
import ae.ac.adec.coursefollowup.views.view.SemesterViewHolder;

/**
 * Created by Mohamaed El-lada on 3/21/2015.
 */
public class SemesterAdapter extends RecyclerView.Adapter<SemesterViewHolder>  {
    public List<Semester> mDataset;
    private Context context;
    private IClickCardView mListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public SemesterAdapter(List<Semester> myDataset,Context context,IClickCardView mListener) {
        mDataset = myDataset;
        this.context=context;
        this.mListener=mListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SemesterViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardview_semester, null);

        // create ViewHolder
        SemesterViewHolder viewHolder = new SemesterViewHolder(itemLayoutView, new IClickCardView() {
            @Override
            public void onClick(View v,long ID) {
                mListener.onClick(v, ID);
            }
        });
        return viewHolder;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SemesterViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Semester semester= mDataset.get(position);

        viewHolder.setID(semester.getId());
        viewHolder.titleTextView.setText(semester.Name);

        String startDate= ConstantVariable.getDateString(semester.StartDate);
        viewHolder.txtFromDate.setText(context.getString(R.string.semester_start_date_hint) +": "+startDate);

        String endDate= ConstantVariable.getDateString(semester.EndDate);
        viewHolder.txtToDate.setText(context.getString(R.string.semester_end_date_hint) +": "+endDate);

        viewHolder.txtSelectYear.setText("Year: "+semester.year.Name);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        AppLog.i("mDataset ==> " + mDataset.size());
        return mDataset.size();

    }

}
