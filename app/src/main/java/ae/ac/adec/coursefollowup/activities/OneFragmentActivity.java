package ae.ac.adec.coursefollowup.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ae.ac.adec.coursefollowup.R;

/**
 * Created by Tareq on 03/05/2015.
 */

public class OneFragmentActivity extends BaseActivity {

    public  static final  String  FRAGMENT="FRAGMENT";
    public  static final  String  HAVE_TOOLBAR_SHARDOW="HAVE_TOOLBAR_SHARDOW";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Boolean HaveToolbarShadow = intent.getBooleanExtra(HAVE_TOOLBAR_SHARDOW,false);

        //Remove defult shadow in toolbar for use flexible space
        if(!HaveToolbarShadow){
            setContentView(R.layout.activity_without_toolbar_shadow);
        }else setContentView(R.layout.activity_main);



        final Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneFragmentActivity.this.finish();
            }
        });


        String FragmentName = intent.getStringExtra(FRAGMENT);
        Log.i("tg","FragmentName = > "+FragmentName);

        Fragment fragment= Fragment.instantiate(this,FragmentName);
        selectFragment(fragment);
    }



    public  void  selectFragment(Fragment fragment){
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, fragment).commit();
        }
    }
}
