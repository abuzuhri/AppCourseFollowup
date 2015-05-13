package ae.ac.adec.coursefollowup.views.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.CourseTimeDayDao;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.fragments.DayTimeFragmentView;
import ae.ac.adec.coursefollowup.services.AppAction;

public class CustomLVAdapter_Times extends BaseAdapter {
    Long course_id;
    Activity context;
    ConstantVariable.DayOfWeek[] days;

    public CustomLVAdapter_Times(Activity c, Long id, ConstantVariable.DayOfWeek[] days) {
        this.course_id = id;
        this.context = c;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return days[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        //if (view == null) {
        view = context.getLayoutInflater().inflate(R.layout.custom_times_dialog_row, parent, false);

        final List<CourseTimeDay> cTDays = new CourseTimeDayDao().getTimesByDay(days[position].id);
        TextView tv = (TextView) view.findViewById(R.id.tv_custom_times_row);
        tv.setText(ConstantVariable.DayOfWeek.fromInteger(days[position].id));
        final LinearLayout ll_timesDetails = (LinearLayout) view.findViewById(R.id.ll_custom_times_details);
        TextView tv_details;
        CourseTimeDay ctd;
        if (cTDays.size() <= 0) {
            tv_details = new TextView(context);
            tv_details.setText("\t" + context.getString(R.string.noTimes));
            ll_timesDetails.addView(tv_details);
        }
        for (int i = 0; i < cTDays.size(); i++) {
            ctd = cTDays.get(i);
            tv_details = new TextView(context);
            if (ctd.Course != null) {
                tv_details.setText('\t' + ctd.Course.Name + " -> " + ConstantVariable.getTimeString(ctd.Start_time) + " - " +
                        ConstantVariable.getTimeString(ctd.End_time));
                if (ctd.Course.getId().longValue() == course_id.longValue())
                    tv_details.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                tv_details.setText('\t' + "Jaffer" + " -> " + ConstantVariable.getTimeString(ctd.Start_time) + " - " +
                        ConstantVariable.getTimeString(ctd.End_time));
                tv_details.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
            ll_timesDetails.addView(tv_details);
//                tv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (ll_timesDetails.getVisibility() == View.GONE)
//                            ll_timesDetails.setVisibility(View.VISIBLE);
//                        else
//                            ll_timesDetails.setVisibility(View.GONE);
//                    }
//                });
            final CourseTimeDay finalCtd = ctd;
            tv_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalCtd.Course == null){
                        AppAction.OpenActivityWithFRAGMENT(context, DayTimeFragmentView.class.getName(), finalCtd.getId());
                        context.finish();
                    }
                    else if (finalCtd.Course.getId().longValue() == course_id.longValue()) {
                        AppAction.OpenActivityWithFRAGMENT(context, DayTimeFragmentView.class.getName(), finalCtd.getId());
                        context.finish();
                    } else {
                        AppAction.DiaplayError(context, context.getString(R.string.BR_GENERAL_002));
                    }

                }
            });
        }
        //}
        return view;
    }
}