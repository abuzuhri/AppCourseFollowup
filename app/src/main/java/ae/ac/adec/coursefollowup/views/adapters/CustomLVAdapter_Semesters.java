package ae.ac.adec.coursefollowup.views.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.db.models.Year;

public class CustomLVAdapter_Semesters extends BaseAdapter {
    List<Semester> dataset;
    Activity context;

    public CustomLVAdapter_Semesters(Activity c, List<Semester> dataset) {
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
            view = context.getLayoutInflater().inflate(R.layout.custom_semester_dialog_row, parent, false);

        TextView tv = (TextView) view.findViewById(R.id.tv_custom_semester_row);
        tv.setText(((Semester) dataset.get(position)).Name);

        return view;
    }
}