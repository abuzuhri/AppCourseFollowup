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
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
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

    CustomDialogClass dialogClass =null;
    MaterialEditText txtTaskName=null;
    MaterialEditText txtTaskSubject=null;
    MaterialEditText txtTaskType=null;
    MaterialEditText txtTaskDueDate=null;
    MaterialEditText txtTaskDetail=null;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position= getArguments().getInt(POSITION,0);
        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(ID!=null && ID!=0)
            setSubTitle(getString(R.string.task_add_subtitle));
        else setSubTitle(getString(R.string.task_add_subtitle));
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu,inflater);
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

    public void AddEdit(){
        try {
            AppLog.i("ID== >>> " + ID);
            TaskDao task = new TaskDao();


            long dueDateMil = (long) txtTaskDueDate.getTag();
//ConstantVariable.Category.Dashboard.id
            Course c = new Course();
          //  if(ID!=null && ID!=0)
            //    task.Edit(ID, txtTaskName.getText().toString() , dueDateMil, null,null, txtTaskDetail.getText().toString() , , 0, c);
            //    task.Edit(ID, holidayName.getText().toString(), startDateMil, endDateMil);
          //  else
         //       task.Add(holidayName.getText().toString(), startDateMil, endDateMil);

            getActivity().finish();
            Toast.makeText(getActivity(), R.string.task_add_successfully, Toast.LENGTH_LONG).show();
        }catch (Error ex){ //catch (BusinessRoleError ex){
            AppAction.DiaplayError(getActivity(), ex.getMessage());
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private  void fillDate(){
        if(ID!=null && ID!=0){
            Task task= Task.load(Task.class, ID);



            txtTaskName.setText(task.Name);
            txtTaskSubject.setText(task.Course.Name);
            txtTaskType.setText(task.TaskType);
            txtTaskDetail.setText(task.Detail);


            txtTaskDueDate.setText(ConstantVariable.getDateString(task.DueDate));
            txtTaskDueDate.setTag(task.DueDate.getTime());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_task_add_edit, container, false);
        removeShadowForNewApi21(rootView);




        txtTaskName= (MaterialEditText) rootView.findViewById(R.id.txtTaskName);

        txtTaskDueDate= (MaterialEditText) rootView.findViewById(R.id.txtTaskDueDate);
        SetDateControl(txtTaskDueDate);


        txtTaskDetail= (MaterialEditText) rootView.findViewById(R.id.txtTaskDetail);

        txtTaskType= (MaterialEditText) rootView.findViewById(R.id.txtTaskType);

        txtTaskSubject= (MaterialEditText) rootView.findViewById(R.id.txtTaskSubject);
        txtTaskSubject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    List<Course> courses = new CourseDao().getAll(position);
                    final CustomLVAdapter_Courses adapter = new CustomLVAdapter_Courses(getActivity(), courses);

                    dialogClass = new CustomDialogClass(getActivity(), CourcesFragmentAddEdit.class.getName(), "Select Subject",
                            adapter, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            txtTaskSubject.setText(((Course)adapter.getItem(position)).Name);
                       //     selectedYear=((Year)adapter.getItem(position));


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
                if(!hasFocus) {

                    final BaseAdapter ad =    new ArrayAdapter<ConstantVariable.TaskType>(getActivity(), android.R.layout.simple_list_item_1, ConstantVariable.TaskType.values());
                    dialogClass = new CustomDialogClass(getActivity(), "", "Select Task Type",ad, new AdapterView.OnItemClickListener() {

                            @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                txtTaskType.setText(ad.getItem(position).toString());
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
