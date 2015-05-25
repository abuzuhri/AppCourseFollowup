package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;


import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.BaseActivity;
import ae.ac.adec.coursefollowup.activities.FullScreenImageActivity;
import ae.ac.adec.coursefollowup.activities.FullScreenVideoActivity;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.dal.NoteDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.services.dailogs.AppDialog;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Courses;
import ae.ac.adec.coursefollowup.views.event.IDialogClick;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by MyLabtop on 4/1/2015.
 */
public class NoteFragmentViewImage extends BaseFragment {

    Display display;
    CustomDialogClass dialogClass = null;
    Course selected_course;
    int noteType = -1;
    MaterialEditText txtNoteAddDate = null;
    MaterialEditText txtNoteSubject = null;
    MaterialEditText txtNoteDetail = null;
    MaterialEditText txtNoteName = null;
    ImageView txtNoteImageView = null, img_delete, img_upload;
    File imgFile = null;
    String img_path;

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

                long dateCreated = Calendar.getInstance().getTimeInMillis();
                noteDao.Edit(ID, selected_course, noteName, noteType, txtNoteDetail.getText().toString().trim(),
                        img_path, dateCreated);
                Toast.makeText(getActivity(), R.string.note_edit_successfully, Toast.LENGTH_LONG).show();
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
            if (imgFile != null)
                if (imgFile.exists()) {
                    imgFile.delete();
                }
            getActivity().finish();
            Toast.makeText(getActivity(), R.string.delete_successfully, Toast.LENGTH_LONG).show();
        } catch (BusinessRoleError ex) {
            AppAction.DiaplayError(getActivity(), ex.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OneFragmentActivity.getNoteType() != null && !OneFragmentActivity.getNoteType().equals("")) {
            Toast.makeText(getActivity().getBaseContext(), OneFragmentActivity.getNoteType(), Toast.LENGTH_LONG).show();
            img_path = OneFragmentActivity.getFilePath();
            if (img_path != null && !img_path.equals("")) {
                setNewImage();
                img_delete.setVisibility(View.VISIBLE);
                img_upload.setVisibility(View.GONE);
            }
        }
    }

    private void setNewImage() {
        if (img_path != null) {
            imgFile = new File(img_path);
            if (imgFile.exists()) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
                Drawable d = new BitmapDrawable(getResources(), myBitmap);
                txtNoteImageView.setBackgroundDrawable(d);
                //txtNoteImageView.setRotation(90);
            } else {
                img_delete.setVisibility(View.GONE);
                img_upload.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OneFragmentActivity.setNoteType(null);
        OneFragmentActivity.setFilePath(null);
        OneFragmentActivity.setCourseName(null);
        OneFragmentActivity.temp = null;
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
            noteType = note.NoteType;
            txtNoteDetail.setText(note.Details);
            txtNoteName.setText(note.NoteName);
            img_path = note.FilePath;
            if (img_path != null)
                imgFile = new File(img_path);
            setNewImage();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_image, container, false);
        removeShadowForNewApi21(rootView);

        display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();

        txtNoteAddDate = (MaterialEditText) rootView.findViewById(R.id.txtNoteAddDate);
        txtNoteAddDate.setTypeface(tf_roboto_light);
        txtNoteSubject = (MaterialEditText) rootView.findViewById(R.id.txtNoteSubject);
        txtNoteSubject.setTypeface(tf_roboto_light);
        txtNoteDetail = (MaterialEditText) rootView.findViewById(R.id.txtNoteDetail);
        txtNoteDetail.setTypeface(tf_roboto_light);
        txtNoteName = (MaterialEditText) rootView.findViewById(R.id.txtNoteName);
        txtNoteName.setTypeface(tf_roboto_light);
        txtNoteName.setTextColor(getResources().getColor(R.color.white));

        txtNoteImageView = (ImageView) rootView.findViewById(R.id.txtNoteImageView);
        img_delete = (ImageView) rootView.findViewById(R.id.img_delete);
        img_upload = (ImageView) rootView.findViewById(R.id.img_uploadNew);
        img_upload.setVisibility(View.GONE);

        txtNoteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FullScreenImageActivity.class);
                i.putExtra("imagePath", imgFile.getPath());
                getActivity().startActivity(i);
            }
        });

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialog.Delete(getActivity(), new IDialogClick() {
                    @Override
                    public void onConfirm() {
                        if (imgFile != null)
                            if (imgFile.exists()) {
                                imgFile.delete();
                            }
                        img_path = "";
                        img_delete.setVisibility(View.GONE);
                        txtNoteImageView.setBackgroundDrawable(null);
                        img_upload.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentTakeImage.class.getName(), -1);
            }
        });

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

        if (img_path != null) {
            File f = new File(img_path);
            if (!f.exists()) {
                img_delete.setVisibility(View.GONE);
                img_upload.setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }

}
