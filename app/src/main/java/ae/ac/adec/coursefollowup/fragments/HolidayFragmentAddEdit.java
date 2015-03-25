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

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.HolidayDao;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by Tareq on 03/05/2015.
 */
public class HolidayFragmentAddEdit extends BaseFragment {

    MaterialEditText holidayName=null;
    MaterialEditText startDate=null;
    MaterialEditText endDate=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(ID!=null && ID!=0)
            setSubTitle(getString(R.string.holiday_edit_subtitle));
        else setSubTitle(getString(R.string.holiday_add_subtitle));
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
                break;
            default:
                break;
        }
        return true;
    }

    public void AddEdit(){
        try {
            AppLog.i("ID== >>> " + ID);
            HolidayDao holiday = new HolidayDao();

            // BR BR_AUH_005
            if (holidayName.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_HLD_005);
            // BR BR_AUH_004
            if (startDate.getText().toString().trim().equals("") || endDate.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_HLD_004);

            long startDateMil = (long) startDate.getTag();
            long endDateMil = (long) endDate.getTag();

            if(ID!=null && ID!=0)
                holiday.Edit(ID, holidayName.getText().toString().trim(), startDateMil, endDateMil);
            else
                holiday.Add(holidayName.getText().toString().trim(), startDateMil, endDateMil);

            getActivity().finish();
            Toast.makeText(getActivity(),R.string.holiday_add_successfully,Toast.LENGTH_LONG).show();
        }catch (BusinessRoleError ex){
            AppAction.DiaplayError(getActivity(), ex.getMessage());
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private  void fillDate(){
        if(ID!=null && ID!=0){
            Holiday holiday= Holiday.load(Holiday.class, ID);
            holidayName.setText(holiday.Name);
            startDate.setText(ConstantVariable.getDateString(holiday.StartDate));
            startDate.setTag(holiday.StartDate.getTime());
            endDate.setText(ConstantVariable.getDateString(holiday.EndDate));
            endDate.setTag(holiday.EndDate.getTime());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_holiday_add_edit, container, false);
        removeShadowForNewApi21(rootView);

        //Holiday Name
        holidayName= (MaterialEditText) rootView.findViewById(R.id.txtName);

        // Start Date
        startDate= (MaterialEditText) rootView.findViewById(R.id.txtStartDate);
        SetDateControl(startDate);

        // End Date
        endDate= (MaterialEditText) rootView.findViewById(R.id.txtEndDate);
        SetDateControl(endDate);

        fillDate();

        return rootView;
    }



}
