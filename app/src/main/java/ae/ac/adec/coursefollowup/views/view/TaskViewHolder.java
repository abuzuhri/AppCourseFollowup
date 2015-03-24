package ae.ac.adec.coursefollowup.views.view;

import android.view.View;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by MyLabtop on 3/24/2015.
 */
public class TaskViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener
{

    public View colorTaskRow1,colorTaskRow2;
    public TextView txtTaskTitle;
    public TextView txtTaskDueDate;
    public TextView txtTaskSubject;
    public TextView txtTaskType;

    public IClickCardView mListener;

    public TaskViewHolder(View itemLayoutView,IClickCardView listener) {
        super(itemLayoutView);
        mListener=listener;
        colorTaskRow1 = (View) itemLayoutView.findViewById(R.id.colorTaskrow1);
        colorTaskRow2 = (View) itemLayoutView.findViewById(R.id.colorTaskrow2);
        txtTaskTitle = (TextView) itemLayoutView.findViewById(R.id.txtTaskTitle);
        txtTaskDueDate = (TextView) itemLayoutView.findViewById(R.id.txtTaskDueDate);
        txtTaskSubject = (TextView) itemLayoutView.findViewById(R.id.txtTaskSubject);
        txtTaskType = (TextView) itemLayoutView.findViewById(R.id.txtTaskType);
        itemLayoutView.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        mListener.onClick(v,getID());
    }


}
