package ae.ac.adec.coursefollowup.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.ColorPicker;
import ae.ac.adec.coursefollowup.ConstantApp.CustomColorDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.BaseActivity;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.dal.CourseTimeDayDao;
import ae.ac.adec.coursefollowup.db.dal.HolidayDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.views.adapters.CourseAdapter;
import ae.ac.adec.coursefollowup.views.adapters.HolidayAdapter;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by Tareq on 02/28/2015.
 */
public class CoursesFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(POSITION, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                FillDate();
            }
        }, 500);
    }

    private void FillDate() {
        CourseDao courseDao = new CourseDao();
        List<Course> courses = courseDao.getAll(position);
        mAdapter = new CourseAdapter(courses, tf_roboto_light, getActivity(), new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                AppAction.OpenActivityWithFRAGMENT(getActivity(), CourcesFragmentView.class.getName(), ID);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        FillDate();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, CourcesFragmentAddEdit.class.getName(), -1);
            }
        });

        return rootView;
    }
}
