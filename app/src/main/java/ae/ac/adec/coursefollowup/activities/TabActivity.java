package ae.ac.adec.coursefollowup.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ae.ac.adec.coursefollowup.R;

/**
 * Created by Tareq on 03/12/2015.
 */
public class TabActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_without_toolbar_shadow);

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        Drawable();
    }
}
