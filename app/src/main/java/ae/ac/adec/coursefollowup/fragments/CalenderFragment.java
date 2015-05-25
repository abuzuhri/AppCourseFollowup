package ae.ac.adec.coursefollowup.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.disegnator.robotocalendar.RobotoCalendarView;

import java.text.DateFormat;
import java.text.ParseException;
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
import ae.ac.adec.coursefollowup.db.dal.NotificationDao;
import ae.ac.adec.coursefollowup.db.dal.TaskDao;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Notification;
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
    String[] tabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_calender, container, false);
        tabs = getResources().getStringArray(R.array.cats);
        ((BaseActivity) getActivity()).RemoveToolBarShadow();
        populateFragments();

        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager_calender);
        mSlidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs_calender);

        calendarView = (RobotoCalendarView) rootView.findViewById(R.id.robotoCalendarPicker);

        setCalenderListener();
        currentMonthIndex = 0;
        currentCalendar = Calendar.getInstance(Locale.ENGLISH);
        //currentCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        selectedDate = currentCalendar.getTime().getTime();
        selectedDayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
        AppLog.i(selectedDayOfWeek + " day in start");

        calendarView.initializeCalendar(currentCalendar);
        calendarView.markDayAsCurrentDay(currentCalendar.getTime());
        calendarView.markDayAsSelectedDay(currentCalendar.getTime());


        pagerAdapter = new SampleFragmentPagerAdapter(getChildFragmentManager(), tabs);
        mViewPager.setAdapter(pagerAdapter);

        fillTabs();
        markCalenderWithDots(currentCalendar.getTime().getTime());
        updateCurrentFragment(currentCalendar.getTime().getTime(), currentCalendar.get(Calendar.DAY_OF_WEEK));
        return rootView;
    }

    private void markCalenderWithDots(long date) {
        Calendar c_nxtMonth = Calendar.getInstance();
        Calendar c_currentMonth = Calendar.getInstance();

        c_nxtMonth.setTimeInMillis(date);
        c_nxtMonth.add(Calendar.MONTH, 1);
        c_nxtMonth.set(Calendar.DAY_OF_MONTH, 1);
        c_nxtMonth.set(Calendar.HOUR_OF_DAY, 0);
        c_nxtMonth.set(Calendar.MINUTE, 0);
        c_nxtMonth.set(Calendar.SECOND, 0);

        c_currentMonth.setTimeInMillis(date);
        c_currentMonth.set(Calendar.HOUR_OF_DAY, 0);
        c_currentMonth.set(Calendar.MINUTE, 0);
        c_currentMonth.set(Calendar.SECOND, 0);
        c_currentMonth.set(Calendar.DAY_OF_MONTH, 1);

        long monthStartDate = c_currentMonth.getTimeInMillis();
        long monthEndDate = c_nxtMonth.getTimeInMillis();

        NotificationDao notificationDao = new NotificationDao();
        List<Notification> notifications = notificationDao.getNotificationsOnPeriod(monthStartDate, monthEndDate);
        Calendar cal = Calendar.getInstance();
        for (Notification n : notifications) {
            cal.setTime(n.CalenderDateTime);
            if (n.Course != null)
                calendarView.markDayWithStyle(RobotoCalendarView.BLUE_CIRCLE, cal.getTime());
            else if (n.Exam != null)
                calendarView.markDayWithStyle(RobotoCalendarView.RED_CIRCLE, cal.getTime());
            else if (n.Task != null)
                calendarView.markDayWithStyle(RobotoCalendarView.GREEN_CIRCLE, cal.getTime());
        }

        /*List<Exam> exams_list = new ExamDao().getExamsOnDate(monthStartDate, monthEndDate);
        List<Task> tasks_list = new TaskDao().getTasksOnDate(monthStartDate, monthEndDate);
        List<CourseTimeDay> ctd_list = new CourseTimeDayDao().getTimesWithinPeriod(monthStartDate, monthEndDate);

        AppLog.i(exams_list.size() + " Exam, " + tasks_list.size() + " Task, " + ctd_list.size() + " Course");

        Calendar calendar = Calendar.getInstance();
        for (CourseTimeDay ctd : ctd_list) {
            if (ctd.IsRepeat) {
                // loop for five weeks to select repeated days overall course
                for (int i = 0; i < 6; i++) {
                    calendar.setTimeInMillis(date); // becaues clear statement deleted year and month
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.DAY_OF_WEEK, ctd.DayOfWeek);
                    calendar.add(Calendar.DATE, i * 7);
                    if ((calendar.getTime().getTime() >= ctd.Course.StartDate.getTime()) &&
                            (calendar.getTime().getTime() < ctd.Course.EndDate.getTime()))
                        calendarView.markDayWithStyle(RobotoCalendarView.BLUE_CIRCLE, calendar.getTime());
                    calendar.clear();
                }
            } else {
                calendar.setTime(ctd.Start_time);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendarView.markDayWithStyle(RobotoCalendarView.BLUE_CIRCLE, calendar.getTime());
            }
        }
        for (Task task : tasks_list) {
            calendar.setTime(task.DueDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendarView.markDayWithStyle(RobotoCalendarView.GREEN_CIRCLE, calendar.getTime());
        }
        for (Exam exam : exams_list) {
            calendar.setTime(exam.StartDateTime);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendarView.markDayWithStyle(RobotoCalendarView.RED_CIRCLE, calendar.getTime());
        }*/

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
        mSlidingTabLayout.setContentDescription(3, tabs[2]);

        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.white));

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
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTime(date);
//                if (getString(R.string.lang).equals("ar")) {
//                    cal.add(Calendar.DAY_OF_WEEK,-1);
//                }


                selectedDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                AppLog.i(date.toGMTString() + "day of week " + selectedDayOfWeek);
                selectedDate = cal.getTime().getTime();

                calendarView.markDayAsSelectedDay(cal.getTime());
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
        AppLog.i("jma: in update");
        Fragment f = getChildFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.view_pager_calender + ":" + mViewPager.getCurrentItem());
        if (mViewPager.getCurrentItem() == 0 && f != null) {
            ((Calender_ExamFragment) f).populateExamsList(date);
            AppLog.i("jma: in exams up");
        } else if (mViewPager.getCurrentItem() == 1 && f != null) {
            ((Calender_CourseFragment) f).populateCoursesList(date, dayOfWeek);
            AppLog.i("jma: in courses up");
        } else if (mViewPager.getCurrentItem() == 2 && f != null) {
            ((Calender_TasksFragment) f).populateTasksList(date);
            AppLog.i("jma: in tasks up");
        }
    }

    private void updateCalendar() {
        currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.MONTH, currentMonthIndex);
        //currentCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.initializeCalendar(currentCalendar);

        if (currentCalendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {
            calendarView.markDayAsSelectedDay(currentCalendar.getTime());
            calendarView.markDayAsCurrentDay(currentCalendar.getTime());
        }
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
