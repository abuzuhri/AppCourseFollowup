package ae.ac.adec.coursefollowup.views.view;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by MyLabtop on 3/24/2015.
 */
public class TaskViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

    public ImageView img_task_color;
    public TextView txtTaskTitle;
    public TextView txtTaskDueDate;
    public TextView txtTaskSubject;
    public TextView txtTaskType;

    public IClickCardView mListener;

    public TaskViewHolder(View itemLayoutView, IClickCardView listener) {
        super(itemLayoutView);
        mListener = listener;
        img_task_color = (ImageView) itemLayoutView.findViewById(R.id.img_task_color);
        txtTaskTitle = (TextView) itemLayoutView.findViewById(R.id.txtTaskTitle);
        txtTaskDueDate = (TextView) itemLayoutView.findViewById(R.id.txtTaskDueDate);
        txtTaskSubject = (TextView) itemLayoutView.findViewById(R.id.txtTaskSubject);
        txtTaskType = (TextView) itemLayoutView.findViewById(R.id.txtTaskType);
        itemLayoutView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        mListener.onClick(v, getID());
    }


}
