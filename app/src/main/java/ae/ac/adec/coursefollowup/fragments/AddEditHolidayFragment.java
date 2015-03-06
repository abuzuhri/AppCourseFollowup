package ae.ac.adec.coursefollowup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ae.ac.adec.coursefollowup.R;

/**
 * Created by Tareq on 03/05/2015.
 */
public class AddEditHolidayFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_edit_holiday, container, false);
        //setText(rootView,"DashboardFragment");

        return rootView;
    }
}
