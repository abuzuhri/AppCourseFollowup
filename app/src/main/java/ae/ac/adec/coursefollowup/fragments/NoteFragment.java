package ae.ac.adec.coursefollowup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ae.ac.adec.coursefollowup.R;

/**
 * Created by Tareq on 02/28/2015.
 */
public class NoteFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setText(rootView,"NoteFragment");
        return rootView;
    }
}
