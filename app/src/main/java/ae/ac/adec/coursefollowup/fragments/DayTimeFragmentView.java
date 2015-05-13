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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.CourseTimeDayDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.services.dailogs.AppDialog;
import ae.ac.adec.coursefollowup.views.event.IDialogClick;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Tareq on 03/13/2015.
 */
public class DayTimeFragmentView extends BaseFragment {

    MaterialEditText startTime = null;
    MaterialEditText endTime = null;
    MaterialEditText daysOfWeek = null;
    MaterialEditText oneDayDate = null;
    ListView lv_times;
    CheckBox isRepeat = null;

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
            AppAction.OpenActivityWithFRAGMENT(getActivity(), DayTimeFragmentEdit.class.getName(), ID);
            getActivity().finish();
        } catch (Exception ex) {
            Crouton.makeText(getActivity(), ex.getMessage(), Style.ALERT).show();
        }
    }

    public void Delete() {
        try {
            CourseTimeDayDao ctd = new CourseTimeDayDao();
            ctd.delete(ID);

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
            CourseTimeDay ctd = CourseTimeDay.load(CourseTimeDay.class, ID);
            if (ctd != null) {
                startTime.setText(ConstantVariable.getTimeString(ctd.Start_time));
                startTime.setEnabled(false);

                endTime.setText(ConstantVariable.getTimeString(ctd.End_time));
                endTime.setEnabled(false);

                if (!ctd.IsRepeat) {
                    oneDayDate.setText(ConstantVariable.getDateString(ctd.Start_time));
                    oneDayDate.setTag(ctd.Start_time.getTime());
                    oneDayDate.setEnabled(false);
                } else
                    oneDayDate.setVisibility(View.GONE);
                daysOfWeek.setText(ConstantVariable.DayOfWeek.fromInteger(ctd.DayOfWeek));
                daysOfWeek.setEnabled(false);

                isRepeat.setChecked(ctd.IsRepeat);
                isRepeat.setEnabled(false);
            }
            else
                getActivity().finish();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_coursetime_add_edit, container, false);
        removeShadowForNewApi21(rootView);

        isRepeat = (CheckBox) rootView.findViewById(R.id.cb_dt_isRepeat);
        daysOfWeek = (MaterialEditText) rootView.findViewById(R.id.tv_dt_daysOfWeek);
        daysOfWeek.setTypeface(tf_roboto_light);
        startTime = (MaterialEditText) rootView.findViewById(R.id.tv_dt_startTime1);
        startTime.setTypeface(tf_roboto_light);
        SetDateControl(startTime);
        endTime = (MaterialEditText) rootView.findViewById(R.id.tv_dt_endTime1);
        endTime.setTypeface(tf_roboto_light);
        SetDateControl(endTime);
        oneDayDate = (MaterialEditText) rootView.findViewById(R.id.tv_dt_onceDate);
        oneDayDate.setTypeface(tf_roboto_light);

        fillDate();


        return rootView;
    }


}
