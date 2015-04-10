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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.dal.TaskDao;
import ae.ac.adec.coursefollowup.db.dal.YearDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Courses;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Years;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by MyLabtop on 3/24/2015.
 */
public class TaskFragmentAddEdit extends BaseFragment {

    Course selectedCourse = null;
    int selectedType = -1;
    CustomDialogClass dialogClass = null;
    MaterialEditText txtTaskName = null;
    MaterialEditText txtTaskSubject = null;
    MaterialEditText txtTaskType = null;
    MaterialEditText txtTaskDueDate = null;
    MaterialEditText txtTaskDetail = null;
    int progressValue = 0;
    TextView txtTaskProgress = null;
    SeekBar seekProgressBar = null;
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
            setSubTitle(getString(R.string.task_add_subtitle));
        else setSubTitle(getString(R.string.task_add_subtitle));
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
            TaskDao task = new TaskDao();
            // BR_TSK_001
            if (txtTaskName.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_TSK_001);
            // BR_TSK_004
            if (ID != null && ID != 0) {
                Task t = Task.load(Task.class, ID);
                selectedType = t.TaskType;
                selectedCourse = t.Course;
            }
            if (selectedCourse == null)
                throw new BusinessRoleError(R.string.BR_TSK_004);
            // BR_TSK_003
            if (selectedType == -1)
                throw new BusinessRoleError(R.string.BR_TSK_003);
            // BR_TSK_005
            if (txtTaskDueDate.getText().toString().trim().equals(""))
                throw new BusinessRoleError(R.string.BR_TSK_005);

            long dueDateMil = (long) txtTaskDueDate.getTag();
            if (ID != null && ID != 0)
                task.Edit(ID, txtTaskName.getText().toString().trim(), dueDateMil, Calendar.getInstance().getTimeInMillis(), null, txtTaskDetail.getText().toString().trim(), selectedType, progressValue, selectedCourse);
            else
                task.Add(txtTaskName.getText().toString().trim(), dueDateMil, Calendar.getInstance().getTimeInMillis(), null, txtTaskDetail.getText().toString().trim(), selectedType, progressValue, selectedCourse);

            getActivity().finish();
            Toast.makeText(getActivity(), R.string.task_add_successfully, Toast.LENGTH_LONG).show();
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
            txtTaskSubject.setText(task.Course.Name);
            txtTaskType.setText(ConstantVariable.TaskType.fromInteger(task.TaskType));
            txtTaskDetail.setText(task.Detail);
            txtTaskDueDate.setText(ConstantVariable.getDateString(task.DueDate));
            txtTaskDueDate.setTag(task.DueDate.getTime());
            txtTaskProgress.setText("Task Progress: " + task.Progress + "% complete");
            seekProgressBar.setProgress(task.Progress);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_task_add_edit, container, false);
        removeShadowForNewApi21(rootView);


        txtTaskName = (MaterialEditText) rootView.findViewById(R.id.txtTaskName);

        txtTaskDueDate = (MaterialEditText) rootView.findViewById(R.id.txtTaskDueDate);
        SetDateControl_New(txtTaskDueDate);


        txtTaskDetail = (MaterialEditText) rootView.findViewById(R.id.txtTaskDetail);

        txtTaskType = (MaterialEditText) rootView.findViewById(R.id.txtTaskType);

        txtTaskSubject = (MaterialEditText) rootView.findViewById(R.id.txtTaskSubject);
        txtTaskSubject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    List<Course> courses = new CourseDao().getAll(position);
                    final CustomLVAdapter_Courses adapter = new CustomLVAdapter_Courses(getActivity(), courses);

                    dialogClass = new CustomDialogClass(getActivity(), CourcesFragmentAddEdit.class.getName(), "Select Subject",
                            adapter, false, -1, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            txtTaskSubject.setText(((Course) adapter.getItem(position)).Name);
                            selectedCourse = ((Course) adapter.getItem(position));
                            dialogClass.dismiss();
                        }
                    });
                    dialogClass.show(getActivity().getFragmentManager(), "Iam here!");
                }
                v.clearFocus();
            }
        });
        txtTaskType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final BaseAdapter ad = new ArrayAdapter<ConstantVariable.TaskType>(getActivity(), android.R.layout.simple_list_item_1, ConstantVariable.TaskType.values());
                    dialogClass = new CustomDialogClass(getActivity(), "", "Select Task Type", ad, false, -1, new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            txtTaskType.setText(ad.getItem(position).toString());
                            selectedType = position + 1;
                            dialogClass.dismiss();
                        }
                    });
                    dialogClass.show(getActivity().getFragmentManager(), "Iam here!");
                }
                v.clearFocus();
            }
        });

        txtTaskProgress = (TextView) rootView.findViewById(R.id.txtTaskProgress);
        seekProgressBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        if (ID == null || ID == 0) {
            txtTaskProgress.setVisibility(View.GONE);
            seekProgressBar.setVisibility(View.GONE);
            progressValue = 0;


        }
        //  seekProgressBar.incrementProgressBy(5);
        seekProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressFinal = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progress = progress / 5;
                progress = progress * 5;
                progressFinal = progress;
                txtTaskProgress.setText(progress + "% Complete");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                progressValue = progressFinal;

            }
        });

        fillDate();

        return rootView;
    }


}
