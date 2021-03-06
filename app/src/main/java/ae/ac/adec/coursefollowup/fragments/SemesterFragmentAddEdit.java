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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.SemesterDao;
import ae.ac.adec.coursefollowup.db.dal.YearDao;
import ae.ac.adec.coursefollowup.db.models.Semester;

import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Courses;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Years;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by MyLabtop on 3/21/2015.
 */
public class SemesterFragmentAddEdit extends BaseFragment {

    MaterialEditText semesterName = null;
    MaterialEditText startDate = null;
    MaterialEditText endDate = null;
    MaterialEditText selectYear = null;
    Spinner s = null;
    Year selectedYear = null;
    private int position;
    CustomDialogClass dialogClass = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(POSITION, 0);
        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ID != null && ID != 0)
            setSubTitle(getString(R.string.semester_edit_subtitle));
        else setSubTitle(getString(R.string.semester_add_subtitle));
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
            SemesterDao semester = new SemesterDao();

            // BR BR_SMR_006
            if (semesterName.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_SMR_006);
            // BR BR_SMR_005
            if (startDate.getText().toString().trim().equals("") || endDate.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_SMR_005);
            // BR BR_SMR_007
            if (selectedYear == null)
                throw new BusinessRoleError(R.string.BR_SMR_007);

            long startDateMil = (long) startDate.getTag();
            long endDateMil = (long) endDate.getTag();
            //ToDo
            //Check selectedYear not null
            AppLog.i("1 : " + selectedYear.Name);
            if (ID != null && ID != 0) {
                semester.Edit(ID, semesterName.getText().toString().trim(), startDateMil, endDateMil, selectedYear);
                Toast.makeText(getActivity(), R.string.semester_edit_successfully, Toast.LENGTH_LONG).show();
            } else {
                semester.Add(semesterName.getText().toString().trim(), startDateMil, endDateMil, selectedYear);
                Toast.makeText(getActivity(), R.string.semester_add_successfully, Toast.LENGTH_LONG).show();
            }

            getActivity().finish();
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
            Semester semester = Semester.load(Semester.class, ID);
            semesterName.setText(semester.Name);
            startDate.setText(ConstantVariable.getDateString(semester.StartDate));
            startDate.setTag(semester.StartDate.getTime());
            endDate.setText(ConstantVariable.getDateString(semester.EndDate));
            endDate.setTag(semester.EndDate.getTime());
            selectYear.setText(semester.year.Name);
            selectedYear = semester.year;
        } else {
            List<Year> years = new YearDao().getCurrentYear(System.currentTimeMillis());
            if (years.size() > 0) {
                selectedYear = years.get(0);
                selectYear.setText(selectedYear.Name);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_semester_add_edit, container, false);
        removeShadowForNewApi21(rootView);


        semesterName = (MaterialEditText) rootView.findViewById(R.id.txtName);
        semesterName.setTypeface(tf_roboto_light);
        // Start Date
        startDate = (MaterialEditText) rootView.findViewById(R.id.txtStartDate);
        SetDateControl_New(startDate);
        startDate.setTypeface(tf_roboto_light);

        // End Date
        endDate = (MaterialEditText) rootView.findViewById(R.id.txtEndDate);
        SetDateControl_New(endDate);
        endDate.setTypeface(tf_roboto_light);

        selectYear = (MaterialEditText) rootView.findViewById(R.id.txtSelectYear);
        selectYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    List<Year> years = new YearDao().getAll(position);
                    final CustomLVAdapter_Years adapter = new CustomLVAdapter_Years(getActivity(), years);

                    dialogClass = new CustomDialogClass(getActivity(), YearFragmentAddEdit.class.getName(), "Years",
                            adapter, false, -1, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            selectYear.setText(((Year) adapter.getItem(position)).Name);
                            selectedYear = ((Year) adapter.getItem(position));
                            AppLog.i("HHHHH : " + selectedYear.Name);
                            // dialogClass.getActivity().finish();
                            dialogClass.dismiss();
                        }
                    });
                    dialogClass.show(getActivity().getFragmentManager(), "Iam here!");
                }
                v.clearFocus();
            }
        });

        fillDate();

        return rootView;
    }


}
