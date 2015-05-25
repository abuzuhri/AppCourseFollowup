package ae.ac.adec.coursefollowup.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.Calendar;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by MyLabtop on 3/28/2015.
 */
public class NoteFragmentRecordSound extends BaseFragment {
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    private ToggleButton startRecord = null;
    //  private Button playRecord   = null;
    private Button stopRecord = null;
    boolean isRecording = false;


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
        String s_time = ConstantVariable.getTimeString(Calendar.getInstance().getTime()).toString();
        String s_date = ConstantVariable.getDateString(Calendar.getInstance().getTime()).toString();
        String s_courseName = OneFragmentActivity.getCourseName();

        outputFile = s_directory + "/VIC_" + s_courseName + "_" + s_date + "_" + s_time + ".3gp";


        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    public void AddEdit() {
        try {
            stopRecord.performClick();
        } catch (Exception e) {

        }
        OneFragmentActivity.setFilePath(outputFile);
        OneFragmentActivity.setNoteType(getString(R.string.voice_captured));
        settingLanguage("ar");
        getActivity().finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_voice, container, false);
        removeShadowForNewApi21(rootView);


        startRecord = (ToggleButton) rootView.findViewById(R.id.startRecord);


        startRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IllegalStateException e) {

                        e.printStackTrace();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    //   startRecord.setText("Stop Recording");
                    //       stopRecord.setEnabled(true);
                    isRecording = true;
                } else {
                    try {
                        myAudioRecorder.stop();
                        myAudioRecorder.release();
                        myAudioRecorder = null;
                        OneFragmentActivity.setFilePath(outputFile);
                        OneFragmentActivity.setNoteType(getString(R.string.voice_captured));
                        getActivity().finish();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }


            }
        });


        return rootView;
    }


}
