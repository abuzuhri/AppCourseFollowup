package ae.ac.adec.coursefollowup.fragments;

import android.app.Fragment;
import android.os.Bundle;
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
        view.setText("fgd fgdf");
        return rootView;
    }

}
