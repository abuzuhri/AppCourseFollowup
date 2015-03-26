package ae.ac.adec.coursefollowup.views.view;

import android.view.View;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by mohamad El-lada on 03/20/2015.
 */
public class SemesterViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener
{

    public TextView titleTextView;
    public TextView txtFromDate;
    public TextView txtToDate;

    public IClickCardView mListener;

    public SemesterViewHolder(View itemLayoutView,IClickCardView listener) {
        super(itemLayoutView);
        mListener=listener;
        titleTextView = (TextView) itemLayoutView.findViewById(R.id.titleTextView);
        txtFromDate = (TextView) itemLayoutView.findViewById(R.id.txtFromDate);
        txtToDate = (TextView) itemLayoutView.findViewById(R.id.txtToDate);

        itemLayoutView.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        mListener.onClick(v,getID());
    }


}
