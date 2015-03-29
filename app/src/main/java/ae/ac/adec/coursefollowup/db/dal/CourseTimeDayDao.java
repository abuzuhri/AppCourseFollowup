package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;

/**
 * Created by JMA on 3/21/2015.
 */
public class CourseTimeDayDao extends BaseDao {
    public CourseTimeDay getById(long id) {
        return CourseTimeDay.load(CourseTimeDay.class, id);
    }

    public void Edit(long ID, Course course, long startTime, long endTime, Boolean isRepeat, int dayOfWeek) throws BusinessRoleError {
        AppLog.i("Edit => " + ID);
        AddEdit(ID, course, startTime, endTime, isRepeat, dayOfWeek);
    }

    public void Add(Course course, long startTime, long endTime, Boolean isRepeat, int dayOfWeek) throws BusinessRoleError {
        AddEdit(null, course, startTime, endTime, isRepeat, dayOfWeek);
    }

    private void AddEdit(Long ID, Course course, long startTime, long endTime, Boolean isRepeat, int DayOfWeek) throws BusinessRoleError {
        CourseTimeDay std = null;
        if (ID != null && ID != 0)
            std = CourseTimeDay.load(CourseTimeDay.class, ID.longValue());
        else
            std = new CourseTimeDay();
        std.Course = course;

        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTimeInMillis(startTime);
        std.Start_time = startDateCal.getTime();

        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTimeInMillis(endTime);
        std.End_time = endDateCal.getTime();

        std.IsRepeat = isRepeat;
        std.DayOfWeek = DayOfWeek;

        if (endTime < startTime)
            throw new BusinessRoleError(R.string.BR_HLD_001);

        long result = std.save();
        AppLog.i("Result: row " + result + " added, result id >" + result);


    }

    public void delete(long Id) throws BusinessRoleError {

        CourseTimeDay ctd = CourseTimeDay.load(CourseTimeDay.class, Id);

        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(ctd);
            ctd.delete();
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public List<CourseTimeDay> getAll(int position) {
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        if (position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(CourseTimeDay.class)
                    .where("End_time > ?", currentTimeInMillis)
                    .orderBy("Start_time ASC")
                    .execute();
        } else if (position == ConstantVariable.TimeFrame.Past.id) {
            return new Select()
                    .from(CourseTimeDay.class)
                    .where("End_time <= ?", currentTimeInMillis)
                    .orderBy("Start_time ASC")
                    .execute();
        } else {
            return new Select()
                    .from(CourseTimeDay.class)
                    .orderBy("Start_time ASC")
                    .execute();
        }
    }

    public List<CourseTimeDay> getTimesByDay(int dayOfWeek) {
        return new Select()
                .from(CourseTimeDay.class)
                .where("DayOfWeek=?", dayOfWeek)
                .orderBy("Start_time ASC")
                .execute();
    }
}
