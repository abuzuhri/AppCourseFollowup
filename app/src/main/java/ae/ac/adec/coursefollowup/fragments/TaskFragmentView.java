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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.SemesterDao;
import ae.ac.adec.coursefollowup.db.dal.TaskDao;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.services.dailogs.AppDialog;
import ae.ac.adec.coursefollowup.views.event.IDialogClick;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by MyLabtop on 3/21/2015.
 */
public class TaskFragmentView extends BaseFragment {

    MaterialEditText txtTaskName = null;
    MaterialEditText txtTaskSubject = null;
    MaterialEditText txtTaskType = null;
    MaterialEditText txtTaskDueDate = null;
    MaterialEditText txtTaskDueTime = null;
    TextView txtTaskDetail = null;
    TextView txtTaskProgress = null;
    SeekBar seekProgressBar = null;

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
            AppAction.OpenActivityWithFRAGMENT(getActivity(), TaskFragmentAddEdit.class.getName(), ID);
            getActivity().finish();
        } catch (Exception ex) {
            Crouton.makeText(getActivity(), ex.getMessage(), Style.ALERT).show();
        }
    }

    public void Delete() {
        try {
            TaskDao taskDao = new TaskDao();
            taskDao.delete(ID);

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
            Task task = Task.load(Task.class, ID);

            txtTaskName.setText(task.Name);
            txtTaskName.setEnabled(false);

            txtTaskSubject.setText(task.Course.Name);
            txtTaskSubject.setEnabled(false);

            txtTaskType.setText(ConstantVariable.TaskType.fromInteger(task.TaskType));
            txtTaskType.setEnabled(false);

            txtTaskDetail.setText(task.Detail);
            txtTaskDetail.setEnabled(false);

            txtTaskDueDate.setText(ConstantVariable.getDateString(task.DueDate));
            txtTaskDueDate.setEnabled(false);

            txtTaskDueTime.setText(ConstantVariable.getTimeString(task.DueDate));
            txtTaskDueTime.setEnabled(false);

            txtTaskProgress.setText("Task Progress: " + task.Progress + "% complete");
            txtTaskProgress.setEnabled(false);

            seekProgressBar.setProgress(task.Progress);
            seekProgressBar.setEnabled(false);
            //  txtTaskProgress.setEnabled(false);


        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_task_view, container, false);
        removeShadowForNewApi21(rootView);

        //Semester Name
        txtTaskName = (MaterialEditText) rootView.findViewById(R.id.txtTaskName);

        // Start Date
        txtTaskSubject = (MaterialEditText) rootView.findViewById(R.id.txtTaskSubject);
        txtTaskSubject.setTypeface(tf_roboto_light);
        // End Date
        txtTaskDueDate = (MaterialEditText) rootView.findViewById(R.id.txtTaskDueDate);
        SetDateControl(txtTaskDueDate);
        txtTaskDueDate.setTypeface(tf_roboto_light);

        txtTaskDueTime = (MaterialEditText) rootView.findViewById(R.id.txtTaskDueTime);
        SetTimeControl(txtTaskDueTime, txtTaskDueDate);
        txtTaskDueTime.setTypeface(tf_roboto_light);

        txtTaskType = (MaterialEditText) rootView.findViewById(R.id.txtTaskType);
        txtTaskType.setTypeface(tf_roboto_light);
        txtTaskDetail = (TextView) rootView.findViewById(R.id.txtTaskDetail);
        txtTaskDetail.setTypeface(tf_roboto_light);

        txtTaskProgress = (TextView) rootView.findViewById(R.id.txtTaskProgress);
        txtTaskProgress.setTypeface(tf_roboto_light);
        seekProgressBar = (SeekBar) rootView.findViewById(R.id.seekBar);


        fillDate();

        return rootView;
    }

}
