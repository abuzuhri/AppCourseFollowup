package ae.ac.adec.coursefollowup.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.CourseTimeDayDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.fragments.Utils.IDateTimePickerResult;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Days;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Semesters;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by Tareq on 03/05/2015.
 */
public class DayTimeFragmentAddEdit extends BaseFragment {

    MaterialEditText startTime = null;
    MaterialEditText endTime = null;
    MaterialEditText oneDayDate = null;
    MaterialEditText daysOfWeek = null;
    CheckBox isRepeat = null;
    Course currentCourse;
    CustomDialogClass dialogClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ID != null && ID != 0)
            setSubTitle(getString(R.string.dt_edit_subtitle));
        else setSubTitle(getString(R.string.dt_add_subtitle));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    public void AddEdit() {
        try {
            AppLog.i("ID== >>> " + ID);
            CourseTimeDayDao ctd = new CourseTimeDayDao();
            long crs_id = getActivity().getIntent().getExtras().getLong(AppAction.COURSE_ID);
            // AppLog.i("ID s== >>> " + ID);
            currentCourse = Course.load(Course.class, crs_id);

            // BR BR_DT_001
            if (startTime.getText().toString().trim().equals("") || endTime.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_DT_001);
            // BR BR_DT_002
            if (daysOfWeek.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_DT_002);
            // BR BR_DT_003
            if (currentCourse == null)
                throw new BusinessRoleError(R.string.BR_DT_003);

            long startDateMil = (long) startTime.getTag();
            long endDateMil = (long) endTime.getTag();

            if (ID != null && ID != 0)
                ctd.Edit(ID, currentCourse, startDateMil, endDateMil, isRepeat.isChecked(), 1);
            else
                ctd.Add(currentCourse, startDateMil, endDateMil, isRepeat.isChecked(), 1);

            getActivity().finish();
            Toast.makeText(getActivity(), R.string.dt_add_successfully, Toast.LENGTH_LONG).show();
        } catch (BusinessRoleError ex) {
            AppAction.DiaplayError(getActivity(), ex.getMessage());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private void fillDate() {
        if (ID != null && ID != 0) {
            CourseTimeDay courseTimeDay = CourseTimeDay.load(CourseTimeDay.class, ID);
            startTime.setText(ConstantVariable.getTimeString(courseTimeDay.Start_time));
            startTime.setTag(courseTimeDay.Start_time.getTime());
            endTime.setText(ConstantVariable.getTimeString(courseTimeDay.End_time));
            endTime.setTag(courseTimeDay.End_time.getTime());
            oneDayDate.setText(ConstantVariable.getDateString(courseTimeDay.Start_time));
            oneDayDate.setTag(courseTimeDay.Start_time.getTime());
            daysOfWeek.setText("11");
            isRepeat.setChecked(courseTimeDay.IsRepeat);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_coursetime_add_edit, container, false);
        removeShadowForNewApi21(rootView);


        isRepeat = (CheckBox) rootView.findViewById(R.id.cb_dt_isRepeat);
        daysOfWeek = (MaterialEditText) rootView.findViewById(R.id.tv_dt_daysOfWeek);

        oneDayDate = (MaterialEditText) rootView.findViewById(R.id.tv_dt_onceDate);
        SetDateControl(oneDayDate);

        startTime = (MaterialEditText) rootView.findViewById(R.id.tv_dt_startTime1);
        endTime = (MaterialEditText) rootView.findViewById(R.id.tv_dt_endTime1);


        daysOfWeek.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final ConstantVariable.DayOfWeek[] days = ConstantVariable.DayOfWeek.values();
                    final CustomLVAdapter_Days ad = new CustomLVAdapter_Days(getActivity(), days);
                    AppLog.i(days.length + " marwan");
                    dialogClass = new CustomDialogClass(getActivity(), "", "Select Days", ad, true, -1, true);
                    dialogClass.setClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String res = "";
                            List<Integer> ad_days_inner = ad.days;
                            for (int i = 0; i < ad_days_inner.size(); i++) {
                                res += ConstantVariable.DayOfWeek.fromInteger(ad_days_inner.get(i));
                                if (i != ad_days_inner.size() - 1)
                                    res += ",";
                            }
                            daysOfWeek.setText(res);
                            dialogClass.dismiss();
                        }
                    });
                    dialogClass.show(getActivity().getFragmentManager(), "jma");
                }
                v.clearFocus();
            }
        });

        if (isRepeat.isChecked()) {
            oneDayDate.setVisibility(View.GONE);
            SetTimeControl(startTime);
            SetTimeControl(endTime);
        }
        isRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    oneDayDate.setVisibility(View.VISIBLE);
                    SetTimeControl(startTime, oneDayDate);
                    SetTimeControl(endTime, oneDayDate);
                } else {
                    oneDayDate.setVisibility(View.GONE);
                    SetTimeControl(startTime);
                    SetTimeControl(endTime);
                }
            }
        });

        fillDate();

        return rootView;
    }
}
