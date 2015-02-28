package ae.ac.adec.coursefollowup.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;


/**
 * Created by Tareq on 02/28/2015.
 */
public class DashboardFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dashboard_fragment, container, false);
        TextView view = (TextView) rootView.findViewById(R.id.section_label);
        setText("DashboardFragment");

        return rootView;
    }


    public void setText(String item) {
        TextView view = (TextView) getView().findViewById(R.id.section_label);
        view.setText(item);
    }
}
