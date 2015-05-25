package ae.ac.adec.coursefollowup.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;

/**
 * Created by JMA on 4/23/2015.
 */
public class TimesListAdapter extends ArrayAdapter {
    ArrayList<CourseTimeDay> data;
    Activity context;
    public TimesListAdapter(Activity context,ArrayList<CourseTimeDay> data) {
        super(context, 0);
        this.data=data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
