package ae.ac.adec.coursefollowup.db.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.db.dal.NotificationDao;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Notification;

/**
 * Created by Tareq on 03/02/2015.
 */
public class SetUpService extends IntentService {

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        AppLog.i("Setup service started");
    }

    public SetUpService() {
        super("SetUpService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Notification> oldNotifications = new NotificationDao().getAll(1);
        for (Notification notification : oldNotifications) {
            if (!notification.isDone) {
                notification.isDone = true;
                notification.save();
            }
        }
    }


}
