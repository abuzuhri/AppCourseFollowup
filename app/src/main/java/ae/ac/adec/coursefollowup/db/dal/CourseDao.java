package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;

/**
 * Created by JMA on 3/21/2015.
 */
public class CourseDao extends BaseDao {
    public Course getById(long id) {
        return Course.load(Course.class, id);
    }

    public void Edit(long ID, String Name, String code, String room, String building,
                     String teacher, String colorCode, Semester semester, long startDate, long endDate, Boolean isNotify) throws BusinessRoleError {
        AppLog.i("Edit => " + ID);
        AddEdit(ID, Name, code, room, building, teacher, colorCode, semester, startDate, endDate, isNotify);
    }

    public void Add(String Name, String code, String room, String building,
                    String teacher, String colorCode, Semester semester, long startDate, long endDate, Boolean isNotify) throws BusinessRoleError {
        AddEdit(null, Name, code, room, building, teacher, colorCode, semester, startDate, endDate, isNotify);
    }

    private void AddEdit(Long ID, String name, String code, String room, String building,
                         String teacher, String colorCode, Semester semester, long startDate, long endDate, Boolean isNotify) throws BusinessRoleError {
        Course course = null;
        if (ID != null && ID != 0)
            course = Course.load(Course.class, ID.longValue());
        else
            course = new Course();
        course.Name = name;

        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTimeInMillis(startDate);
        course.StartDate = startDateCal.getTime();

        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTimeInMillis(endDate);
        course.EndDate = endDateCal.getTime();

        course.Code=code;
        course.Room=room;
        course.Building=building;
        course.Teacher=teacher;
        course.ColorCode=colorCode;
        course.Semester=semester;
        course.IsNotify=isNotify;

        if (endDate < startDate)
            throw new BusinessRoleError(R.string.BR_HLD_001);

        int countC = new Select().from(Course.class).where("Name=?", course.Name).count();
        if (countC > 0)
            throw new BusinessRoleError(R.string.BR_HLD_003);

        long result = course.save();
        AppLog.i("Result: row " + name + " added, result id >" + result);


    }

    public void delete(long Id) throws BusinessRoleError {

        Course course = Course.load(Course.class, Id);

        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(course);
            course.delete();
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public List<Course> getAll(int position) {
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        if (position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(Course.class)
                    .where("EndDate > ?", currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        } else if (position == ConstantVariable.TimeFrame.Past.id) {
            return new Select()
                    .from(Course.class)
                    .where("EndDate <= ?", currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        } else {
            return new Select()
                    .from(Course.class)
                    .orderBy("StartDate ASC")
                    .execute();
        }
    }
}
