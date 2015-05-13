package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
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
 * Created by MyLabtop on 4/2/2015.
 */
public class NoteFragmentViewVideo extends BaseFragment {

    Display display;
    CustomDialogClass dialogClass = null;
    Course selected_course;
    int noteType = -1;
    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

    ImageView img_delete, img_upload;

    private MaterialEditText txtNoteName = null;
    private MaterialEditText txtNoteAddDate = null;
    private MaterialEditText txtNoteSubject = null;
    private MaterialEditText txtNoteDetail = null;

    String vid_path;
    File videoFile = null;
    int dC = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);


        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(getActivity());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
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
                        vid_path, dateCreated);
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
            if (videoFile != null)
                if (videoFile.exists()) {
                    videoFile.delete();
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
            vid_path = OneFragmentActivity.getFilePath();
            if (vid_path != null && !vid_path.equals("")) {
                showVideo();
                img_delete.setVisibility(View.VISIBLE);
                img_upload.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OneFragmentActivity.setFilePath(null);
        OneFragmentActivity.setNoteType(null);
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
            txtNoteName.setText(note.NoteName);
            txtNoteDetail.setText(note.Details);
            vid_path = note.FilePath;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_video, container, false);//fragment_note_video
        removeShadowForNewApi21(rootView);

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
                        if (videoFile != null)
                            if (videoFile.exists()) {
                                videoFile.delete();
                            }
                        vid_path = "";
                        img_delete.setVisibility(View.GONE);
                        myVideoView.setVideoURI(null);
                        img_upload.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppAction.OpenActivityWithFRAGMENT(v.getContext(), OneFragmentActivity.class, NoteFragmentTakeVideo.class.getName(), -1);
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


        myVideoView = (VideoView) rootView.findViewById(R.id.video_view);
        fillDate();

        myVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dC++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        dC = 0;
                    }
                };
                if (dC == 1) {
                    //Single click
                    handler.postDelayed(r, 250);
                } else if (dC == 2) {
                    //Double click
                    dC = 0;
                    if (videoFile != null && videoFile.exists()) {
                        Intent i = new Intent(getActivity(), FullScreenVideoActivity.class);
                        i.putExtra("typeView", "video");
                        i.putExtra("position", myVideoView.getCurrentPosition());
                        i.putExtra("videoPath", videoFile.getPath());
                        getActivity().startActivity(i);
                    }
                }
                return false;
            }
        });
        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                progressDialog.dismiss();
                myVideoView.seekTo(position);

            }
        });

        if (vid_path != null && !vid_path.equals("")) {
            File f = new File(vid_path);
            if (!f.exists()) {
                img_delete.setVisibility(View.GONE);
                img_upload.setVisibility(View.VISIBLE);
            } else
                showVideo();
        }

        return rootView;
    }

    private void showVideo() {
        videoFile = new File(vid_path);
        progressDialog = new ProgressDialog(getActivity(), "Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);
            //set the uri of the video to be played
            myVideoView.setVideoURI(Uri.parse(videoFile.getPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

