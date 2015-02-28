package ae.ac.adec.coursefollowup.fragments;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;


/**
 * Created by Tareq on 02/28/2015.
 */
public class SemesterFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.semester_fragment, container, false);
        TextView view = (TextView) rootView.findViewById(R.id.section_label);
        view.setTextColor(Color.RED);
        view.setText("ssss");

        setText("SemesterFragment");
        return rootView;
    }

    public void setText(String item) {
        TextView view = (TextView) getView().findViewById(R.id.section_label);
        view.setTextColor(Color.RED);
        view.setText(item);
    }

}
