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
import android.widget.CheckBox;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.dal.ExamDao;
import ae.ac.adec.coursefollowup.db.dal.HolidayDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Courses;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by Tareq on 03/05/2015.
 */
public class ExamFragmentAddEdit extends BaseFragment {

    MaterialEditText courseName = null;
    CheckBox isResit = null;
    MaterialEditText seat = null;
    MaterialEditText room = null;
    MaterialEditText startDateTime = null;
    MaterialEditText endDateTime = null;
    MaterialEditText examDate = null;
    Course current_course;
    CustomDialogClass dialog;

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
            setSubTitle(getString(R.string.exam_edit_subtitle));
        else setSubTitle(getString(R.string.exam_add_subtitle));
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
            ExamDao examDao = new ExamDao();

            // BR BR_EXM_002
            if (current_course == null)
                throw new BusinessRoleError(R.string.BR_EXM_002);
            // BR BR_EXM_001
            if (startDateTime.getText().toString().trim().equals("") || endDateTime.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_EXM_001);


            long startDateMil = (long) startDateTime.getTag();
            long endDateMil = (long) endDateTime.getTag();

            if (ID != null && ID != 0) {
                examDao.Edit(ID, startDateMil, endDateMil, System.currentTimeMillis(), room.getText().toString(),
                        seat.getText().toString(), isResit.isChecked(), current_course);
                Toast.makeText(getActivity(), R.string.exam_edit_successfully, Toast.LENGTH_LONG).show();
            } else {
                examDao.Add(startDateMil, endDateMil, System.currentTimeMillis(), room.getText().toString(),
                        seat.getText().toString(), isResit.isChecked(), current_course);
                Toast.makeText(getActivity(), R.string.exam_add_successfully, Toast.LENGTH_LONG).show();
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
            Exam exam = Exam.load(Exam.class, ID);

            current_course = exam.Course;

            courseName.setText(exam.Course.Name);
            startDateTime.setText(ConstantVariable.getTimeString(exam.StartDateTime));
            startDateTime.setTag(exam.StartDateTime.getTime());
            endDateTime.setText(ConstantVariable.getTimeString(exam.EndDateTime));
            endDateTime.setTag(exam.EndDateTime.getTime());
            examDate.setText(ConstantVariable.getDateString(exam.EndDateTime));
            examDate.setTag(exam.EndDateTime.getTime());
            seat.setText(exam.Seat);
            room.setText(exam.Room);
            isResit.setChecked(exam.IsResit);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exam_view, container, false);
        removeShadowForNewApi21(rootView);

        courseName = (MaterialEditText) rootView.findViewById(R.id.tv_exam_course_name);
        courseName.setTypeface(tf_roboto_light);
        seat = (MaterialEditText) rootView.findViewById(R.id.tv_exam_seat);
        seat.setTypeface(tf_roboto_light);
        room = (MaterialEditText) rootView.findViewById(R.id.tv_exam_room);
        room.setTypeface(tf_roboto_light);
        isResit = (CheckBox) rootView.findViewById(R.id.cb_exam_isResit);

        examDate = (MaterialEditText) rootView.findViewById(R.id.tv_exam_date);
        examDate.setTypeface(tf_roboto_light);
        SetDateControl_New(examDate);

        startDateTime = (MaterialEditText) rootView.findViewById(R.id.tv_exam_startTime);
        SetTimeControl(startDateTime, examDate);
        startDateTime.setTypeface(tf_roboto_light);
        endDateTime = (MaterialEditText) rootView.findViewById(R.id.tv_exam_endTime);
        SetTimeControl(endDateTime, examDate);
        endDateTime.setTypeface(tf_roboto_light);


        courseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Course> courses = new CourseDao().getAll(2);
                final CustomLVAdapter_Courses adapter = new CustomLVAdapter_Courses(getActivity(), courses);
                dialog = new CustomDialogClass(getActivity(), CourcesFragmentAddEdit.class.getName(),
                        getString(R.string.select_course), adapter, false, -1, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        current_course = (Course) adapter.getItem(position);
                        courseName.setText(current_course.Name);
                        dialog.dismiss();
                    }
                });
                dialog.show(getActivity().getFragmentManager(), "jma");
            }
        });

        fillDate();

        return rootView;
    }


}
