package ae.ac.adec.coursefollowup.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Course;

public class CustomLVAdapter_Courses extends BaseAdapter {
    List<Course> dataset;
    Activity context;

    public CustomLVAdapter_Courses(Activity c, List<Course> dataset) {
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
            view = context.getLayoutInflater().inflate(R.layout.custom_course_dialog_row, parent, false);

        TextView tv = (TextView) view.findViewById(R.id.tv_custom_course_row);
        ImageView img_color = (ImageView) view.findViewById(R.id.img_custom_course_row);

        tv.setText(((Course) dataset.get(position)).Name);
        img_color.setBackgroundColor(Color.parseColor(dataset.get(position).ColorCode));

        return view;
    }
}