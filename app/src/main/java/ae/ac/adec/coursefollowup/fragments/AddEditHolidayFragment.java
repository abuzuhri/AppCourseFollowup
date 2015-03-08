package ae.ac.adec.coursefollowup.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.fragments.Utils.DatePickerFragment;
import ae.ac.adec.coursefollowup.fragments.Utils.IDateTimePickerResult;
import ae.ac.adec.coursefollowup.fragments.Utils.TimePickerFragment;

/**
 * Created by Tareq on 03/05/2015.
 */
public class AddEditHolidayFragment extends BaseFragment implements IDateTimePickerResult ,DatePickerDialog.OnDateSetListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle("Add Semester");
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main2, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings2:

                DatePickerFragment newDate = new DatePickerFragment();
                newDate.show(getActivity().getSupportFragmentManager(), "datePicker",this);

                //startActivityForResult(ss, 999);
                //getActivity().finish();
                // Not implemented here
                return false;
            case R.id.action_settings3:
                TimePickerFragment newTime = new TimePickerFragment();
                newTime.show(getActivity().getSupportFragmentManager(), "timePicker", this);
                //getActivity().finish();
                // Do Fragment menu item stuff here
                return true;
            case R.id.action_settings4:
                //final Calendar c = Calendar.getInstance();
                //int year = c.get(Calendar.YEAR);
                //int month = c.get(Calendar.MONTH);
                //int day = c.get(Calendar.DAY_OF_MONTH);
               // new DatePickerDialog(getActivity(), this, year, month, day).show();
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete entry").show();

                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_edit_holiday, container, false);
        return rootView;
    }

    @Override
    public void onDatePickerSubmit(int year, int month, int day, String tag) {
        Toast.makeText(getActivity(),tag+"  year "+year,Toast.LENGTH_LONG).show();;
    }

    @Override
    public void onTimePickerSubmit(int hourOfDay, int minute, String tag) {
        Toast.makeText(getActivity(),tag,Toast.LENGTH_LONG).show();;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }
}
