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

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.dal.HolidayDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.services.dailogs.AppDialog;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Times;
import ae.ac.adec.coursefollowup.views.event.IDialogClick;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Tareq on 03/13/2015.
 */
public class CourcesFragmentView extends BaseFragment {

    MaterialEditText courseName=null;
    MaterialEditText startDate=null;
    MaterialEditText endDate=null;
    MaterialEditText semester=null;
    MaterialEditText code=null;
    MaterialEditText building=null;
    MaterialEditText room=null;
    MaterialEditText teacher=null;
    MaterialEditText colorCode=null;
    MaterialEditText times = null;
    CustomDialogClass dialogClass = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideSoftKeyboard();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ic_menu_delete:
                AppDialog.Delete(getActivity(), new IDialogClick() {
                    @Override
                    public void onConfirm() {
                        Delete();
                    }
                });
                return true;
            case R.id.ic_menu_edit:
                Edit();
                return true;
            default:
                break;
        }

        return false;
    }

    public void Edit() {
        try {
            AppAction.OpenActivityWithFRAGMENT(getActivity(), CourcesFragmentAddEdit.class.getName(), ID);
            getActivity().finish();
        } catch (Exception ex) {
            Crouton.makeText(getActivity(), ex.getMessage(), Style.ALERT).show();
        }
    }

    public void Delete() {
        try {
            CourseDao courseDao = new CourseDao();
            courseDao.delete(ID);
            getActivity().finish();
            Toast.makeText(getActivity(), R.string.delete_successfully, Toast.LENGTH_LONG).show();
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
            Course course= Course.load(Course.class, ID);
            courseName.setText(course.Name);
            courseName.setEnabled(false);
            startDate.setText(ConstantVariable.getDateString(course.StartDate));
            startDate.setTag(course.StartDate.getTime());
            startDate.setEnabled(false);
            endDate.setText(ConstantVariable.getDateString(course.EndDate));
            endDate.setTag(course.EndDate.getTime());
            endDate.setEnabled(false);
            semester.setText(course.Semester.Name);
            semester.setEnabled(false);
            code.setText(course.Code);
            code.setEnabled(false);
            room.setText(course.Room);
            room.setEnabled(false);
            building.setText(course.Building);
            building.setEnabled(false);
            teacher.setText(course.Teacher);
            teacher.setEnabled(false);
            colorCode.setText(course.ColorCode);
            colorCode.setEnabled(false);
            times.setText(R.string.click_to_show_times);
            times.setVisibility(View.GONE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course_add_edit, container, false);
        removeShadowForNewApi21(rootView);

        courseName = (MaterialEditText) rootView.findViewById(R.id.tv_course_nameCode);
        // Start Date
        startDate= (MaterialEditText) rootView.findViewById(R.id.tv_course_startDate);
        SetDateControl(startDate);
        // End Date
        endDate= (MaterialEditText) rootView.findViewById(R.id.tv_course_endDate);
        SetDateControl(endDate);

        semester = (MaterialEditText) rootView.findViewById(R.id.tv_course_semmester);
        code = (MaterialEditText) rootView.findViewById(R.id.tv_course_code);
        building = (MaterialEditText) rootView.findViewById(R.id.tv_course_building);
        room = (MaterialEditText) rootView.findViewById(R.id.tv_course_room);
        teacher = (MaterialEditText) rootView.findViewById(R.id.tv_course_teacher);
        colorCode = (MaterialEditText) rootView.findViewById(R.id.tv_course_colorCode);
        times = (MaterialEditText) rootView.findViewById(R.id.tv_course_times);

        fillDate();

        return rootView;
    }


}
