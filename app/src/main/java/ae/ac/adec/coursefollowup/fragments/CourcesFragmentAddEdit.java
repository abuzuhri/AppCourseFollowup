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
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.dal.CourseTimeDayDao;
import ae.ac.adec.coursefollowup.db.dal.HolidayDao;
import ae.ac.adec.coursefollowup.db.dal.SemesterDao;
import ae.ac.adec.coursefollowup.db.dal.YearDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Semesters;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Times;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Years;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by Tareq on 03/05/2015.
 */
public class CourcesFragmentAddEdit extends BaseFragment {

    MaterialEditText courseName = null;
    MaterialEditText startDate = null;
    MaterialEditText endDate = null;
    MaterialEditText semester = null;
    MaterialEditText code = null;
    MaterialEditText building = null;
    MaterialEditText room = null;
    MaterialEditText teacher = null;
    MaterialEditText colorCode = null;
    MaterialEditText times = null;

    CustomDialogClass dialogClass = null;
    private Semester current_semester;
    private int position;


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
            setSubTitle(getString(R.string.course_edit_subtitle));
        else setSubTitle(getString(R.string.course_add_subtitle));
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
            CourseDao courseDao = new CourseDao();


            // BR BR_CRS_009
            if (courseName.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_CRS_009);
            // BR BR_CRS_008
            if (startDate.getText().toString().trim().equals("") || endDate.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_CRS_008);
            if (ID != null && ID != 0)
                current_semester = Course.load(Course.class, ID).Semester;
            // BR BR_CRS_010
            if (current_semester == null)
                throw new BusinessRoleError(R.string.BR_CRS_010);
            // BR BR_CRS_011
            if (colorCode.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_CRS_011);


            long startDateMil = (long) startDate.getTag();
            long endDateMil = (long) endDate.getTag();

            if (ID != null && ID != 0) {
                courseDao.Edit(ID, courseName.getText().toString().trim(), code.getText().toString().trim(), room.getText().toString().trim(),
                        building.getText().toString().trim(), teacher.getText().toString().trim(), colorCode.getText().toString().trim(), current_semester
                        , startDateMil, endDateMil, true);
            } else {
                courseDao.Add(courseName.getText().toString().trim(), code.getText().toString().trim(), room.getText().toString().trim(),
                        building.getText().toString().trim(), teacher.getText().toString().trim(), colorCode.getText().toString().trim(), current_semester
                        , startDateMil, endDateMil, true);
            }
            getActivity().finish();
            Toast.makeText(getActivity(), R.string.holiday_add_successfully, Toast.LENGTH_LONG).show();
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
            Course course = Course.load(Course.class, ID);
            courseName.setText(course.Name);
            startDate.setText(ConstantVariable.getDateString(course.StartDate));
            startDate.setTag(course.StartDate.getTime());
            endDate.setText(ConstantVariable.getDateString(course.EndDate));
            endDate.setTag(course.EndDate.getTime());
            semester.setText(course.Semester.Name);
            code.setText(course.Code);
            room.setText(course.Room);
            building.setText(course.Building);
            teacher.setText(course.Teacher);
            colorCode.setText(course.ColorCode);
        } else {
            List<Year> years = new YearDao().getCurrentYear(System.currentTimeMillis());
            if (years.size() > 0) {
                List<Semester> sems = new SemesterDao().getCurrentSemesters(System.currentTimeMillis(), years.get(0));
                if (sems.size() > 0) {
                    current_semester = sems.get(0);
                    semester.setText(current_semester.Name);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course_add_edit, container, false);
        removeShadowForNewApi21(rootView);

        courseName = (MaterialEditText) rootView.findViewById(R.id.tv_course_nameCode);
        // Start Date
        startDate = (MaterialEditText) rootView.findViewById(R.id.tv_course_startDate);
        SetDateControl(startDate);
        // End Date
        endDate = (MaterialEditText) rootView.findViewById(R.id.tv_course_endDate);
        SetDateControl(endDate);

        semester = (MaterialEditText) rootView.findViewById(R.id.tv_course_semmester);
        code = (MaterialEditText) rootView.findViewById(R.id.tv_course_code);
        building = (MaterialEditText) rootView.findViewById(R.id.tv_course_building);
        room = (MaterialEditText) rootView.findViewById(R.id.tv_course_room);
        teacher = (MaterialEditText) rootView.findViewById(R.id.tv_course_teacher);
        colorCode = (MaterialEditText) rootView.findViewById(R.id.tv_course_colorCode);
        times = (MaterialEditText) rootView.findViewById(R.id.tv_course_times);

        semester.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final List<Semester> semesters = new SemesterDao().getAll(position);
                    final CustomLVAdapter_Semesters adapter = new CustomLVAdapter_Semesters(getActivity(), semesters);
                    dialogClass = new CustomDialogClass(getActivity(), SemesterFragmentAddEdit.class.getName(), "Semesters",
                            adapter,false, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            current_semester = ((Semester) adapter.getItem(position));
                            semester.setText(current_semester.Name);
                            dialogClass.dismiss();
                        }
                    });
                    dialogClass.show(getActivity().getFragmentManager(), "jma");
                }
                v.clearFocus();
            }
        });

        times.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final List<CourseTimeDay> tim = new CourseTimeDayDao().getAll(position);
                    final CustomLVAdapter_Times adapter = new CustomLVAdapter_Times(getActivity(), tim);
                    dialogClass = new CustomDialogClass(getActivity(), DayTimeFragmentAddEdit.class.getName(), "Times",
                            adapter,false, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            times.setText(((CourseTimeDay) adapter.getItem(position)).DayOfWeek+"");
                            dialogClass.dismiss();
                        }
                    });
                    dialogClass.show(getActivity().getFragmentManager(), "jma");
                }
                v.clearFocus();
            }
        });
        fillDate();

        return rootView;
    }


}
