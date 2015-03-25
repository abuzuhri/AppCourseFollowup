package ae.ac.adec.coursefollowup.views.view;

import android.view.View;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by Tareq on 03/04/2015.
 */
public class CourseViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener
{

    public TextView course_nameCode;
    public TextView course_room;
    public TextView course_building;

    public IClickCardView mListener;

    public CourseViewHolder(View itemLayoutView, IClickCardView listener) {
        super(itemLayoutView);
        mListener=listener;
        course_nameCode = (TextView) itemLayoutView.findViewById(R.id.tv_course_card_courseNameCode);
        course_room = (TextView) itemLayoutView.findViewById(R.id.tv_course_card_room);
        course_building = (TextView) itemLayoutView.findViewById(R.id.tv_course_card_building);

        itemLayoutView.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        mListener.onClick(v,getID());
    }


}
