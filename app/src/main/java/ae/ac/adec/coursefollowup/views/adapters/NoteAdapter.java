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
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;
import ae.ac.adec.coursefollowup.views.view.NoteViewHolder;
import ae.ac.adec.coursefollowup.views.view.TaskViewHolder;

/**
 * Created by MyLabtop on 3/24/2015.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>  {

    public List<Note> mDataset;
    private Context context;
    private IClickCardView mListener;
    private int posList=-1;
    private Typeface tf;

    // Provide a suitable constructor (depends on the kind of dataset)
    public NoteAdapter(List<Note> myDataset,Typeface tf,Context context,IClickCardView mListener) {
        mDataset = myDataset;
        this.context=context;
        this.mListener=mListener;
        this.tf=tf;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardview_note, null);

        // create ViewHolder
        NoteViewHolder viewHolder = new NoteViewHolder(itemLayoutView, new IClickCardView() {
            @Override
            public void onClick(View v,long ID) {
                mListener.onClick(v, ID);
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NoteViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        Note note= mDataset.get(position);

        viewHolder.setID(note.getId());

        viewHolder.txtNoteSubject.setText(note.Course.Name);
        viewHolder.txtNoteSubject.setTypeface(tf);

        String addDate= ConstantVariable.getDateString(note.DateAdded);
        viewHolder.txtNoteAddDate.setText(addDate);
        viewHolder.txtNoteAddDate.setTypeface(tf);

        viewHolder.txtNoteType.setText(ConstantVariable.NoteType.fromInteger(note.NoteType));
        viewHolder.txtNoteType.setTypeface(tf);

        viewHolder.img_note_color.setBackgroundColor(Color.parseColor(note.Course.ColorCode));

        setposList(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
    //    AppLog.i("mDataset ==> " + mDataset.size());
       return mDataset.size();
      //  return posList;

    }



    private void setposList(int i){
        posList = i;
    }

public int getPosition(){
    return posList;
}
}
