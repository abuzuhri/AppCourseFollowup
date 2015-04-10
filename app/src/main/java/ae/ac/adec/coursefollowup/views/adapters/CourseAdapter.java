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
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;
import ae.ac.adec.coursefollowup.views.view.CourseViewHolder;
import ae.ac.adec.coursefollowup.views.view.ExamViewHolder;

/**
 * Created by Tareq on 03/04/2015.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {

    public List<Course> mDataset;
    private Context context;
    private IClickCardView mListener;
    private Typeface tf;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CourseAdapter(List<Course> myDataset,Typeface tf, Context context, IClickCardView mListener) {
        this.mDataset = myDataset;
        this.context = context;
        this.mListener = mListener;
        this.tf=tf;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardview_course, null);

        // create ViewHolder
        CourseViewHolder viewHolder = new CourseViewHolder(itemLayoutView,tf, new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                mListener.onClick(v, ID);
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CourseViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Course course = mDataset.get(position);

        viewHolder.setID(course.getId());
        viewHolder.course_nameCode.setText(course.Name + "(" + course.Code + ")");
        viewHolder.course_room.setText(course.Room);
        viewHolder.course_building.setText(course.Building);
        viewHolder.img_course_color.setBackgroundColor(Color.parseColor(course.ColorCode));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        AppLog.i("mDataset ==> " + mDataset.size());
        return mDataset.size();

    }


}
