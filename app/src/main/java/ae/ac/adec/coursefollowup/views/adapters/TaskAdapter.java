package ae.ac.adec.coursefollowup.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;
import ae.ac.adec.coursefollowup.views.view.TaskViewHolder;

/**
 * Created by MyLabtop on 3/24/2015.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder>  {

    private Typeface tf;
    public List<Task> mDataset;
    private Context context;
    private IClickCardView mListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public TaskAdapter(List<Task> myDataset,Typeface tf,Context context,IClickCardView mListener) {
        mDataset = myDataset;
        this.context=context;
        this.mListener=mListener;
        this.tf=tf;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardview_task, null);

        // create ViewHolder
        TaskViewHolder viewHolder = new TaskViewHolder(itemLayoutView, new IClickCardView() {
            @Override
            public void onClick(View v,long ID) {
                mListener.onClick(v, ID);
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TaskViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Task task= mDataset.get(position);

        viewHolder.setID(task.getId());

        viewHolder.txtTaskTitle.setText(task.Name);
        viewHolder.txtTaskTitle.setTypeface(tf);

        String dueDate= ConstantVariable.getDateString(task.DueDate);
        viewHolder.txtTaskDueDate.setText(dueDate);
        viewHolder.txtTaskDueDate.setTypeface(tf);

        viewHolder.txtTaskSubject.setText(task.Course.Name+":");
        viewHolder.txtTaskSubject.setTypeface(tf);

        viewHolder.txtTaskType.setText(ConstantVariable.TaskType.fromInteger(task.TaskType));
        viewHolder.txtTaskType.setTypeface(tf);
        viewHolder.img_task_color.setBackgroundColor(Color.parseColor(task.Course.ColorCode));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        AppLog.i("mDataset ==> " + mDataset.size());
        return mDataset.size();

    }


}
