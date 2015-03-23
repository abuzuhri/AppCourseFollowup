package ae.ac.adec.coursefollowup.views.view;

import android.view.View;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by Tareq on 03/04/2015.
 */
public class ExamViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener
{

    public TextView course_name;
    public TextView exam_duration;
    public TextView exam_startTime;

    public IClickCardView mListener;

    public ExamViewHolder(View itemLayoutView, IClickCardView listener) {
        super(itemLayoutView);
        mListener=listener;
        course_name = (TextView) itemLayoutView.findViewById(R.id.tv_exam_card_courseName);
        exam_duration = (TextView) itemLayoutView.findViewById(R.id.tv_exam_card_duration);
        exam_startTime = (TextView) itemLayoutView.findViewById(R.id.tv_exam_card_startTime);

        itemLayoutView.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        mListener.onClick(v,getID());
    }


}
