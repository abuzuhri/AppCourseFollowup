package ae.ac.adec.coursefollowup.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.fragments.Utils.DatePickerFragment;
import ae.ac.adec.coursefollowup.fragments.Utils.IDateTimePickerResult;
import ae.ac.adec.coursefollowup.fragments.Utils.TimePickerFragment;

/**
 * Created by Tareq on 02/28/2015.
 */
public class BaseFragment extends Fragment  {


    public void setSubTitle(String txt){
        ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(txt);
    }
    public void setText(View rootView,String item) {
        TextView view = (TextView) rootView.findViewById(R.id.section_label);
        view.setText(item);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public  void OpenDatePicker(IDateTimePickerResult dateTimePickerResult) {
        DatePickerFragment newDate = new DatePickerFragment();
        newDate.show(getActivity().getSupportFragmentManager(), "datePicker", dateTimePickerResult);
    }
    public  void OpenTimePicker(IDateTimePickerResult dateTimePickerResult) {
        TimePickerFragment newTime = new TimePickerFragment();
        newTime.show(getActivity().getSupportFragmentManager(), "timePicker", dateTimePickerResult);
    }


}
