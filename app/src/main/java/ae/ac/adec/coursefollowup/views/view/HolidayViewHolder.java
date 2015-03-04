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

    public TextView tvtinfo_text;
    private int mPosition;
    public IHolidayViewHolderClicks mListener;

    public HolidayViewHolder(View itemLayoutView,IHolidayViewHolderClicks listener) {
        super(itemLayoutView);
        mListener=listener;
        tvtinfo_text = (TextView) itemLayoutView.findViewById(R.id.info_text);
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
