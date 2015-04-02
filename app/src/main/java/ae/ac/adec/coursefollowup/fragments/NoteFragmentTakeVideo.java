package ae.ac.adec.coursefollowup.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import android.graphics.Bitmap;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import ae.ac.adec.coursefollowup.ConstantApp.AppLog;

/**
 * Created by MyLabtop on 3/28/2015.
 */
public class NoteFragmentTakeVideo extends BaseFragment {
    String outputFile = null;
    boolean successRecord = false;
    private static final int VIDEO_CAPTURE = 101;
    private Uri fileUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();

    }
    public void startRecording()
    {

        String s_directory = ConstantVariable.NOTES_VIDEO_DIRECTORY;
        String s_time= ConstantVariable.getTimeString(Calendar.getInstance().getTime()).toString();
        String s_date= ConstantVariable.getDateString(Calendar.getInstance().getTime()).toString();
        String s_courseName= OneFragmentActivity.getCourseName();


         outputFile = s_directory + "/VID_" +s_courseName+"_"+ s_date+ "_" + s_time +".mp4";


        File mediaFile = new File(outputFile);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = Uri.fromFile(mediaFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setSubTitle("Capture Video");
        startRecording();
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

                break;
            default:
                break;
        }
        return true;
    }

    public void AddEdit(){
        //   OneFragmentActivity.setFilePath(outputFile);

        if(successRecord) {
            OneFragmentActivity.setFilePath(outputFile);
            OneFragmentActivity.setNoteType("I have a Video note");
        }else{
            OneFragmentActivity.setFilePath(null);
            OneFragmentActivity.setNoteType("No Video Recorded!");
        }
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == getActivity().RESULT_OK) {
                 successRecord = true;
                AddEdit();
             } else if (resultCode == getActivity().RESULT_CANCELED) {
                successRecord = false;
                AddEdit();
            } else {

            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return null;
    }
}
