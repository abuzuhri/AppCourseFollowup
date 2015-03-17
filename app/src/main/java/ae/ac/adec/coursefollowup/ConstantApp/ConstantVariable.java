package ae.ac.adec.coursefollowup.ConstantApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by JMA on 3/8/2015.
 */
public class ConstantVariable {

    public enum TimeFrame {
        Current(0),
        Past(1),
        All(2);
        public int id;
        private TimeFrame(int id) {
            this.id = id;
        }
    }

    public enum Category {
        Dashboard(10),
        Calender(20),
        Tasks(30),
        Notes(430),
        Exams(50),
        Semesters(60),
        Years(65),
        Classes(70),
        Holiday(80),
        Search(99),
        Setting(100),
        Test(1000),
        Profile(101);

        public int id;

        private Category(int id) {
            this.id = id;
        }
    }

    public static String getDateString(Date date) {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, java.util.Locale.getDefault()).format(date);
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currenTimeZone=new Date((long)1379487711*1000);
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
