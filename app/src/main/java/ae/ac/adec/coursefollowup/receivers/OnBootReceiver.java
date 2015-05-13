package ae.ac.adec.coursefollowup.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.db.service.FollowUpService;
import ae.ac.adec.coursefollowup.db.service.SetUpService;

/**
 * Created by JMA on 4/11/2015.
 */
public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppLog.i("Receiver called");
        context.startService(new Intent(context, SetUpService.class));
        startCheckService(context);
    }

    private void startCheckService(Context context) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, 0);
        Intent myIntent = new Intent(context,
                FollowUpService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                context, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                c.getTimeInMillis(),
                60 * 1000, pendingIntent); // change it to call
    }
}
