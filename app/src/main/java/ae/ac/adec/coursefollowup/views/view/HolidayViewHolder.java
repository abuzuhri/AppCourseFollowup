package ae.ac.adec.coursefollowup.views.view;

import android.view.View;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by Tareq on 03/04/2015.
 */
public class HolidayViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener
{

    public TextView titleTextView;
    public TextView txtFromDate;
    public TextView txtToDate;

    public IClickCardView mListener;

    public HolidayViewHolder(View itemLayoutView,IClickCardView listener) {
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
