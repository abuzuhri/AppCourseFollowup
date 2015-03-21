package ae.ac.adec.coursefollowup.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.HolidayDao;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.views.adapters.HolidayAdapter;
import ae.ac.adec.coursefollowup.views.event.IClickCardView;

/**
 * Created by Tareq on 02/28/2015.
 */
public class CoursesFragment extends BaseFragment {

    private int position;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position= getArguments().getInt(POSITION,0);
    }

    /*@Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                FillDate();
            }
        }, 500);
    }
    private void FillDate(){
        HolidayDao holidayDao=new HolidayDao();
        List<Holiday> holidayList= holidayDao.getAll(position);
        mAdapter = new HolidayAdapter(holidayList,getActivity(),new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                AppAction.OpenActivityWithFRAGMENT(getActivity(), HolidayFragmentView.class.getName(), ID);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setText(rootView,"CoursesFragment");
        return rootView;
    }
}
