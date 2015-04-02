package ae.ac.adec.coursefollowup.fragments;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.db.dal.NoteDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by MyLabtop on 3/28/2015.
 */
public class NoteFragmentRecordSound extends BaseFragment {
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    private Button startRecord    = null;
    private Button playRecord   = null;
    private Button stopRecord     = null;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            setSubTitle("Sound Record");
        String s_directory = ConstantVariable.NOTES_VOICE_DIRECTORY;
        String s_time= ConstantVariable.getTimeString(Calendar.getInstance().getTime()).toString();
        String s_date= ConstantVariable.getDateString(Calendar.getInstance().getTime()).toString();
        String s_courseName= OneFragmentActivity.getCourseName();

        outputFile = s_directory + "/VIC_" +s_courseName+"_"+ s_date+ "_" + s_time +".3gp";


        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);


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
        OneFragmentActivity.setFilePath(outputFile);
        OneFragmentActivity.setNoteType("I have a Note Voice!");
   getActivity().finish();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_sound, container, false);
        removeShadowForNewApi21(rootView);

//"/AppCourseFollowup/Note" +


        startRecord= (Button) rootView.findViewById(R.id.startRecord);

        stopRecord=(Button) rootView.findViewById(R.id.stopRecord);

        playRecord=(Button) rootView.findViewById(R.id.playRecord);

        stopRecord.setEnabled(false);
     //   play.setEnabled(false);


        playRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    MediaPlayer m = new MediaPlayer();
                    m.setDataSource(outputFile);
                    m.prepare();
                    m.start();
                    Toast.makeText(getActivity(), "Playing audio", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder  = null;
                stopRecord.setEnabled(false);
                playRecord.setEnabled(true);


                OneFragmentActivity.setFilePath(outputFile);
                OneFragmentActivity.setNoteType("I have a Note Voice!");

            }
        });

        startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException e  ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                startRecord.setEnabled(false);
                stopRecord.setEnabled(true);
            }
        });


        return rootView;
    }



}
