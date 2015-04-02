package ae.ac.adec.coursefollowup.views.view;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by MyLabtop on 4/1/2015.
 */
public class NoteViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

    public ImageView img_note_color;
    public TextView txtNoteAddDate;
    public TextView txtNoteType;
    public TextView txtNoteSubject;

    public IClickCardView mListener;

    public NoteViewHolder(View itemLayoutView, IClickCardView listener) {
        super(itemLayoutView);
        mListener = listener;
        img_note_color = (ImageView) itemLayoutView.findViewById(R.id.img_note_color);
        txtNoteSubject = (TextView) itemLayoutView.findViewById(R.id.txtNoteSubject);
        txtNoteType = (TextView) itemLayoutView.findViewById(R.id.txtNoteType);
        txtNoteAddDate = (TextView) itemLayoutView.findViewById(R.id.txtNoteAddDate);
        itemLayoutView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        mListener.onClick(v, getID());
    }


}
