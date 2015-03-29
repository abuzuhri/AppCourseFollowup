package ae.ac.adec.coursefollowup.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.fragments.Utils.DatePickerFragment;
import ae.ac.adec.coursefollowup.fragments.Utils.IDateTimePickerResult;
import ae.ac.adec.coursefollowup.fragments.Utils.TimePickerFragment;
import ae.ac.adec.coursefollowup.services.AppAction;

/**
 * Created by Tareq on 02/28/2015.
 */
public class BaseFragment extends Fragment {

    public Long ID;
    public static final String POSITION = "POSITION";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set Item ID
        if (getArguments() != null) {
            ID = getArguments().getLong(AppAction.IDEXTRA, 0);
        }
    }

    public void setSubTitle(String txt) {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(txt);
    }

    public void setText(View rootView, String item) {
        TextView view = (TextView) rootView.findViewById(R.id.section_label);
        view.setText(item);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void OpenDatePicker(IDateTimePickerResult dateTimePickerResult) {
        DatePickerFragment newDate = new DatePickerFragment();
        newDate.show(getActivity().getSupportFragmentManager(), "datePicker", dateTimePickerResult);
    }

    public void OpenTimePicker(IDateTimePickerResult dateTimePickerResult) {
        TimePickerFragment newTime = new TimePickerFragment();
        newTime.show(getActivity().getSupportFragmentManager(), "timePicker", dateTimePickerResult);
    }

    public void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void SetDateControl(final MaterialEditText dateControl) {
        dateControl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    OpenDatePicker(new IDateTimePickerResult() {
                        @Override
                        public void onDatePickerSubmit(int year, int month, int day, String tag) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day);
                            dateControl.setText(ConstantVariable.getDateString(calendar.getTime()));
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
    public void SetTimeControl(final MaterialEditText dateControl) {
        dateControl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    OpenTimePicker(new IDateTimePickerResult() {
                        @Override
                        public void onDatePickerSubmit(int year, int month, int day, String tag) {
                            /*Calendar calendar = Calendar.getInstance();
                            calendar.set(year, month, day);
                            dateControl.setText(ConstantVariable.getDateString(calendar.getTime()));
                            dateControl.setTag(calendar.getTimeInMillis());*/
                        }

                        @Override
                        public void onTimePickerSubmit(int hourOfDay, int minute, String tag) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                            calendar.set(Calendar.MINUTE,minute);
                            dateControl.setText(ConstantVariable.getTimeString(calendar.getTime()));
                            dateControl.setTag(calendar.getTimeInMillis());
                        }
                    });
                }
                v.clearFocus();
            }
        });

    }
    public void removeShadowForNewApi21(View rootView) {
        View shadowView = rootView.findViewById(R.id.shadow_main_activity);
        // Solve Android bug in API < 21 by app custom shadow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            shadowView.setVisibility(View.GONE);
    }
}
