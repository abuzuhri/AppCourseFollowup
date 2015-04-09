package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.gc.materialdesign.widgets.ProgressDialog;

import java.io.File;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.FullScreenVideoActivity;
import ae.ac.adec.coursefollowup.db.dal.NoteDao;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.services.dailogs.AppDialog;
import ae.ac.adec.coursefollowup.views.event.IDialogClick;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by MyLabtop on 4/2/2015.
 */
public class NoteFragmentViewVideo extends BaseFragment {

     private VideoView myVideoView;
     private int position = 0;
     private ProgressDialog progressDialog;
     private MediaController mediaControls;

     private TextView txtNoteAddDate         =null;
     private TextView txtNoteSubject         =null;
     private TextView txtNoteDetail          =null;

    File videoFile =null;
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
                //    Edit();
                return true;
            default:
                break;
        }

        return false;
    }

    public void Edit(){
        try {
            //       AppAction.OpenActivityWithFRAGMENT(getActivity(), TaskFragmentAddEdit.class.getName(), ID);
        //    getActivity().finish();
        }catch (Exception ex){
            Crouton.makeText(getActivity(), ex.getMessage(), Style.ALERT).show();
        }
    }

    public void Delete(){
        try {
            NoteDao noteDao = new NoteDao();

            noteDao.delete(ID);
            if(videoFile!=null)
                if(videoFile.exists()){
                    videoFile.delete();
                }

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

            Note note= Note.load(Note.class, ID);

            txtNoteAddDate.setText(ConstantVariable.getDateString(note.DateAdded));
            txtNoteSubject.setText(note.Course.Name);
            txtNoteDetail.setText(note.Details);


            videoFile = new File(note.FilePath);



        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_video, container, false);//fragment_note_video
        removeShadowForNewApi21(rootView);

        txtNoteAddDate= (TextView) rootView.findViewById(R.id.txtNoteAddDate);

        txtNoteSubject= (TextView) rootView.findViewById(R.id.txtNoteSubject);

        txtNoteDetail = (TextView) rootView.findViewById(R.id.txtNoteDetail);


        	        myVideoView = (VideoView) rootView.findViewById(R.id.video_view);
                     fillDate();

        	        progressDialog = new ProgressDialog(getActivity(),"Loading....");
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

                    Intent i = new Intent(getActivity(), FullScreenVideoActivity.class);
                    i.putExtra("typeView","video");
                    i.putExtra("position", myVideoView.getCurrentPosition());
                    i.putExtra("videoPath", videoFile.getPath());


                    getActivity().startActivity(i);

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




        return rootView;
    }

}

