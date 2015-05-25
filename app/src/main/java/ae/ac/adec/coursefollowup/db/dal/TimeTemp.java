package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.annotation.Column;

import java.util.Date;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.db.models.Course;

/**
 * Created by JMA on 5/3/2015.
 */
public class TimeTemp {

    public static long Start_time;
    public static long End_time;
    public static int DayOfWeek;
    public static Boolean IsRepeat;
    public static int Sync_status_typeID;

    public TimeTemp(long start_time, long end_time, int dayOfWeek, Boolean isRepeat) {
        Start_time = start_time;
        End_time = end_time;
        DayOfWeek = dayOfWeek;
        IsRepeat = isRepeat;
    }

    public static void setEnd_time(long end_time) {
        End_time = end_time;
    }

    public static void setDayOfWeek(int dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public static void setIsRepeat(Boolean isRepeat) {
        IsRepeat = isRepeat;
    }

    public static void setSync_status_typeID(int sync_status_typeID) {
        Sync_status_typeID = sync_status_typeID;
    }

    public static long getStart_time() {
        return Start_time;
    }

    public static long getEnd_time() {
        return End_time;
    }

    public static int getDayOfWeek() {
        return DayOfWeek;
    }

    public static Boolean getIsRepeat() {
        return IsRepeat;
    }

    public static int getSync_status_typeID() {
        return Sync_status_typeID;
    }

    public static void setStart_time(long start_time) {
        Start_time = start_time;
    }
}
