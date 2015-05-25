package ae.ac.adec.coursefollowup.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.Lib.SlidingTabs.SlidingTabLayout;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.BaseActivity;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.dal.CourseTimeDayDao;
import ae.ac.adec.coursefollowup.db.dal.ExamDao;
import ae.ac.adec.coursefollowup.db.dal.NoteDao;
import ae.ac.adec.coursefollowup.db.dal.TaskDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.services.AppAction;


/**
 * Created by Tareq on 02/28/2015.
 */
public class DashboardFragment extends BaseFragment {
    TextView tv_tasks, tv_classes, tv_exams, tv_time, tv_date;
    ImageView img_tasks, img_classes, img_exams;
    LinearLayout ll_tasks, ll_classes, ll_exams;
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
    public void onDestroy() {
        super.onDestroy();
        ConstantVariable.isInDash = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ((BaseActivity) getActivity()).RemoveToolBarShadow();

        ConstantVariable.isInDash = true;

        tabs = getResources().getStringArray(R.array.cats);
        tv_tasks = (TextView) rootView.findViewById(R.id.tv_task_title);
        tv_tasks.setTypeface(tf_roboto_light);
        tv_classes = (TextView) rootView.findViewById(R.id.tv_class_title);
        tv_classes.setTypeface(tf_roboto_light);
        tv_exams = (TextView) rootView.findViewById(R.id.tv_exam_title);
        tv_exams.setTypeface(tf_roboto_light);

        Calendar current_date = Calendar.getInstance();
        String day = getString(ConstantVariable.DayOfWeek.fromInteger(current_date.get(Calendar.DAY_OF_WEEK)));

        tv_date = (TextView) rootView.findViewById(R.id.tv_date);
        tv_date.setTypeface(tf_roboto_light);

        tv_date.setText(getString(R.string.current_date) +" "+ day + " " + ConstantVariable.getDateString(current_date.getTime()));

        tv_time = (TextView) rootView.findViewById(R.id.tv_time);
        tv_time.setTypeface(tf_roboto_light);
        tv_time.setText(getString(R.string.current_time) + ConstantVariable.getTimeString(current_date.getTime()));

        img_tasks = (ImageView) rootView.findViewById(R.id.img_task);
        img_classes = (ImageView) rootView.findViewById(R.id.img_class);
        img_exams = (ImageView) rootView.findViewById(R.id.img_exam);

        ll_tasks = (LinearLayout) rootView.findViewById(R.id.ll_tasks);
        ll_classes = (LinearLayout) rootView.findViewById(R.id.ll_classes);
        ll_exams = (LinearLayout) rootView.findViewById(R.id.ll_exams);

        fillCards();
        populateFragments();

        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager_dashboard);
        mSlidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs_dashboard);
        pagerAdapter = new SampleFragmentPagerAdapter(getChildFragmentManager(), tabs);
        mViewPager.setAdapter(pagerAdapter);

        fillTabs();

        ll_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).selectItem(ConstantVariable.Category.Tasks.id);
            }
        });

        ll_classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).selectItem(ConstantVariable.Category.Classes.id);
            }
        });

        ll_exams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).selectItem(ConstantVariable.Category.Exams.id);
            }
        });

        return rootView;
    }

    private void fillCards() {
        Calendar c_tomorrow = Calendar.getInstance();
        Calendar c_today = Calendar.getInstance();

        c_tomorrow.add(Calendar.DATE, 1);
        c_tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        c_tomorrow.set(Calendar.MINUTE, 0);
        c_tomorrow.set(Calendar.SECOND, 0);

        c_today.set(Calendar.HOUR_OF_DAY, 0);
        c_today.set(Calendar.MINUTE, 0);
        c_today.set(Calendar.SECOND, 0);

        long start = c_today.getTimeInMillis();
        long end = c_tomorrow.getTimeInMillis();

        List<Task> tasks = new TaskDao().getTasksOnDate(start, end);
        List<Course> courses = getTodayCourses(start, end, c_today.get(Calendar.DAY_OF_WEEK));
        List<Exam> exams = new ExamDao().getExamsOnDate(start, end);

        tv_tasks.setText(tasks.size() + " " + getString(R.string.dashboard_task));
        tv_classes.setText(courses.size() + " " + getString(R.string.dashboard_class));
        tv_exams.setText(exams.size() + " " + getString(R.string.dashboard_exam));
    }

    private List<Course> getTodayCourses(long startDate, long endDate, int dayOfWeek) {
        List<CourseTimeDay> ctd = new CourseTimeDayDao().getCoursesTimesOnDate(startDate, endDate, dayOfWeek);
        List<Course> courses = new ArrayList<Course>();
        for (CourseTimeDay c : ctd) {
            if (c.Course != null && (startDate >= c.Course.StartDate.getTime()) &&
                    (endDate < c.Course.EndDate.getTime()))
                courses.add(c.Course);
        }
        return courses;
    }

    private void populateFragments() {
        frags = new ArrayList<Fragment>();
        frags.add(new Calender_ExamFragment());
        frags.add(new Calender_CourseFragment());
        frags.add(new Calender_TasksFragment());
    }

    private void fillTabs() {
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator_dashboard, android.R.id.text1);
        mSlidingTabLayout.setContentDescription(1, tabs[0]);
        mSlidingTabLayout.setContentDescription(2, tabs[1]);
        mSlidingTabLayout.setContentDescription(2, tabs[2]);

        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimary));

        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Calendar calendar = Calendar.getInstance();
                updateCurrentFragment(calendar.getTime().getTime(), calendar.get(Calendar.DAY_OF_WEEK));
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
            Calendar calendar = Calendar.getInstance();
            Fragment fragment = frags.get(position);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

    }

    private void updateCurrentFragment(long date, int dayOfWeek) {
        Fragment f = getChildFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.view_pager_dashboard + ":" + mViewPager.getCurrentItem());
        if (mViewPager.getCurrentItem() == 0 && f != null) {
            ((Calender_ExamFragment) f).populateExamsList(date);
        } else if (mViewPager.getCurrentItem() == 1 && f != null) {
            ((Calender_CourseFragment) f).populateCoursesList(date, dayOfWeek);
        } else if (mViewPager.getCurrentItem() == 2 && f != null) {
            ((Calender_TasksFragment) f).populateTasksList(date);
        }
    }
}
