package ae.ac.adec.coursefollowup.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.dal.HolidayDao;
import ae.ac.adec.coursefollowup.db.dal.NoteDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Courses;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by MyLabtop on 3/28/2015.
 */
public class NoteFragmentAddEdit  extends BaseFragment {



    Course selectedCourse = null;
    int selectedType = -1;
    String filePath =null;
    private int position;
    CustomDialogClass dialogClass = null;
    MaterialEditText txtNoteSubject = null;
    MaterialEditText txtNoteType = null;
    MaterialEditText txtNoteFilePath = null;
    MaterialEditText txtNoteDetail = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(POSITION, 0);
        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();
        hideSoftKeyboard();
        File NotesDirectory = new File( ConstantVariable.NOTES_DIRECTORY);
        File voiceDirectory = new File( ConstantVariable.NOTES_VOICE_DIRECTORY);
        File imageDirectory = new File( ConstantVariable.NOTES_IMAGE_DIRECTORY);
        File textDirectory  = new File( ConstantVariable.NOTES_TEXT_DIRECTORY);
        File videoDirectory = new File( ConstantVariable.NOTES_VIDEO_DIRECTORY);

        if (!NotesDirectory.exists())
            NotesDirectory.mkdirs();

        if (!voiceDirectory.exists())
            voiceDirectory.mkdirs();

        if (!imageDirectory.exists())
            imageDirectory.mkdirs();

        if (!textDirectory.exists())
            textDirectory.mkdirs();

        if (!videoDirectory.exists())
            videoDirectory.mkdirs();


    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            setSubTitle("New Note");

    }

    @Override
    public void onResume() {
        super.onResume();
        txtNoteFilePath.setText(OneFragmentActivity.getNoteType());
        filePath = OneFragmentActivity.getFilePath();

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
            NoteDao Note = new NoteDao();

                if(ID!=null && ID!=0)
                Note.Edit(ID, selectedCourse, selectedType , txtNoteDetail.getText().toString() ,filePath , Calendar.getInstance().getTimeInMillis() );
            else
                Note.Add(selectedCourse, selectedType , txtNoteDetail.getText().toString() ,filePath , Calendar.getInstance().getTimeInMillis() );

            getActivity().finish();
            Toast.makeText(getActivity(), R.string.note_add_successfully, Toast.LENGTH_LONG).show();
        }catch (BusinessRoleError ex){
            AppAction.DiaplayError(getActivity(), ex.getMessage());
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private  void fillDate(){
    /*    if(ID!=null && ID!=0){
            Holiday holiday= Holiday.load(Holiday.class, ID);
            holidayName.setText(holiday.Name);
            startDate.setText(ConstantVariable.getDateString(holiday.StartDate));
            startDate.setTag(holiday.StartDate.getTime());
            endDate.setText(ConstantVariable.getDateString(holiday.EndDate));
            endDate.setTag(holiday.EndDate.getTime());
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_add_edit, container, false);
        removeShadowForNewApi21(rootView);



        txtNoteSubject = (MaterialEditText) rootView.findViewById(R.id.txtNoteSubject);

        txtNoteType = (MaterialEditText) rootView.findViewById(R.id.txtNoteType);

        txtNoteFilePath = (MaterialEditText) rootView.findViewById(R.id.txtNoteFilePath);

        txtNoteDetail = (MaterialEditText) rootView.findViewById(R.id.txtNoteDetail);



        txtNoteSubject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    List<Course> courses = new CourseDao().getAll(position);
                    final CustomLVAdapter_Courses adapter = new CustomLVAdapter_Courses(getActivity(), courses);

                    dialogClass = new CustomDialogClass(getActivity(), CourcesFragmentAddEdit.class.getName(), "Select Subject",
                            adapter,false,-1, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            txtNoteSubject.setText(((Course) adapter.getItem(position)).Name);
                            selectedCourse = ((Course) adapter.getItem(position));
                            dialogClass.dismiss();
                        }
                    });
                    dialogClass.show(getActivity().getFragmentManager(), "Iam here!");
                    v.clearFocus();
                }

            }
        });

        txtNoteType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    final BaseAdapter ad = new ArrayAdapter<ConstantVariable.NoteType>(getActivity(), android.R.layout.simple_list_item_1, ConstantVariable.NoteType.values());
                    dialogClass = new CustomDialogClass(getActivity(), "", "Select Note Type", ad,false,-1, new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            txtNoteType.setText(ad.getItem(position).toString());
                            selectedType = position + 1;
                            dialogClass.dismiss();
                        }
                    });
                    dialogClass.show(getActivity().getFragmentManager(), "Iam here!");
                    v.clearFocus();
                }

            }
        });

        txtNoteFilePath.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    // create a File object for the parent directory




               //     File outputFile = new File(wallpaperDirectory, filename);

                //    FileOutputStream fos = new FileOutputStream(outputFile);

                    if(txtNoteSubject.getText().toString()!=null && !txtNoteSubject.getText().toString().equals("")) {

                        OneFragmentActivity.setCourseName(txtNoteSubject.getText().toString());

                        if (txtNoteType.getText().toString() != null && !txtNoteType.getText().toString().equals("")) {
                            if (txtNoteType.getText().toString().equals("Voice")) {
                                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentRecordSound.class.getName(), -1);
                            } else if (txtNoteType.getText().toString().equals("Image")) {
                                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentTakeImage.class.getName(), -1);
                            } else if (txtNoteType.getText().toString().equals("Video")) {
                                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentTakeVideo.class.getName(), -1);
                            } else if (txtNoteType.getText().toString().equals("Text")) {
                                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentText.class.getName(), -1);
                            }

                        } else {
                            Toast.makeText(getActivity(), "Select Note Type First", Toast.LENGTH_LONG).show();
                        }
                    }else{

                        Toast.makeText(getActivity(), "Select Subject First", Toast.LENGTH_LONG).show();
                    }

                    v.clearFocus();
                }



            }
        });


        return rootView;
    }

}
