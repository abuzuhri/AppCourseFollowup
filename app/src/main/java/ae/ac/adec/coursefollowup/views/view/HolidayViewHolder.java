package ae.ac.adec.coursefollowup.views.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IHolidayViewHolderClicks;

/**
 * Created by Tareq on 03/04/2015.
 */
public class HolidayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
{

    public TextView titleTextView;
    public TextView txtFromDate;
    public TextView txtToDate;

    public TextView butEdit;
    public TextView btnDelete;

    private int mPosition;
    public IHolidayViewHolderClicks mListener;

    public HolidayViewHolder(View itemLayoutView,IHolidayViewHolderClicks listener) {
        super(itemLayoutView);
        mListener=listener;
        titleTextView = (TextView) itemLayoutView.findViewById(R.id.titleTextView);
        txtFromDate = (TextView) itemLayoutView.findViewById(R.id.txtFromDate);
        txtToDate = (TextView) itemLayoutView.findViewById(R.id.txtToDate);

        butEdit = (TextView) itemLayoutView.findViewById(R.id.butEdit);
        btnDelete = (TextView) itemLayoutView.findViewById(R.id.btnDelete);
        itemLayoutView.setOnClickListener(this);

    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @Override
    public boolean onLongClick(View v) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView)
        {
            mListener.onDelete(v);
        }
        else {
            mListener.onClick(v);
        }
    }


}
