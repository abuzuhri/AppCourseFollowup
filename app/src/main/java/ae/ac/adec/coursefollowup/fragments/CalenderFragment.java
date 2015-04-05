package ae.ac.adec.coursefollowup.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.disegnator.robotocalendar.RobotoCalendarView;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.Lib.SlidingTabs.SlidingTabLayout;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.BaseActivity;
import ae.ac.adec.coursefollowup.db.dal.CourseTimeDayDao;
import ae.ac.adec.coursefollowup.db.dal.ExamDao;
import ae.ac.adec.coursefollowup.db.dal.TaskDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Task;

/**
 * Created by Tareq on 02/28/2015.
 */
public class CalenderFragment extends BaseFragment {
    RobotoCalendarView calendarView;
    int currentMonthIndex;
    Calendar currentCalendar;
    long selectedDate = System.currentTimeMillis();
    int selectedDayOfWeek = 1;
    ViewPager mViewPager;
    SlidingTabLayout mSlidingTabLayout;
    SampleFragmentPagerAdapter pagerAdapter;
    List<Fragment> frags;
    String[] tabs = {"Exams", "Courses", "Tasks"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ((BaseActivity) getActivity()).RemoveToolBarShadow();
        populateFragments();

        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager_calender);
        mSlidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs_calender);

        calendarView = (RobotoCalendarView) rootView.findViewById(R.id.robotoCalendarPicker);

        setCalenderListener();
        currentMonthIndex = 0;
        currentCalendar = Calendar.getInstance(Locale.ENGLISH);

        calendarView.initializeCalendar(currentCalendar);
        calendarView.markDayAsCurrentDay(currentCalendar.getTime());
        calendarView.markDayAsSelectedDay(currentCalendar.getTime());

        pagerAdapter = new SampleFragmentPagerAdapter(getChildFragmentManager(), tabs);
        mViewPager.setAdapter(pagerAdapter);

        fillTabs();
        updateCurrentFragment(currentCalendar.getTime().getTime(), currentCalendar.get(Calendar.DAY_OF_WEEK));
        markCalenderWithDots(currentCalendar.getTime().getTime());

        return rootView;
    }

    private void markCalenderWithDots(long date) {
        Calendar c_nxtMonth = Calendar.getInstance();
        Calendar c_today = Calendar.getInstance();

        c_nxtMonth.setTimeInMillis(date);
        c_nxtMonth.add(Calendar.MONTH, 1);
        c_nxtMonth.set(Calendar.DAY_OF_MONTH, 1);
        c_nxtMonth.set(Calendar.HOUR_OF_DAY, 0);
        c_nxtMonth.set(Calendar.MINUTE, 0);
        c_nxtMonth.set(Calendar.SECOND, 0);

        c_today.setTimeInMillis(date);
        c_today.set(Calendar.HOUR_OF_DAY, 0);
        c_today.set(Calendar.MINUTE, 0);
        c_today.set(Calendar.SECOND, 0);
        c_today.set(Calendar.DAY_OF_MONTH, 1);

        long monthStartDate = c_today.getTimeInMillis();
        long monthEndDate = c_nxtMonth.getTimeInMillis();

        List<Exam> exams_list = new ExamDao().getExamsOnDate(monthStartDate, monthEndDate);
        List<Task> tasks_list = new TaskDao().getTasksOnDate(monthStartDate, monthEndDate);
        List<CourseTimeDay> ctd_list = new CourseTimeDayDao().getCoursesOnMonth(monthStartDate, monthEndDate);
        List<Course> courses_list = new ArrayList<Course>();
        for (CourseTimeDay c : ctd_list)
            courses_list.add(c.Course);

        AppLog.i(exams_list.size() + " Exam, " + tasks_list.size() + " Task, " + courses_list.size() + " Course");


    }

    private void populateFragments() {
        frags = new ArrayList<Fragment>();
        frags.add(new Calender_ExamFragment());
        frags.add(new Calender_CourseFragment());
        frags.add(new Calender_TasksFragment());
    }

    private void fillTabs() {
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setContentDescription(1, tabs[0]);
        mSlidingTabLayout.setContentDescription(2, tabs[1]);
        mSlidingTabLayout.setContentDescription(2, tabs[2]);

        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.primary));

        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateCurrentFragment(selectedDate, selectedDayOfWeek);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        fillTabs();
    }

    public void setCalenderListener() {
        calendarView.setRobotoCalendarListener(new RobotoCalendarView.RobotoCalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                selectedDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

                selectedDate = date.getTime();
                calendarView.markDayAsSelectedDay(date);
                updateCurrentFragment(selectedDate, selectedDayOfWeek);
            }

            @Override
            public void onRightButtonClick() {
                currentMonthIndex++;
                updateCalendar();
                markCalenderWithDots(currentCalendar.getTime().getTime());
            }

            @Override
            public void onLeftButtonClick() {
                currentMonthIndex--;
                updateCalendar();
                markCalenderWithDots(currentCalendar.getTime().getTime());
            }
        });
    }

    private void updateCurrentFragment(long date, int dayOfWeek) {
        Fragment f = getChildFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.view_pager_calender + ":" + mViewPager.getCurrentItem());
        if (mViewPager.getCurrentItem() == 0 && f != null) {
            ((Calender_ExamFragment) f).populateExamsList(date);
        } else if (mViewPager.getCurrentItem() == 1 && f != null) {
            ((Calender_CourseFragment) f).populateCoursesList(date, dayOfWeek);
        } else if (mViewPager.getCurrentItem() == 2 && f != null) {
            ((Calender_TasksFragment) f).populateTasksList(date);
        }
    }


    private void updateCalendar() {
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        calendarView.initializeCalendar(currentCalendar);
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        String[] tabTitles;

        public SampleFragmentPagerAdapter(FragmentManager fm, String[] tabTitles) {
            super(fm);
            this.tabTitles = tabTitles;
        }

        @Override
        public int getCount() {
            return frags.size();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = frags.get(position);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

    }


}
