package ae.ac.adec.coursefollowup.fragments.Utils;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
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

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), this, year, month, day);
       //if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
       //    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
       //}
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,day);
        DateTimePickerResult.onDatePickerSubmit(year, month,  day,tag);
    }
}