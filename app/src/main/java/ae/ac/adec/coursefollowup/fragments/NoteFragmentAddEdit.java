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
import android.view.MotionEvent;
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
import ae.ac.adec.coursefollowup.services.dailogs.AppDialog;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Courses;
import ae.ac.adec.coursefollowup.views.event.IDialogClick;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by MyLabtop on 3/28/2015.
 */
public class NoteFragmentAddEdit extends BaseFragment {


    Course selectedCourse = null;
    String filePath = null;
    private int position;
    CustomDialogClass dialogClass = null;
    MaterialEditText txtNoteSubject = null;
    //MaterialEditText txtNoteType = null;
    Button txtNoteFilePath = null;
    MaterialEditText txtNoteDetail = null;
    MaterialEditText txtNoteName = null;
    int noteType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(POSITION, 0);
        noteType = getActivity().getIntent().getExtras().getInt(AppAction.NOTETYPE, 0);
        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();
        hideSoftKeyboard();
        File NotesDirectory = new File(ConstantVariable.NOTES_DIRECTORY);
        File voiceDirectory = new File(ConstantVariable.NOTES_VOICE_DIRECTORY);
        File imageDirectory = new File(ConstantVariable.NOTES_IMAGE_DIRECTORY);
        File textDirectory = new File(ConstantVariable.NOTES_TEXT_DIRECTORY);
        File videoDirectory = new File(ConstantVariable.NOTES_VIDEO_DIRECTORY);

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
            NoteDao Note = new NoteDao();

            String notename = txtNoteName.getText().toString().trim();
            if (notename.equals(""))
                throw new BusinessRoleError(R.string.BR_NOT_005);
            if (filePath == null || filePath.equals(""))
                throw new BusinessRoleError(R.string.BR_NOT_001);

            if (ID != null && ID != 0) {
                Note.Edit(ID, selectedCourse, notename, noteType, txtNoteDetail.getText().toString(), filePath, Calendar.getInstance().getTimeInMillis());
                Toast.makeText(getActivity(), R.string.note_edit_successfully, Toast.LENGTH_LONG).show();
            } else {
                Note.Add(selectedCourse, notename, noteType, txtNoteDetail.getText().toString(), filePath, Calendar.getInstance().getTimeInMillis());
                Toast.makeText(getActivity(), R.string.note_add_successfully, Toast.LENGTH_LONG).show();
            }

            getActivity().finish();
        } catch (BusinessRoleError ex) {
            AppAction.DiaplayError(getActivity(), ex.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OneFragmentActivity.setNoteType(null);
        OneFragmentActivity.setCourseName(null);
        OneFragmentActivity.temp = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_add_edit, container, false);
        removeShadowForNewApi21(rootView);


        txtNoteSubject = (MaterialEditText) rootView.findViewById(R.id.txtNoteSubject);
        txtNoteSubject.setTypeface(tf_roboto_light);
        txtNoteName = (MaterialEditText) rootView.findViewById(R.id.txtNoteName);
        txtNoteName.setTypeface(tf_roboto_light);

        //txtNoteType = (MaterialEditText) rootView.findViewById(R.id.txtNoteType);

        txtNoteFilePath = (Button) rootView.findViewById(R.id.txtNoteFilePath);
        txtNoteFilePath.setTypeface(tf_roboto_light);

        txtNoteDetail = (MaterialEditText) rootView.findViewById(R.id.txtNoteDetail);
        txtNoteDetail.setTypeface(tf_roboto_light);

        txtNoteSubject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    List<Course> courses = new CourseDao().getAll(2);
                    final CustomLVAdapter_Courses adapter = new CustomLVAdapter_Courses(getActivity(), courses);

                    dialogClass = new CustomDialogClass(getActivity(), CourcesFragmentAddEdit.class.getName(), getString(R.string.select_course),
                            adapter, false, -1, new AdapterView.OnItemClickListener() {
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


//        txtNoteType.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    final BaseAdapter ad = new ArrayAdapter<ConstantVariable.NoteType>(getActivity(), android.R.layout.simple_list_item_1, ConstantVariable.NoteType.values());
//                    dialogClass = new CustomDialogClass(getActivity(), "", "Select Note Type", ad, false, -1, new AdapterView.OnItemClickListener() {
//
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            txtNoteType.setText(ad.getItem(position).toString());
//                            selectedType = position + 1;
//                            dialogClass.dismiss();
//                        }
//                    });
//                    dialogClass.show(getActivity().getFragmentManager(), "Iam here!");
//                    v.clearFocus();
//                }
//
//            }
//        });

        txtNoteFilePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (txtNoteSubject.getText().toString() != null && !txtNoteSubject.getText().toString().equals("")) {
                        OneFragmentActivity.setCourseName(txtNoteSubject.getText().toString());
                        if (noteType > 0) {
                            if (noteType == ConstantVariable.NoteType.Voice.id) {
                                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentRecordSound.class.getName(), -1);
                            } else if (noteType == ConstantVariable.NoteType.Image.id) {
                                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentTakeImage.class.getName(), -1);
                            } else if (noteType == ConstantVariable.NoteType.Video.id) {
                                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentTakeVideo.class.getName(), -1);
                            } else if (noteType == ConstantVariable.NoteType.Text.id) {
                                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentText.class.getName(), -1);
                            }
                        } else {
                            throw new BusinessRoleError(R.string.BR_NOT_002);
                        }
                    } else {
                        throw new BusinessRoleError(R.string.BR_NOT_003);
                    }
                } catch (BusinessRoleError ex) {
                    AppAction.DiaplayError(getActivity(), ex.getMessage());
                }
            }
        });


        return rootView;
    }

}
