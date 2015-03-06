package ae.ac.adec.coursefollowup.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import butterknife.ButterKnife;

/**
 * Created by Tareq on 02/28/2015.
 */
public class BaseFragment extends Fragment {


    public void setText(View rootView,String item) {
        TextView view = (TextView) rootView.findViewById(R.id.section_label);
        view.setText(item);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
