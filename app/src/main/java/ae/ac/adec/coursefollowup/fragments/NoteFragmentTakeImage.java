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
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;
import android.graphics.Bitmap;

import android.view.View.OnClickListener;
import android.widget.ImageView;


import ae.ac.adec.coursefollowup.ConstantApp.AppLog;

/**
 * Created by MyLabtop on 3/28/2015.
 */
public class NoteFragmentTakeImage extends BaseFragment {
   // String videoPath = "";
    boolean successRecord = false;

    ImageView imgFavorite;
      String outputFile = null;
    private Uri fileUri;
    private static final int IMAGE_CAPTURE = 100;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setSubTitle("Take Picture");
        open();
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
     //   OneFragmentActivity.setFilePath(outputFile);
        if(successRecord) {
            OneFragmentActivity.setFilePath(outputFile);
            OneFragmentActivity.setNoteType(getString(R.string.img_captured));
        }else{
            OneFragmentActivity.setFilePath(null);
        }
        getActivity().finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //settingLanguage("ar");
        if (requestCode == IMAGE_CAPTURE) {
            if (resultCode == getActivity().RESULT_OK) {

                successRecord = true;
                AddEdit();
            } else if (resultCode == getActivity().RESULT_CANCELED) {

                successRecord = false;
                AddEdit();
            } else {

            }
        }

  /*  try {
         Bitmap photo = (Bitmap) data.getExtras().get("data");
         imgFavorite.setImageBitmap(photo);
         Uri tempUri = getImageUri(getActivity().getBaseContext(), photo);
         imagePath = getRealPathFromURI(tempUri);
         //  File finalFile = new File(getRealPathFromURI(tempUri));

    }catch(NullPointerException e){
        AppLog.i("error : Null Data Image");
    }
    */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return null;
    }

    public void open(){

        String s_directory = ConstantVariable.NOTES_IMAGE_DIRECTORY;
        String s_time= ConstantVariable.getTimeString(Calendar.getInstance().getTime()).toString();
        String s_date= ConstantVariable.getDateString(Calendar.getInstance().getTime()).toString();
        String s_courseName= OneFragmentActivity.getCourseName();


        outputFile = s_directory + "/IMG_" +s_courseName+"_"+ s_date+ "_" + s_time +".jpg";

        File mediaFile = new File( outputFile);
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
         fileUri = Uri.fromFile(mediaFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
   //     Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IMAGE_CAPTURE);
    }


}
