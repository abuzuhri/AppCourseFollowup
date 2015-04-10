package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.db.models.Notification;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;

/**
 * Created by JMA on 3/21/2015.
 */
public class NotificationDao extends BaseDao {
    public Notification getById(long id) {
        return Notification.load(Notification.class, id);
    }

    public void Edit(long ID, long calenderDateTime, long notificationDateTime, Course course, CourseTimeDay courseTime,
                     Task task, Exam exam, Boolean isHoliday, Boolean isDone, Boolean isNoNeedNothing) throws BusinessRoleError {
        AppLog.i("Edit => " + ID);
        AddEdit(ID, calenderDateTime, notificationDateTime, course, courseTime, task, exam, isHoliday, isDone, isNoNeedNothing);
    }

    public void Add(long calenderDateTime, long notificationDateTime, Course course, CourseTimeDay courseTime,
                    Task task, Exam exam, Boolean isHoliday, Boolean isDone, Boolean isNoNeedNothing) throws BusinessRoleError {
        AddEdit(null, calenderDateTime, notificationDateTime, course, courseTime, task, exam, isHoliday, isDone, isNoNeedNothing);
    }

    private void AddEdit(Long ID, long calenderDateTime, long notificationDateTime, Course course, CourseTimeDay courseTime,
                         Task task, Exam exam, Boolean isHoliday, Boolean isDone, Boolean isNoNeedNothing) throws BusinessRoleError {
        Notification note = null;
        if (ID != null && ID != 0)
            note = Notification.load(Notification.class, ID.longValue());
        else
            note = new Notification();

        Calendar cdt = Calendar.getInstance();
        cdt.setTimeInMillis(calenderDateTime);
        note.CalenderDateTime = cdt.getTime();

        Calendar ndt = Calendar.getInstance();
        ndt.setTimeInMillis(notificationDateTime);
        note.NotificationDateTime = ndt.getTime();

        note.Course = course;
        note.Task = task;
        note.Exam = exam;
        note.isDone = isDone;
        note.IsHoliday = isHoliday;
        note.IsNoNeedNothing = isNoNeedNothing;
        note.CourseTime = courseTime;

        /*int countC = new Select().from(Course.class).where("Name=?", course.Name).count();
        if (countC > 0)
            throw new BusinessRoleError(R.string.BR_HLD_003);*/

        long result = note.save();
        AppLog.i("Result: row " + result + " added, result id >" + result);
    }

    public void delete(long Id) throws BusinessRoleError {

        Notification note = Notification.load(Notification.class, Id);

        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(note);
            note.delete();
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public List<Notification> getAll(int position) {
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        if (position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(Notification.class)
                    .where("NotificationDateTime > ?", currentTimeInMillis)
                    .orderBy("NotificationDateTime ASC")
                    .execute();
        } else if (position == ConstantVariable.TimeFrame.Past.id) {
            return new Select()
                    .from(Notification.class)
                    .where("NotificationDateTime <= ?", currentTimeInMillis)
                    .orderBy("NotificationDateTime ASC")
                    .execute();
        } else {
            return new Select()
                    .from(Notification.class)
                    .orderBy("NotificationDateTime ASC")
                    .execute();
        }
    }

    public void deleteRelatedWithCourseTime(CourseTimeDay courseTime) {
        new Delete().from(Notification.class).where("CourseTime=?", courseTime.getId()).execute();
    }
    public void deleteRelatedWithExam(Exam exam) {
        new Delete().from(Notification.class).where("Exam=?", exam.getId()).execute();
    }
    public void deleteRelatedWithTask(Task task) {
        new Delete().from(Notification.class).where("Task=?", task.getId()).execute();
    }
}
