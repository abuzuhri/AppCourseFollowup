package ae.ac.adec.coursefollowup.db.dal;

import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.services.AppAction;
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

        course.Code = code;
        course.Room = room;
        course.Building = building;
        course.Teacher = teacher;
        course.ColorCode = colorCode;
        course.Semester = semester;
        course.IsNotify = isNotify;

        // BR_CRS_007
        if (endDate < startDate)
            throw new BusinessRoleError(R.string.BR_CRS_007);

        // BR_CRS_001

        // The problem occurred when executing select object with the same type of the saved object,
        // because the object which we want to save is found in the result of select.

        long cCount;
        if (ID != null && ID != 0)
            cCount = new Select().from(Course.class).where("Name=? AND _ID!=?", name, course.getId()).count();
        else
            cCount = new Select().from(Course.class).where("Name=?", name).count();
        AppLog.i(cCount + " soso");
        if (cCount > 0)
            throw new BusinessRoleError(R.string.BR_CRS_001);

        // BR_CRS_012
//        cCount = getConflictCourses(course, ID);
//        if (cCount > 0)
//            throw new BusinessRoleError(R.string.BR_CRS_012);

        // BR_CRS_003
        if ((startDate < semester.StartDate.getTime()) || (endDate > semester.EndDate.getTime()))
            throw new BusinessRoleError(R.string.BR_CRS_003);

        long result = course.save();
        AppLog.i("Result: row " + course.Name + " added, result id >" + result);
    }

    public void delete(long Id) throws BusinessRoleError {

        Course course = Course.load(Course.class, Id);
        if (course == null)
            return;
        List<Note> cNotes = new NoteDao().getNotesWithinCourse(course);
        List<Exam> cExams = new ExamDao().getExamsWithinCourse(course);
        List<Task> cTasks = new TaskDao().getTasksWithinCourse(course);

        if (cNotes != null && cNotes.size() > 0)
            throw new BusinessRoleError(R.string.BR_CRS_004);
        else if (cExams != null && cExams.size() > 0)
            throw new BusinessRoleError(R.string.BR_CRS_006);
        else if (cTasks != null && cTasks.size() > 0)
            throw new BusinessRoleError(R.string.BR_CRS_005);


        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(course);
            CourseTimeDayDao cDao = new CourseTimeDayDao();
            List<CourseTimeDay> ctd = cDao.getTimesByCourse(course);
            for (int i = 0; i < ctd.size(); i++) {
                cDao.delete(ctd.get(i).getId());
            }
            course.delete();
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessRoleError(R.string.BR_GENERAL_001);

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

    public List<Course> getCoursesWithinSemester(Semester semester) {
        return new Select()
                .from(Course.class)
                .where("semester=?", semester.getId())
                .execute();
    }

    public List<Course> getCoursesWithinPeriod(long startDate, long endDate) {
        List<Year> years = new YearDao().getCurrentYear(startDate);
        List<Semester> semesters = null;
        if (years.size() > 0)
            semesters = new SemesterDao().getCurrentSemesters(startDate, years.get(0));
        if (semesters != null && semesters.size() > 0)
            return new Select()
                    .from(Course.class)
                    .where("((StartDate<=? OR StartDate<=?)AND(EndDate>=? OR EndDate>=?))AND semester=?",
                            startDate, endDate,
                            startDate, endDate, semesters.get(0).getId())
                    .orderBy("StartDate ASC")
                    .execute();
        else
            return new Select()
                    .from(Course.class)
                    .where("((StartDate<=? OR StartDate<=?)AND(EndDate>=? OR EndDate>=?))",
                            startDate, endDate,
                            startDate, endDate)
                    .orderBy("StartDate ASC")
                    .execute();
    }

//    public long getConflictCourses(Course course, Long id) {
//        if (id != null && id != 0)
//            return new Select()
//                    .from(Course.class)
//                    .where("((StartDate<=? OR StartDate<=?)AND(EndDate>=? OR EndDate>=?))AND semester=? AND _ID!=?",
//                            course.StartDate.getTime(), course.EndDate.getTime(),
//                            course.StartDate.getTime(), course.EndDate.getTime(), course.Semester.getId(), id)
//                    .count();
//        else
//            return new Select()
//                    .from(Course.class)
//                    .where("((StartDate<=? OR StartDate<=?)AND(EndDate>=? OR EndDate>=?))AND semester=?",
//                            course.StartDate.getTime(), course.EndDate.getTime(),
//                            course.StartDate.getTime(), course.EndDate.getTime(), course.Semester.getId())
//                    .count();
//    }
}
