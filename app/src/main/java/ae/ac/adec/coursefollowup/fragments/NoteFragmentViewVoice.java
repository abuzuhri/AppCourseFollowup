package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;


import com.gc.materialdesign.widgets.ProgressDialog;

import java.io.File;
import java.io.IOException;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
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
 * Created by MyLabtop on 4/3/2015.
 */
public class NoteFragmentViewVoice extends BaseFragment {

   // private VideoView myVideoView;
  //  private int position = 0;
 //   private ProgressDialog progressDialog;
   // private MediaController mediaControls;
    private MediaPlayer mediaPlayer = null;
    private TextView txtNoteAddDate         =null;
    private TextView txtNoteSubject         =null;
    private TextView txtNoteDetail          =null;
    private ToggleButton playPauseVoiceBtn =null;
    private ImageButton stopVoiceBtn = null;
    File voiceFile =null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    //    Toast.makeText(getActivity(),"Destroy Frag",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      //  hideSoftKeyboard();

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
            if(voiceFile!=null)
                if(voiceFile.exists()){
                    voiceFile.delete();
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
            voiceFile = new File(note.FilePath);





        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_voice_view, container, false);
        removeShadowForNewApi21(rootView);


        txtNoteAddDate= (TextView) rootView.findViewById(R.id.txtNoteAddDate);

        txtNoteSubject= (TextView) rootView.findViewById(R.id.txtNoteSubject);

        txtNoteDetail = (TextView) rootView.findViewById(R.id.txtNoteDetail);



        //  m.prepare();
      //  m.start();
        playPauseVoiceBtn = (ToggleButton) rootView.findViewById(R.id.playPauseVoiceBtn);
        stopVoiceBtn = (ImageButton) rootView.findViewById(R.id.stopVoiceBtn);
        stopVoiceBtn.setEnabled(false);

         playPauseVoiceBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                                     //        playPauseVoiceBtn.setText("Play");
                                 }
                             });
                         } catch (IOException e) {
                             e.printStackTrace();
                             getActivity().finish();
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
                     }else{
                         playPauseVoiceBtn.setChecked(false);
                     }
                 }

             }

         });


             stopVoiceBtn.setOnClickListener(new View.OnClickListener()

             {
                 @Override
                 public void onClick (View v){
                 if (mediaPlayer != null) {
                     mediaPlayer.stop();
                     mediaPlayer.release();
                     mediaPlayer = null;
                     stopVoiceBtn.setEnabled(false);
                 }

             }
             }

             );

             fillDate();


             return rootView;
         }

    }

