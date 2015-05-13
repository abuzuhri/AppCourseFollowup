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
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.SemesterDao;
import ae.ac.adec.coursefollowup.db.models.Semester;
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
public class SemesterFragmentView extends BaseFragment {

    MaterialEditText semesterName=null;
    MaterialEditText startDate=null;
    MaterialEditText endDate=null;
    MaterialEditText selectYear=null;

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
            AppAction.OpenActivityWithFRAGMENT(getActivity(), SemesterFragmentAddEdit.class.getName(), ID);
            getActivity().finish();
        }catch (Exception ex){
            Crouton.makeText(getActivity(), ex.getMessage(), Style.ALERT).show();
        }
    }

    public void Delete(){
        try {
            SemesterDao semesterDao = new SemesterDao();
            semesterDao.delete(ID);

            getActivity().finish();
            Toast.makeText(getActivity(), R.string.delete_successfully, Toast.LENGTH_LONG).show();
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
            Semester semester= Semester.load(Semester.class, ID);
            semesterName.setText(semester.Name);
            semesterName.setEnabled(false);

            startDate.setText(ConstantVariable.getDateString(semester.StartDate));
            startDate.setEnabled(false);

            endDate.setText(ConstantVariable.getDateString(semester.EndDate));
            endDate.setEnabled(false);

            selectYear.setText(semester.year.Name);
            selectYear.setEnabled(false);



        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_semester_add_edit, container, false);
        removeShadowForNewApi21(rootView);

        //Semester Name
        semesterName= (MaterialEditText) rootView.findViewById(R.id.txtName);
        semesterName.setTypeface(tf_roboto_light);
        // Start Date
        startDate= (MaterialEditText) rootView.findViewById(R.id.txtStartDate);
        SetDateControl(startDate);
        startDate.setTypeface(tf_roboto_light);


        // End Date
        endDate= (MaterialEditText) rootView.findViewById(R.id.txtEndDate);
        SetDateControl(endDate);
        endDate.setTypeface(tf_roboto_light);

        selectYear = (MaterialEditText) rootView.findViewById(R.id.txtSelectYear);

        fillDate();


        return rootView;
    }

}
