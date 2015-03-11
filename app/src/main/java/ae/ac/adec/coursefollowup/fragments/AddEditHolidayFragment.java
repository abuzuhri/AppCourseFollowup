package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.fragments.Utils.IDateTimePickerResult;

/**
 * Created by Tareq on 03/05/2015.
 */
public class AddEditHolidayFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSubTitle("Add Semester");
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ic_menu_save_menu:
                AddEdit();


                return true;
            default:
                break;
        }

        return false;
    }
    public void AddEdit(){

    }

    public void SetDateControl(final MaterialEditText dateControl){
        OpenDatePicker(new IDateTimePickerResult() {
            @Override
            public void onDatePickerSubmit(int year, int month, int day, String tag) {
                Date date = new Date(year,month,day);
                dateControl.setText(SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.ENGLISH).format(date).toUpperCase());
                dateControl.setTag(date);
                //dateControl.setText("Year "+year);
                //Toast.makeText(getActivity(),tag+"  year "+year,Toast.LENGTH_LONG).show();;
            }

            @Override
            public void onTimePickerSubmit(int hourOfDay, int minute, String tag) {
                //Toast.makeText(getActivity(),tag,Toast.LENGTH_LONG).show();;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_edit_holiday, container, false);
        final MaterialEditText startDate= (MaterialEditText) rootView.findViewById(R.id.txtStartDate);
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    SetDateControl(startDate);
                }
                v.clearFocus();
            }
        });
        return rootView;
    }



}
