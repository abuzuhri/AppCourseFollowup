package ae.ac.adec.coursefollowup.utils;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Constants {



    public enum TimeFrame {
        Current(1),
        Future(20),
        Past(30);
        public int id;
        private TimeFrame(int id) {
            this.id = id;
        }
    }


    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date currenTimeZone=new java.util.Date((long)1379487711*1000);
        return  currenTimeZone;
    }

    public static int getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return  day;
    }
}
