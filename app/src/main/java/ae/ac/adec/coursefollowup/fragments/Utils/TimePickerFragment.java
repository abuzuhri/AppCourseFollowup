package ae.ac.adec.coursefollowup.fragments.Utils;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Tareq on 03/07/2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    IDateTimePickerResult DateTimePickerResult;
    String tag;
    Calendar calendar = Calendar.getInstance();

    public void show(FragmentManager manager, String tag,IDateTimePickerResult DateTimePickerResult) {
        super.show(manager, tag);
        this.DateTimePickerResult=DateTimePickerResult;
        this.tag=tag;
    }
    public void show(FragmentManager manager, String tag,Calendar calendar,IDateTimePickerResult DateTimePickerResult) {
        super.show(manager, tag);
        this.DateTimePickerResult=DateTimePickerResult;
        this.tag=tag;
        this.calendar=calendar;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getActivity().  getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog timePickerDialog= new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        //if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        //    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
       // }

        return timePickerDialog;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DateTimePickerResult.onTimePickerSubmit(hourOfDay, minute, tag);
    }
}