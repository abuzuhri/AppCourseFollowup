package ae.ac.adec.coursefollowup.fragments.Utils;

/**
 * Created by Tareq on 03/07/2015.
 */
public interface IDateTimePickerResult {
    public void onDatePickerSubmit(int year, int month, int day, String tag);
    public void onTimePickerSubmit(int hourOfDay, int minute, String tag);
}
