package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;


import com.gc.materialdesign.widgets.ProgressDialog;
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
 * Created by MyLabtop on 4/3/2015.
 */
public class NoteFragmentViewVoice extends BaseFragment {

    // private VideoView myVideoView;
    //  private int position = 0;
    //   private ProgressDialog progressDialog;
    // private MediaController mediaControls;
    Display display;
    CustomDialogClass dialogClass = null;
    Course selected_course;
    int noteType = -1;
    private MediaPlayer mediaPlayer = null;
    private MaterialEditText txtNoteAddDate = null;
    private MaterialEditText txtNoteSubject = null;
    private MaterialEditText txtNoteDetail = null;
    private MaterialEditText txtNoteName = null;
    private ToggleButton playPauseVoiceBtn = null;
    private ImageButton stopVoiceBtn = null;
    ImageView img_delete, img_upload;
    File voiceFile = null;
    String voice_path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

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
                        voice_path, dateCreated);
                Toast.makeText(getActivity(), R.string.note_edit_successfully, Toast.LENGTH_LONG).show();
                getActivity().finish();
            } catch (BusinessRoleError ex) {
                AppAction.DiaplayError(getActivity(), ex.getMessage());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OneFragmentActivity.getNoteType() != null && !OneFragmentActivity.getNoteType().equals("")) {
            Toast.makeText(getActivity().getBaseContext(), OneFragmentActivity.getNoteType(), Toast.LENGTH_LONG).show();
            voice_path = OneFragmentActivity.getFilePath();
            if (voice_path != null && !voice_path.equals("")) {
                setNewSound();
                img_delete.setVisibility(View.VISIBLE);
                img_upload.setVisibility(View.GONE);
                playPauseVoiceBtn.setEnabled(true);
                stopVoiceBtn.setEnabled(true);
            }
            ((BaseActivity)getActivity()).settingLanguage("ar");
        }
    }

    private void setNewSound() {
        voiceFile = new File(voice_path);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(voiceFile.getPath());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopVoiceBtn.setEnabled(false);
                    if (mediaPlayer != null)
                        mediaPlayer = null;
                    playPauseVoiceBtn.toggle();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  hideSoftKeyboard();

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


    public void Delete() {
        try {
            NoteDao noteDao = new NoteDao();

            noteDao.delete(ID);
            if (voiceFile != null)
                if (voiceFile.exists()) {
                    voiceFile.delete();
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
            noteType = note.NoteType;
            txtNoteName.setText(note.NoteName);
            txtNoteDetail.setText(note.Details);
            voice_path = note.FilePath;
            if (voice_path != null)
                voiceFile = new File(voice_path);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_voice_view, container, false);
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

        img_delete = (ImageView) rootView.findViewById(R.id.img_delete);
        img_upload = (ImageView) rootView.findViewById(R.id.img_uploadNew);
        img_upload.setVisibility(View.GONE);

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialog.Delete(getActivity(), new IDialogClick() {
                    @Override
                    public void onConfirm() {
                        if (voiceFile != null)
                            if (voiceFile.exists()) {
                                voiceFile.delete();
                            }
                        voice_path = "";
                        img_delete.setVisibility(View.GONE);
                        img_upload.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentRecordSound.class.getName(), -1);
            }
        });

        txtNoteSubject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    List<Course> courses = new CourseDao().getAll(2);
                    final CustomLVAdapter_Courses adapter = new CustomLVAdapter_Courses(getActivity(), courses);
                    dialogClass = new CustomDialogClass(getActivity(), CourcesFragmentAddEdit.class.getName(),  getString(R.string.select_course),
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

        playPauseVoiceBtn = (ToggleButton) rootView.findViewById(R.id.playPauseVoiceBtn);
        playPauseVoiceBtn.getLayoutParams().height = (int) (height * 0.15);
        playPauseVoiceBtn.getLayoutParams().width = (int) (height * 0.15);
        stopVoiceBtn = (ImageButton) rootView.findViewById(R.id.stopVoiceBtn);
        stopVoiceBtn.getLayoutParams().height = (int) (height * 0.15);
        stopVoiceBtn.getLayoutParams().width = (int) (height * 0.15);
        stopVoiceBtn.setEnabled(false);

        playPauseVoiceBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (voiceFile != null && voiceFile.exists()) {
                    if (isChecked) {
                        if (mediaPlayer == null) {
                            try {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(voiceFile.getPath());
                                mediaPlayer.prepare();
                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        stopVoiceBtn.setEnabled(false);
                                        if (mediaPlayer != null)
                                            mediaPlayer = null;
                                        playPauseVoiceBtn.toggle();
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //    MediaPlayer m = new MediaPlayer();
                        }

                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            stopVoiceBtn.setEnabled(true);
                        }
                    } else {
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                            }
                        } else {
                            playPauseVoiceBtn.setChecked(false);
                        }
                    }

                } else {
                    playPauseVoiceBtn.toggle();
                    playPauseVoiceBtn.setEnabled(false);
                    stopVoiceBtn.setEnabled(false);
                    Toast.makeText(getActivity().getBaseContext()
                            , "There is no sound, Please Add it", Toast.LENGTH_LONG).show();
                }
            }

        });


        stopVoiceBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (mediaPlayer != null) {
                                                    mediaPlayer.stop();
                                                    mediaPlayer.release();
                                                    mediaPlayer = null;
                                                    stopVoiceBtn.setEnabled(false);
                                                    playPauseVoiceBtn.toggle();
                                                }
                                            }
                                        }
        );
        fillDate();

        if (voice_path != null) {
            File f = new File(voice_path);
            if (!f.exists()) {
                img_delete.setVisibility(View.GONE);
                img_upload.setVisibility(View.VISIBLE);
            }
        }
        return rootView;
    }

}

