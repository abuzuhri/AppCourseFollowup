package ae.ac.adec.coursefollowup.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.TaskDao;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.views.adapters.TaskAdapter;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by Tareq on 02/28/2015.
 */
public class Calender_TasksFragment extends BaseFragment {
    RecyclerView lv_tasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_calender_tasks, container, false);

        lv_tasks = (RecyclerView) rootView.findViewById(R.id.my_recycler_view_tasks);

        lv_tasks.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lv_tasks.setLayoutManager(mLayoutManager);

        Calendar calendar = Calendar.getInstance();
        populateTasksList(calendar.getTime().getTime());

        return rootView;
    }

    public void populateTasksList(long date) {

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

        populateTasksList(c_today.getTimeInMillis(), c_tomorrow.getTimeInMillis());
    }

    public void populateTasksList(long startDate, long endDate) {
        List<Task> tasks = new TaskDao().getTasksOnDate(startDate, endDate);
        TaskAdapter tasks_adapter = new TaskAdapter(tasks,tf_roboto_light, getActivity(), new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                AppAction.OpenActivityWithFRAGMENT(getActivity(), TaskFragmentView.class.getName(), ID);
            }
        });
        lv_tasks.setAdapter(tasks_adapter);
    }
}
