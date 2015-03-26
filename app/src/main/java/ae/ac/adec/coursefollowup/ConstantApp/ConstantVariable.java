package ae.ac.adec.coursefollowup.ConstantApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by JMA on 3/8/2015.
 */
public class ConstantVariable {

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


        public static String fromInteger(int x) {
            switch(x) {
                case 1:
                    return "Assignment";
                case 2:
                    return "Homework";
                case 3:
                    return "Reminder";
                case 4:
                    return "Revision";
                case 5:
                    return "Meeting";
            }
            return null;
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

    public enum SystemNotificationType{
        Course(1),
        Exam(2),
        Task(3);
        public int id;
        private SystemNotificationType(int id){this.id=id;}
    }

    public enum SyncStatus{
        Normal(0),
        New(1),
        Updated(2);
        public int id;
        private SyncStatus(int id){this.id=id;}
    }

    public enum DayOfWeek{
        Saturday(1),
        Sunday(2),
        Monday(3),
        Tuesday(4),
        Wednesday(5),
        Thursday(6),
        Friday(7);
        public int id;
        private DayOfWeek(int id){this.id=id;}
        public static String fromInteger(int x) {
            switch(x) {
                case 1:
                    return "Saturday";
                case 2:
                    return "Sunday";
                case 3:
                    return "Monday";
                case 4:
                    return "Tuesday";
                case 5:
                    return "Wednesday";
                case 6:
                    return "Thursday";
                case 7:
                    return "Friday";
            }
            return null;
        }
    }

    public enum NoteType{
        Voice(1),
        Text(2),
        Video(3),
        Image(4);
        public int id;
        private NoteType(int id){this.id=id;}
    }
    public enum CloudStatus{
        Normal(0),
        NeedUpload(1),
        NeedDownload(2);
        public int id;
        private CloudStatus(int id){this.id=id;}
    }

    public static String getDateString(Date date) {
        if(date==null)
            return "";
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
