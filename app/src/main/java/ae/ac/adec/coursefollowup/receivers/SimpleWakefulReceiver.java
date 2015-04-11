package ae.ac.adec.coursefollowup.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import ae.ac.adec.coursefollowup.db.service.FollowUpService;

/**
 * Created by JMA on 4/11/2015.
 */
public class SimpleWakefulReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent service = new Intent(context, FollowUpService.class);
//        startWakefulService(context, service);
    }
}
