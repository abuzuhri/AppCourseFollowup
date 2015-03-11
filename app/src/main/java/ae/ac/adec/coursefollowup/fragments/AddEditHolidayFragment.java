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
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.HolidayDao;

/**
 * Created by Tareq on 03/05/2015.
 */
public class AddEditHolidayFragment extends BaseFragment {

    MaterialEditText holidayName=null;
    MaterialEditText startDate=null;
    MaterialEditText endDate=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSubTitle("Add Holiday");
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
        try {
            HolidayDao holiday = new HolidayDao();
            long startDateMil = (long) startDate.getTag();
            long endDateMil = (long) endDate.getTag();
            holiday.add(holidayName.getText().toString(), startDateMil, endDateMil);
            getActivity().finish();
            Toast.makeText(getActivity(),R.string.holiday_add_successfully,Toast.LENGTH_LONG).show();
        }catch (Exception ex){

        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_edit_holiday, container, false);

        //Holiday Name
        holidayName= (MaterialEditText) rootView.findViewById(R.id.txtholidayName);

        // Start Date
        startDate= (MaterialEditText) rootView.findViewById(R.id.txtStartDate);
        SetDateControl(startDate);


        // End Date
        endDate= (MaterialEditText) rootView.findViewById(R.id.txtEndDate);
        SetDateControl(endDate);

        return rootView;
    }



}
