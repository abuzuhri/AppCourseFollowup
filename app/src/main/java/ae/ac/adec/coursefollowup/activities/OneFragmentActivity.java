package ae.ac.adec.coursefollowup.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.services.AppAction;

/**
 * Created by Tareq on 03/05/2015.
 */

public class OneFragmentActivity extends BaseActivity {

    //public  static final  String  FRAGMENT="FRAGMENT";
    //public  static final  String  HAVE_TOOLBAR_SHARDOW="HAVE_TOOLBAR_SHARDOW";


    static private String filePath = null;
    static private String noteType = null;
    static private String courseName = null;
    static public String temp = null;

    public static String getCourseName() {
        return courseName;
    }

    public static void setCourseName(String courseName) {
        OneFragmentActivity.courseName = courseName;
    }


    public static void setNoteType(String noteType) {
        OneFragmentActivity.noteType = noteType;
    }

    public static String getNoteType() {
        return noteType;
    }


public static void setFilePath(String newFilePath){
  filePath = newFilePath;
}
    public static String getFilePath(){
        return filePath;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetupToolbarShadow();
        Intent intent = getIntent();
        //Boolean HaveToolbarShadow = intent.getBooleanExtra(HAVE_TOOLBAR_SHARDOW,false);

        //Remove defult shadow in toolbar for use flexible space
        //if(!HaveToolbarShadow){
        //    setContentView(R.layout.activity_without_toolbar_shadow);
        //}else setContentView(R.layout.activity_main);



        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneFragmentActivity.this.finish();
            }
        });


        String FragmentName = intent.getStringExtra(AppAction.FRAGMENTEXTRA);
        AppLog.i("FragmentName = > "+FragmentName);



        Fragment fragment= Fragment.instantiate(this,FragmentName);

        //Pass Item  It to Fragment
        long ItemID = intent.getLongExtra(AppAction.IDEXTRA,0);
        Bundle args = new Bundle();
        args.putLong(AppAction.IDEXTRA, ItemID);
        fragment.setArguments(args);

        selectFragment(fragment);
    }



    public  void  selectFragment(Fragment fragment){
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, fragment).commit();
        }
    }
}
