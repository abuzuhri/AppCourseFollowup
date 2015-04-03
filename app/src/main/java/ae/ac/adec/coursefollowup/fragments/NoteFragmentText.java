package ae.ac.adec.coursefollowup.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Calendar;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import android.graphics.Bitmap;

import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.views.view.CustomEditText;

/**
 * Created by MyLabtop on 3/28/2015.
 */
public class NoteFragmentText extends BaseFragment {

    CustomEditText customEditText = null;
    String outputFile  = null;
    boolean success = false;
    String tempS =null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();
     //   Toast.makeText(getActivity(),"onActivity",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setSubTitle("Text Note");


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void writeToFile(String data) {

        String s_directory = ConstantVariable.NOTES_TEXT_DIRECTORY;
        String s_time= ConstantVariable.getTimeString(Calendar.getInstance().getTime()).toString();
        String s_date= ConstantVariable.getDateString(Calendar.getInstance().getTime()).toString();
        String s_courseName= OneFragmentActivity.getCourseName();


        outputFile = s_directory+"/TXT_" +s_courseName+"_"+ s_date+ "_" + s_time +".txt";


        //    File outputFilex = new File(outputFile);

        //    FileOutputStream fos = new FileOutputStream(outputFile);

        try
        {
         //   File root = new File(s_directory);
         //   if (!root.exists()) {
         //       root.mkdirs();
        //    }
            File gpxfile = new File(outputFile);
            if(!gpxfile.exists()){
                gpxfile.createNewFile();
            }
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(customEditText.getText().toString());
            writer.flush();
            writer.close();
            success = true;
       //     Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();

        }
        catch(IOException e)
        {
            AppLog.i( "File write failed: " + e.toString());
        }


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
        writeToFile(customEditText.getText().toString());

        if(success) {
            OneFragmentActivity.setFilePath(outputFile);
            OneFragmentActivity.setNoteType("I have an Text note");
            OneFragmentActivity.temp = customEditText.getText().toString();
        }else{
            OneFragmentActivity.setFilePath(null);
            OneFragmentActivity.setNoteType("Failed to create Text Note!");
        }
        getActivity().finish();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_note_text, container, false);
        removeShadowForNewApi21(rootView);

        LinearLayout v = (LinearLayout)rootView.findViewById(R.id.compoundView);

        customEditText = new CustomEditText(getActivity(),null);
        customEditText.setLines(30);
        customEditText.setGravity(Gravity.LEFT | Gravity.TOP);
        v.addView(customEditText);

        if(OneFragmentActivity.temp!=null)
            customEditText.setText(OneFragmentActivity.temp);
        return rootView;
    }



}
