package ae.ac.adec.coursefollowup.activities;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.Calendar;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.service.FollowUpService;


public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstantVariable.lang = getString(R.string.lang);
        startServices();

        int x = 10;
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        SetupToolbarShadow();

        Drawable();
        AppLog.i("jma " + x);
    }

    private void startServices() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        Intent myIntent = new Intent(getBaseContext(),
                FollowUpService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                getBaseContext(), 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getBaseContext()
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(),
                60 * 1000, pendingIntent); // change it to call
    }

}
