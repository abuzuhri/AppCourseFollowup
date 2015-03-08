package ae.ac.adec.coursefollowup.fragments.Utils;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Tareq on 03/07/2015.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    IDateTimePickerResult DateTimePickerResult;
    String tag;

    public void show(FragmentManager manager, String tag,IDateTimePickerResult DateTimePickerResult) {
        super.show(manager, tag);

        this.DateTimePickerResult=DateTimePickerResult;
        this.tag=tag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getActivity().  getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), this, year, month, day);
       if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
           datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
       }
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        DateTimePickerResult.onDatePickerSubmit(year, month,  day,tag);
    }
}