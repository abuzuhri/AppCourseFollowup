package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
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

    MaterialEditText courseName = null;
    MaterialEditText startDate = null;
    MaterialEditText endDate = null;
    MaterialEditText semester = null;
    MaterialEditText code = null;
    MaterialEditText building = null;
    MaterialEditText room = null;
    MaterialEditText teacher = null;
    LinearLayout ll_times;
    CardView times_cardView;
    TextView tv_color = null;
    String colorCode;
    Button times = null, btn_color = null;
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
            Course course = Course.load(Course.class, ID);
            courseName.setText(course.Name);
            courseName.setEnabled(false);
            startDate.setText(ConstantVariable.getDateString(course.StartDate));
            startDate.setTag(course.StartDate.getTime());
            startDate.setEnabled(false);
            endDate.setText(ConstantVariable.getDateString(course.EndDate));
            endDate.setTag(course.EndDate.getTime());
            endDate.setEnabled(false);
            if (course.Semester != null)
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
            colorCode = course.ColorCode;
            if (colorCode != null)
                btn_color.setBackgroundColor(Color.parseColor(colorCode));
            btn_color.setEnabled(false);
            times.setText(R.string.click_to_show_times);

            fillTimes(course);
        }
    }

    private void fillTimes(Course course) {
        ConstantVariable.DayOfWeek days[] = ConstantVariable.DayOfWeek.values();
        LinearLayout ll_customView = null, ll_innerTimes, ll_timesRow;
        TextView tv_day, tv_row_date, tv_row_isRepeat;
        CourseTimeDayDao ctdDao = new CourseTimeDayDao();
        List<CourseTimeDay> ctd;
        for (int i = 0; i < days.length; i++) {
            ctd = ctdDao.getTimesByCourse_DayOfWeek(course, days[i].id);
            ll_customView = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.custom_time_view, null);

            tv_day = (TextView) ll_customView.findViewById(R.id.custom_time_tvDay);
            tv_day.setTypeface(tf_roboto_light);
            ll_innerTimes = (LinearLayout) ll_customView.findViewById(R.id.custom_time_ll_times);
            for (CourseTimeDay c : ctd) {
                tv_day.setText(ConstantVariable.DayOfWeek.fromInteger(days[i].id));
                ll_timesRow = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.times_row, null);
                tv_row_date = (TextView) ll_timesRow.findViewById(R.id.tv_timesrow_time);
                tv_row_date.setTypeface(tf_roboto_light);
                tv_row_isRepeat = (TextView) ll_timesRow.findViewById(R.id.tv_timesrow_repeat);
                tv_row_isRepeat.setTypeface(tf_roboto_light);
                tv_row_date.setText(ConstantVariable.getTimeString(c.Start_time) + "-" +
                        ConstantVariable.getTimeString(c.End_time));
                if (c.IsRepeat) {
                    tv_row_isRepeat.setText(getString(R.string.time_repeated));
                } else {
                    tv_row_isRepeat.setText(getString(R.string.once_on) + ConstantVariable.getDateString(c.Start_time));
                }
                ll_innerTimes.addView(ll_timesRow);
            }
            if (ctd.size() > 0)
                ll_times.addView(ll_customView);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course_add_edit, container, false);
        removeShadowForNewApi21(rootView);

        courseName = (MaterialEditText) rootView.findViewById(R.id.tv_course_nameCode);
        courseName.setTypeface(tf_roboto_light);
        // Start Date
        startDate = (MaterialEditText) rootView.findViewById(R.id.tv_course_startDate);
        SetDateControl(startDate);
        startDate.setTypeface(tf_roboto_light);
        // End Date
        endDate = (MaterialEditText) rootView.findViewById(R.id.tv_course_endDate);
        SetDateControl(endDate);
        endDate.setTypeface(tf_roboto_light);

        semester = (MaterialEditText) rootView.findViewById(R.id.tv_course_semmester);
        semester.setTypeface(tf_roboto_light);
        code = (MaterialEditText) rootView.findViewById(R.id.tv_course_code);
        code.setTypeface(tf_roboto_light);
        building = (MaterialEditText) rootView.findViewById(R.id.tv_course_building);
        building.setTypeface(tf_roboto_light);
        room = (MaterialEditText) rootView.findViewById(R.id.tv_course_room);
        room.setTypeface(tf_roboto_light);
        teacher = (MaterialEditText) rootView.findViewById(R.id.tv_course_teacher);
        teacher.setTypeface(tf_roboto_light);
        times = (Button) rootView.findViewById(R.id.tv_course_times);
        times.setVisibility(View.GONE);

        ll_times = (LinearLayout) rootView.findViewById(R.id.ll_courseView_times);

        tv_color = (TextView) rootView.findViewById(R.id.tv_course_color);
        tv_color.setTypeface(tf_roboto_light);
        colorCode = getResources().getStringArray(R.array.colors)[0];
        btn_color = (Button) rootView.findViewById(R.id.btn_course_color);
        if (colorCode != null)
            btn_color.setBackgroundColor(Color.parseColor(colorCode));

        fillDate();

        return rootView;
    }

}
