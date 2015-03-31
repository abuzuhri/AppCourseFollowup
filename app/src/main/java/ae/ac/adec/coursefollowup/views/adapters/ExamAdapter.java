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
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;
import ae.ac.adec.coursefollowup.views.view.ExamViewHolder;
import ae.ac.adec.coursefollowup.views.view.HolidayViewHolder;

/**
 * Created by Tareq on 03/04/2015.
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamViewHolder> {

    public List<Exam> mDataset;
    private Context context;
    private IClickCardView mListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExamAdapter(List<Exam> myDataset, Context context, IClickCardView mListener) {
        this.mDataset = myDataset;
        this.context = context;
        this.mListener = mListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardview_exam, null);

        // create ViewHolder
        ExamViewHolder viewHolder = new ExamViewHolder(itemLayoutView, new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                mListener.onClick(v, ID);
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ExamViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Exam exam = mDataset.get(position);

        viewHolder.setID(exam.getId());
        viewHolder.course_name.setText(exam.Course.Name);

        long sd = exam.StartDateTime.getTime();
        long ed = exam.EndDateTime.getTime();
        long diff = (ed-sd)/(1000*60);
        viewHolder.exam_duration.setText(diff+" Min");

        String startTime = ConstantVariable.getTimeString(exam.StartDateTime);
        String startDate = ConstantVariable.getDateString(exam.StartDateTime);
        viewHolder.exam_startTime.setText(context.getString(R.string.exam_start_date_hint) + ": " + startTime+" On: "+startDate);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        AppLog.i("mDataset ==> " + mDataset.size());
        return mDataset.size();

    }


}
