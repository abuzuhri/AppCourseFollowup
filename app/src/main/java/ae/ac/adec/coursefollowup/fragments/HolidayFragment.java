package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.db.dal.HolidayDao;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.views.adapters.HolidayAdapter;

/**
 * Created by Tareq on 03/03/2015.
 */
public class HolidayFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position= getArguments().getInt(POSITION,0);
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

    private void FillDate(){
        HolidayDao holidayDao=new HolidayDao();
        List<Holiday> holidayList= holidayDao.getAll(position);
        mAdapter = new HolidayAdapter(holidayList,getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_holiday, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        FillDate();
        //mRecyclerView.setItemAnimator(new FeedItemAnimator());

        FloatingActionButton fab=(FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OneFragmentActivity.class);
                intent.putExtra(OneFragmentActivity.FRAGMENT, HolidayFragmentAddEdit.class.getName());
                startActivity(intent);
            }
        });


        return rootView;
    }

    /*public void onSomeButtonClicked(View view) {
       getActivity(). getWindow().setExitTransition(new Explode());
        Intent intent = new Intent(getActivity(), OneFragmentActivity.class);
        intent.putExtra(OneFragmentActivity.FRAGMENT, AddEditHolidayFragment.class.getName());
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }*/
}
