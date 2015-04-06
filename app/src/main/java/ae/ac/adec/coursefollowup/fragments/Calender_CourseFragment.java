package ae.ac.adec.coursefollowup.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.CourseTimeDayDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.views.adapters.CourseAdapter;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by Tareq on 02/28/2015.
 */
public class Calender_CourseFragment extends BaseFragment {
    RecyclerView lv_courses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_calender_course, container, false);

        lv_courses = (RecyclerView) rootView.findViewById(R.id.my_recycler_view_courses);

        lv_courses.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lv_courses.setLayoutManager(mLayoutManager);

        return rootView;
    }

    public void populateCoursesList(long date, int dayOfWeek) {

        Calendar c_tomorrow = Calendar.getInstance();
        Calendar c_today = Calendar.getInstance();

        c_tomorrow.setTimeInMillis(date);
        c_tomorrow.add(Calendar.DATE, 1);
        c_tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        c_tomorrow.set(Calendar.MINUTE, 0);
        c_tomorrow.set(Calendar.SECOND, 0);

        c_today.setTimeInMillis(date);
        c_today.set(Calendar.HOUR_OF_DAY, 0);
        c_today.set(Calendar.MINUTE, 0);
        c_today.set(Calendar.SECOND, 0);

        populateCoursesList(c_today.getTimeInMillis(), c_tomorrow.getTimeInMillis(), dayOfWeek);
    }

    public void populateCoursesList(long startDate, long endDate, int dayOfWeek) {
        List<CourseTimeDay> ctd = new CourseTimeDayDao().getCoursesTimesOnDate(startDate, endDate, dayOfWeek);
        List<Course> courses = new ArrayList<Course>();
        for (CourseTimeDay c : ctd) {
            if ((startDate >= c.Course.StartDate.getTime()) &&
                    (endDate < c.Course.EndDate.getTime()))
                courses.add(c.Course);
        }
        CourseAdapter courses_adapter = new CourseAdapter(courses, getActivity(), new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                AppAction.OpenActivityWithFRAGMENT(getActivity(), CourcesFragmentView.class.getName(), ID);
            }
        });
        lv_courses.setAdapter(courses_adapter);
    }
}
