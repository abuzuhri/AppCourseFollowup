package ae.ac.adec.coursefollowup.ConstantApp;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.BaseActivity;
import ae.ac.adec.coursefollowup.services.AppAction;

/**
 * Created by JMA on 3/8/2015.
 */
public class ConstantVariable {

    public static String MAIN_APP_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CourseFollowup";
    public static String NOTES_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CourseFollowup/Notes";
    public static String NOTES_TEXT_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CourseFollowup/Notes/Text";
    public static String NOTES_IMAGE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CourseFollowup/Notes/Image";
    public static String NOTES_VOICE_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CourseFollowup/Notes/Voice";
    public static String NOTES_VIDEO_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CourseFollowup/Notes/Video";
    public static String lang = "en";
    public static Boolean isValidPause=true,isTimesDialog=false;

    public enum TaskType {
        Assignment(1),
        Homework(2),
        Reminder(3),
        Revision(4),
        Meeting(5);
        public int id;

        private TaskType(int id) {
            this.id = id;
        }

        public static int fromInteger(int x) {
            switch (x) {
                case 1:
                    return R.string.task_ass;
                case 2:
                    return R.string.task_hw;
                case 3:
                    return R.string.task_rem;
                case 4:
                    return R.string.task_rev;
                case 5:
                    return R.string.task_meet;
            }
            return 0;
        }

    }

    public enum TimeFrame {
        Current(0),
        Past(1),
        All(2);
        public int id;

        private TimeFrame(int id) {
            this.id = id;
        }
    }
    public enum NoteType_Tabs {
        Text(0),
        Image(1),
        Voice(2),
        Video(3);
        public int id;

        private NoteType_Tabs(int id) {
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
        Profile(101),
        Language(102);

        public int id;

        private Category(int id) {
            this.id = id;
        }
    }

    public enum SystemNotificationType {
        Course(1),
        Exam(2),
        Task(3);
        public int id;

        private SystemNotificationType(int id) {
            this.id = id;
        }
    }

    public enum SyncStatus {
        Normal(0),
        New(1),
        Updated(2);
        public int id;

        private SyncStatus(int id) {
            this.id = id;
        }
    }

    public enum DayOfWeek {
        Saturday(7),
        Sunday(1),
        Monday(2),
        Tuesday(3),
        Wednesday(4),
        Thursday(5),
        Friday(6);
        public int id;

        private DayOfWeek(int id) {
            this.id = id;
        }

        public static int fromInteger(int x) {
            switch (x) {
                case 7:
                    return R.string.saturday;
                case 1:
                    return R.string.sunday;
                case 2:
                    return R.string.monday;
                case 3:
                    return R.string.tuesday;
                case 4:
                    return R.string.wednesday;
                case 5:
                    return R.string.thursday;
                case 6:
                    return R.string.friday;
            }
            return 0;
        }
        public static int fromInteger_shortcut(int x) {
            switch (x) {
                case 7:
                    return R.string.sat;
                case 1:
                    return R.string.sun;
                case 2:
                    return R.string.mon;
                case 3:
                    return R.string.tue;
                case 4:
                    return R.string.wed;
                case 5:
                    return R.string.thu;
                case 6:
                    return R.string.fri;
            }
            return 0;
        }
    }

    public enum NoteType {
        Voice(1),
        Text(2),
        Video(3),
        Image(4);
        public int id;

        private NoteType(int id) {
            this.id = id;
        }

        public static int fromInteger(int x) {
            switch (x) {
                case 1:
                    return R.string.voice;
                case 2:
                    return R.string.text;
                case 3:
                    return R.string.video;
                case 4:
                    return R.string.image;
            }
            return 0;
        }

    }

    public enum CloudStatus {
        Normal(0),
        NeedUpload(1),
        NeedDownload(2);
        public int id;

        private CloudStatus(int id) {
            this.id = id;
        }
    }

    public static String getDateString(Date date) {
        if (date == null)
            return "";
        if (lang.equals("ar"))
            return SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("ar")).format(date);
        else
            return SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, new Locale("en")).format(date);
    }

    public static String getTimeString(Date date) {
        if (date == null)
            return "";
        DateFormat formatter = null;
        if (lang.equals("ar"))
            formatter = new SimpleDateFormat("hh:mm: aa", new Locale("ar"));
        else
            formatter = new SimpleDateFormat("hh:mm: aa", new Locale("en"));
        return formatter.format(date);
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currenTimeZone = new Date((long) 1379487711 * 1000);
        return currenTimeZone;
    }

    public static int getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day;
    }

    public static Boolean isVersionUnder21() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return false;
        } else {
            return true;
        }
    }
}
