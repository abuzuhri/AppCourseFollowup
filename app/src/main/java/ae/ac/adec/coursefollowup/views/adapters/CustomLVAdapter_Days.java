package ae.ac.adec.coursefollowup.views.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Course;

public class CustomLVAdapter_Days extends BaseAdapter {
    ConstantVariable.DayOfWeek[] dataset;
    Activity context;
    public List<Integer> days;

    public CustomLVAdapter_Days(Activity c, ConstantVariable.DayOfWeek[] dataset) {
        this.dataset = dataset;
        this.context = c;
        days = new ArrayList<Integer>();
    }

    @Override
    public int getCount() {
        return dataset.length;
    }

    @Override
    public Object getItem(int position) {
        return dataset[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = context.getLayoutInflater().inflate(R.layout.custom_days_dialog_row, parent, false);

            TextView tv = (TextView) view.findViewById(R.id.tv_custom_days_row);
            CheckBox cb = (CheckBox) view.findViewById(R.id.cb_custom_days_row);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked)
                            days.add(dataset[position].id);
                        else
                            days.remove(dataset[position].id);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            tv.setText(ConstantVariable.DayOfWeek.fromInteger(dataset[position].id));
        }
        return view;
    }
}