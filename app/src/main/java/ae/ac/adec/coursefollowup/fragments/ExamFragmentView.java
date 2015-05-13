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
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.ExamDao;
import ae.ac.adec.coursefollowup.db.dal.HolidayDao;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Holiday;
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
public class ExamFragmentView extends BaseFragment {

    MaterialEditText courseName=null;
    CheckBox isResit = null;
    MaterialEditText seat=null;
    MaterialEditText room=null;
    MaterialEditText startDateTime=null;
    MaterialEditText endDateTime=null;
    MaterialEditText examDate=null;

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
        super.onCreateOptionsMenu(menu,inflater);
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

    public void Edit(){
        try {
            AppAction.OpenActivityWithFRAGMENT(getActivity(), ExamFragmentAddEdit.class.getName(), ID);
            getActivity().finish();
        }catch (Exception ex){
            Crouton.makeText(getActivity(), ex.getMessage(), Style.ALERT).show();
        }
    }

    public void Delete(){
        try {
            ExamDao examDao = new ExamDao();
            examDao.delete(ID);

            getActivity().finish();
            Toast.makeText(getActivity(),R.string.delete_successfully,Toast.LENGTH_LONG).show();
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
            Exam exam= Exam.load(Exam.class, ID);
            courseName.setText(exam.Course.Name);
            courseName.setEnabled(false);

            isResit.setChecked(exam.IsResit);
            isResit.setEnabled(false);

            seat.setText(exam.Seat);
            seat.setEnabled(false);

            room.setText(exam.Room);
            room.setEnabled(false);

            startDateTime.setText(ConstantVariable.getTimeString(exam.StartDateTime));
            startDateTime.setEnabled(false);

            endDateTime.setText(ConstantVariable.getTimeString(exam.EndDateTime));
            endDateTime.setEnabled(false);

            examDate.setText(ConstantVariable.getDateString(exam.EndDateTime));
            examDate.setTag(exam.EndDateTime.getTime());
            examDate.setEnabled(false);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exam_view, container, false);
        removeShadowForNewApi21(rootView);

        courseName= (MaterialEditText) rootView.findViewById(R.id.tv_exam_course_name);
        courseName.setTypeface(tf_roboto_light);
        seat = (MaterialEditText) rootView.findViewById(R.id.tv_exam_seat);
        seat.setTypeface(tf_roboto_light);
        room = (MaterialEditText) rootView.findViewById(R.id.tv_exam_room);
        room.setTypeface(tf_roboto_light);
        isResit = (CheckBox) rootView.findViewById(R.id.cb_exam_isResit);

        examDate = (MaterialEditText) rootView.findViewById(R.id.tv_exam_date);
        SetDateControl(examDate);
        examDate.setTypeface(tf_roboto_light);

        startDateTime= (MaterialEditText) rootView.findViewById(R.id.tv_exam_startTime);
        SetDateControl(startDateTime);
        startDateTime.setTypeface(tf_roboto_light);

        endDateTime= (MaterialEditText) rootView.findViewById(R.id.tv_exam_endTime);
        SetDateControl(endDateTime);
        endDateTime.setTypeface(tf_roboto_light);


        fillDate();


        return rootView;
    }



}
