package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.dal.NoteDao;
import ae.ac.adec.coursefollowup.db.dal.YearDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.services.dailogs.AppDialog;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Courses;
import ae.ac.adec.coursefollowup.views.event.IDialogClick;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import ae.ac.adec.coursefollowup.views.view.CustomEditText;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by MyLabtop on 4/3/2015.
 */
public class NoteFragmentViewText extends BaseFragment {

    CustomDialogClass dialogClass = null;
    MaterialEditText txtNoteAddDate = null;
    MaterialEditText txtNoteSubject = null;
    MaterialEditText txtNoteName = null;
    MaterialEditText txtNoteDetail = null;
    CustomEditText customEditText = null;
    Course selected_course;
    int noteType = -1;
    String filepath = null;
    File txtFile = null;

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


    private String readFromFile() {
        //Read text from file
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(txtFile));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();
    }

    private void writeToFile(String text) throws IOException {
        //Write text to file
        FileOutputStream fos = new FileOutputStream(txtFile);
        fos.write(text.getBytes());
        fos.close();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_delete, menu);
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
            case R.id.ic_menu_save:
                Edit();
                return true;
            default:
                break;
        }

        return false;
    }

    public void Edit() {
        if (ID != null && ID != 0) {
            try {
                AppLog.i("ID== >>> " + ID);
                NoteDao noteDao = new NoteDao();

                String noteName = txtNoteName.getText().toString().trim();
                if (noteName.equals(""))
                    throw new BusinessRoleError(R.string.BR_NOT_005);
                if (selected_course == null)
                    throw new BusinessRoleError(R.string.BR_NOT_003);
                String cusText = customEditText.getText().toString().trim();
                if (cusText.equals(""))
                    throw new BusinessRoleError(R.string.BR_NOT_004);

                long dateCreated = Calendar.getInstance().getTimeInMillis();
                try {
                    writeToFile(cusText);
                    noteDao.Edit(ID, selected_course, noteName, noteType, txtNoteDetail.getText().toString().trim(),
                            filepath, dateCreated);
                    Toast.makeText(getActivity(), R.string.note_edit_successfully, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                getActivity().finish();
            } catch (BusinessRoleError ex) {
                AppAction.DiaplayError(getActivity(), ex.getMessage());
            }
        }
    }

    public void Delete() {
        try {
            NoteDao noteDao = new NoteDao();
            noteDao.delete(ID);
            if (txtFile != null)
                if (txtFile.exists()) {
                    txtFile.delete();
                }
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
            Note note = Note.load(Note.class, ID);
            txtNoteAddDate.setText(ConstantVariable.getDateString(note.DateAdded));
            if (note.Course != null)
                txtNoteSubject.setText(note.Course.Name);
            selected_course = note.Course;
            txtNoteDetail.setText(note.Details);
            txtNoteName.setText(note.NoteName);
            noteType = note.NoteType;
            filepath = note.FilePath;
            txtFile = new File(filepath);
            customEditText.setText(readFromFile());

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_text_view, container, false);
        removeShadowForNewApi21(rootView);


        txtNoteAddDate = (MaterialEditText) rootView.findViewById(R.id.txtNoteAddDate);
        txtNoteAddDate.setTypeface(tf_roboto_light);
        txtNoteSubject = (MaterialEditText) rootView.findViewById(R.id.txtNoteSubject);
        txtNoteSubject.setTypeface(tf_roboto_light);
        txtNoteDetail = (MaterialEditText) rootView.findViewById(R.id.txtNoteDetail);
        txtNoteDetail.setTypeface(tf_roboto_light);
        txtNoteName = (MaterialEditText) rootView.findViewById(R.id.txtNoteName);
        txtNoteName.setTypeface(tf_roboto_light);
        txtNoteName.setTextColor(getResources().getColor(R.color.white));
        txtNoteName.setTypeface(tf_roboto_light);

        LinearLayout v = (LinearLayout) rootView.findViewById(R.id.compoundView);

        customEditText = new CustomEditText(getActivity(), null);
        customEditText.setLines(30);
        customEditText.setGravity(Gravity.LEFT | Gravity.TOP);
        v.addView(customEditText);

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
                            selected_course = ((Course) adapter.getItem(position));
                            dialogClass.dismiss();
                        }
                    });
                    dialogClass.show(getActivity().getFragmentManager(), "Iam here!");

                    v.clearFocus();
                }

            }
        });

        fillDate();

        //  txtNoteImageView = (ImageView) rootView.findViewById(R.id.txtNoteImageView);


        return rootView;
    }

}
