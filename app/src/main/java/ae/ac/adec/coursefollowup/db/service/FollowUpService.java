package ae.ac.adec.coursefollowup.db.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.MainActivity;
import ae.ac.adec.coursefollowup.db.dal.NotificationDao;
import ae.ac.adec.coursefollowup.db.models.Notification;
import ae.ac.adec.coursefollowup.receivers.SimpleWakefulReceiver;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.services.PlaySound;

/**
 * Created by Tareq on 03/02/2015.
 */
public class FollowUpService extends IntentService {

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        AppLog.i("Service Started");
    }

    public FollowUpService() {
        super("FollowUpService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationDao notificationDao = new NotificationDao();
        List<Notification> notifications = notificationDao.getCurrent();
        AppLog.i(notifications.size() + " Notification");
        if (notifications.size() > 0) {
            Notification notification = notifications.get(0);
            AppLog.i("Notification found");
            String tag = "", title = "Course Follow Up";
            if (notification.Course != null)
                tag = notification.Course.Name + " class starts after 15 Minute";
            else if (notification.Exam != null)
                tag = notification.Exam.Course.Name + " exam starts after 4 Hours";
            else if (notification.Task != null)
                tag = notification.Task.Course.Name + " " + notification.Task.Name + " ends Tomorrow";

            Uri sound = null;
            // push notification
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (am.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                // silent mode
            } else if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(1200);
            } else {
                sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
                //PlaySound.play(getBaseContext());
            }

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getBaseContext())
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("Course Follow Up")
                            .setContentText(tag).setAutoCancel(true);

            if (sound != null)
                mBuilder.setSound(sound);
            Intent openHomePageActivity = new Intent("ae.ac.adec.coursefollowup.activities.MainActivity");
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntent(openHomePageActivity);

            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0, mBuilder.build());

            //******
            notification.isDone = true;
            notification.save();
        }
        //SimpleWakefulReceiver.completeWakefulIntent(intent);
    }
}
