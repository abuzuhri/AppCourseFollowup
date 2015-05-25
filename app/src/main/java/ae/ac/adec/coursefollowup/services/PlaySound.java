package ae.ac.adec.coursefollowup.services;

import android.R.bool;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import ae.ac.adec.coursefollowup.R;

public class PlaySound {
    static MediaPlayer mp;
    static Vibrator v;
//    static Boolean sound; // false off true on

//    public static Boolean getSound() {
//        return sound;
//    }

    public static void setSoundFromSP(Context c) {
//        PlaySound.sound = PreferenceManager.getDefaultSharedPreferences(c)
//                .getBoolean("sound_on", true);
    }

    public static void play(Context c) {
//        setSoundFromSP(c);
//        if (sound) {
        if (mp == null)
            mp = MediaPlayer.create(c, R.raw.notification);
        if (!mp.isPlaying())
            mp.start();
//        }
    }

    public static void vibrate(Context c) {
        if (v == null)
            v = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public static void stop(Context c) {
        if (mp == null)
            mp = MediaPlayer.create(c, R.raw.notification);
        if (mp.isPlaying()) {
            mp.stop();
            mp.release();
        }
    }

    public static Boolean isPlay(Context c) {
        if (mp == null)
            mp = MediaPlayer.create(c, R.raw.notification);
        return mp.isPlaying();
    }
}
