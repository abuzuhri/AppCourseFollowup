package ae.ac.adec.coursefollowup.fragments;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.fragments.Utils.DatePickerFragment;
import ae.ac.adec.coursefollowup.fragments.Utils.IDateTimePickerResult;
import ae.ac.adec.coursefollowup.fragments.Utils.TimePickerFragment;

/**
 * Created by Tareq on 02/28/2015.
 */
public class BaseFragment extends Fragment  {

    public static final String POSITION = "POSITION";

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


    public void SetDateControl(final MaterialEditText dateControl){
        dateControl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    OpenDatePicker(new IDateTimePickerResult() {
                        @Override
                        public void onDatePickerSubmit(int year, int month, int day, String tag) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year,month,day);
                            dateControl.setText(SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, java.util.Locale.getDefault()).format(calendar.getTime()));
                            dateControl.setTag(calendar.getTimeInMillis());
                        }

                        @Override
                        public void onTimePickerSubmit(int hourOfDay, int minute, String tag) {
                            //Toast.makeText(getActivity(),tag,Toast.LENGTH_LONG).show();;
                        }
                    });
                }
                v.clearFocus();
            }
        });

    }

    public void  removeShadowForNewApi21(View rootView){
        View shadowView=rootView.findViewById(R.id.shadow_main_activity);
        // Solve Android bug in API < 21 by app custom shadow
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            shadowView.setVisibility(View.GONE);
    }
}
