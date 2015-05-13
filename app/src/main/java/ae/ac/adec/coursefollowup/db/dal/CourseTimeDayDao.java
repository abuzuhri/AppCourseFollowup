package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
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

    public void buildNotifications(CourseTimeDay courseTime, Boolean isRepeat, Course course, long startTime, int dayOfWeek) throws BusinessRoleError {
        NotificationDao notificationDao = new NotificationDao();
        // remove old related with this time if found
        notificationDao.deleteRelatedWithCourseTime(courseTime);

        HolidayDao holidayDao = new HolidayDao();
        if (isRepeat) {
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(course.StartDate);
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(course.EndDate);

            Calendar temp = Calendar.getInstance();
            temp.setTimeInMillis(startTime);
            startCal.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
            startCal.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));

            int day;
            while (startCal.compareTo(endCal) != 1) {
                day = startCal.get(Calendar.DAY_OF_WEEK);
                if (day == dayOfWeek) {
                    if (holidayDao.getHolidaysOnDate(startCal.getTimeInMillis()).size() > 0)
                        notificationDao.Add(startCal.getTimeInMillis(), startCal.getTimeInMillis() - (60 * 60 * 1000), course,
                                courseTime, null, null, true, false, false);
                    else
                        notificationDao.Add(startCal.getTimeInMillis(), startCal.getTimeInMillis() - (60 * 60 * 1000), course,
                                courseTime, null, null, false, false, false);
                }
                startCal.add(Calendar.DATE, 1);
            }

        } else {

            if (holidayDao.getHolidaysOnDate(startTime).size() > 0)
                notificationDao.Add(startTime, startTime - (60 * 60 * 1000), course, courseTime, null, null, true, false, false);
            else
                notificationDao.Add(startTime, startTime - (60 * 60 * 1000), course, courseTime, null, null, false, false, false);
        }
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

        // BR_TIM_001
        long cCount = getConflictTimes(std, ID);
        if (cCount > 0)
            throw new BusinessRoleError(R.string.BR_TIM_001);

        long result = std.save();
        AppLog.i("Result: row " + result + " added, result id >" + result);

        buildNotifications(CourseTimeDay.load(CourseTimeDay.class, result), isRepeat, course, startTime, DayOfWeek);

    }

    public void delete(long Id) throws BusinessRoleError {

        CourseTimeDay ctd = CourseTimeDay.load(CourseTimeDay.class, Id);

        ActiveAndroid.beginTransaction();
        try {
            new NotificationDao().deleteRelatedWithCourseTime(ctd);
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

    public List<CourseTimeDay> getTimesByCourse(Course course) {
        return new Select()
                .from(CourseTimeDay.class)
                .where("Course=?", course.getId())
                .orderBy("Start_time ASC")
                .execute();
    }

    public List<CourseTimeDay> getTimesByCourse_DayOfWeek(Course course, int dayOfWeek) {
        return new Select()
                .from(CourseTimeDay.class)
                .where("Course=? AND DayOfWeek=?", course.getId(), dayOfWeek)
                .orderBy("Start_time ASC")
                .execute();
    }

    public long getConflictTimes(CourseTimeDay ctd, Long id) {

        //ToDo get conflict times in non repeated times

        if (id != null && id != 0) {
            if (ctd.IsRepeat)
                return new Select()
                        .from(CourseTimeDay.class)
                        .where("((Start_time<=? OR Start_time<=?)AND(End_time>=? OR End_time>=?)) AND DayOfWeek=? AND _ID!=?",
                                ctd.Start_time.getTime(), ctd.End_time.getTime(),
                                ctd.Start_time.getTime(), ctd.End_time.getTime(), ctd.DayOfWeek, id)
                        .count();
            else
                return new Select()
                        .from(CourseTimeDay.class)
                        .where("((Start_time<=? OR Start_time<=?)AND(End_time>=? OR End_time>=?)) AND _ID!=?",
                                ctd.Start_time.getTime(), ctd.End_time.getTime(),
                                ctd.Start_time.getTime(), ctd.End_time.getTime(), id)
                        .count();
        } else {
            if (ctd.IsRepeat)
                return new Select()
                        .from(CourseTimeDay.class)
                        .where("((Start_time<=? OR Start_time<=?)AND(End_time>=? OR End_time>=?)) AND DayOfWeek=?",
                                ctd.Start_time.getTime(), ctd.End_time.getTime(),
                                ctd.Start_time.getTime(), ctd.End_time.getTime(), ctd.DayOfWeek)
                        .count();
            else
                return new Select()
                        .from(CourseTimeDay.class)
                        .where("((Start_time<=? OR Start_time<=?)AND(End_time>=? OR End_time>=?))",
                                ctd.Start_time.getTime(), ctd.End_time.getTime(),
                                ctd.Start_time.getTime(), ctd.End_time.getTime())
                        .count();
        }
    }

    public List<CourseTimeDay> getCoursesTimesOnDate(long startDate, long endDate, int dayOfWeek) {
        return new Select()
                .from(CourseTimeDay.class)
                .where("(Start_time>=? AND Start_time<? AND IsRepeat=?)" +
                        " OR (DayOfWeek=? AND IsRepeat=?)", startDate, endDate, false, dayOfWeek, true)
                .orderBy("Start_time ASC")
                .execute();
    }

    public List<CourseTimeDay> getTimesWithinPeriod(long startDate, long endDate) {
        List<Course> courses = new CourseDao().getCoursesWithinPeriod(startDate, endDate);

        List<CourseTimeDay> ctd_list = new ArrayList<CourseTimeDay>();
        List<CourseTimeDay> ct = new ArrayList<CourseTimeDay>();
        for (int i = 0; i < courses.size(); i++) {
            ct = new Select()
                    .from(CourseTimeDay.class)
                    .where("(Start_time>=? AND Start_time<? AND IsRepeat=?)" +
                            " OR (IsRepeat=?) AND Course=?", startDate, endDate, false, true, courses.get(i).getId())
                    .orderBy("Start_time ASC")
                    .execute();
            for (int j = 0; j < ct.size(); j++)
                ctd_list.add(ct.get(j));
        }

        return ctd_list;
    }

    public List<CourseTimeDay> getTimes(int dayOfWeek, long startDate_oneTime, long endDate_oneTime, Course course) {
        return new Select()
                .from(CourseTimeDay.class)
                .where("(Start_time>=? AND Start_time<? AND IsRepeat=?)" +
                        " OR (DayOfWeek=? AND IsRepeat=?) AND Course=?", startDate_oneTime, endDate_oneTime, false, dayOfWeek
                        , true, course.getId())
                .orderBy("Start_time ASC")
                .execute();
    }
}
