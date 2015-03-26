package ae.ac.adec.coursefollowup.views.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Semester;

public class CustomLVAdapter_Times extends BaseAdapter {
    List<CourseTimeDay> dataset;
    Activity context;

    public CustomLVAdapter_Times(Activity c, List<CourseTimeDay> dataset) {
        this.dataset = dataset;
        this.context = c;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public Object getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = context.getLayoutInflater().inflate(R.layout.custom_times_dialog_row, parent, false);

       CourseTimeDay ctd = (CourseTimeDay) dataset.get(position);
        TextView tv = (TextView) view.findViewById(R.id.tv_custom_times_row);
        tv.setText(ctd.Start_time+"-"+ctd.End_time);

        return view;
    }
}