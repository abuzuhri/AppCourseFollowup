package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;

/**
 * Created by JMA on 3/21/2015.
 */
public class ExamDao extends BaseDao {
    public Exam getById(long id) {
        return Exam.load(Exam.class, id);
    }

    public void Edit(long ID, long startDateTime, long endDateTime, long dateAdded, String room, String seat,
                     Boolean isResit, Course course) throws BusinessRoleError {
        AppLog.i("Edit => " + ID);
        AddEdit(ID, startDateTime, endDateTime, dateAdded, room, seat, isResit, course);
    }

    public void Add(long startDateTime, long endDateTime, long dateAdded, String room, String seat,
                    Boolean isResit, Course course) throws BusinessRoleError {
        AddEdit(null, startDateTime, endDateTime, dateAdded, room, seat, isResit, course);
    }

    private void AddEdit(Long ID, long startDateTime, long endDateTime, long dateAdded, String room, String seat,
                         Boolean isResit, Course course) throws BusinessRoleError {
        Exam exam = null;
        if (ID != null && ID != 0)
            exam = Exam.load(Exam.class, ID.longValue());
        else
            exam = new Exam();

        Calendar dA = Calendar.getInstance();
        dA.setTimeInMillis(dateAdded);
        exam.DateAdded = dA.getTime();

        Calendar startDateCal = Calendar.getInstance();
        startDateCal.setTimeInMillis(startDateTime);
        exam.StartDateTime = startDateCal.getTime();

        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTimeInMillis(endDateTime);
        exam.EndDateTime = endDateCal.getTime();

        exam.Room = room;
        exam.Seat = seat;
        exam.IsResit = isResit;
        exam.Course = course;

        if (endDateTime < startDateTime)
            throw new BusinessRoleError(R.string.BR_EXM_003);

        // BR_EXM_012
        if (getConflictExams(exam).size() > 0)
            throw new BusinessRoleError(R.string.BR_EXM_004);

        long result = exam.save();
        AppLog.i("Result: row " + result + " added, result id >" + result);
    }

    public void delete(long Id) throws BusinessRoleError {

        Exam exam = Exam.load(Exam.class, Id);

        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(exam);
            exam.delete();
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public List<Exam> getAll(int position) {
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        if (position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(Exam.class)
                    .where("EndDateTime > ?", currentTimeInMillis)
                    .orderBy("StartDateTime ASC")
                    .execute();
        } else if (position == ConstantVariable.TimeFrame.Past.id) {
            return new Select()
                    .from(Exam.class)
                    .where("EndDateTime <= ?", currentTimeInMillis)
                    .orderBy("StartDateTime ASC")
                    .execute();
        } else {
            return new Select()
                    .from(Exam.class)
                    .orderBy("StartDateTime ASC")
                    .execute();
        }
    }

    public List<Exam> getExamsWithinCourse(Course course) {
        return new Select()
                .from(Exam.class)
                .where("Course=?", course.getId())
                .execute();
    }

    public List<Exam> getConflictExams(Exam exam) {
        return new Select()
                .from(Exam.class)
                .where("((StartDateTime<=? OR StartDateTime<=?)AND(EndDateTime>=? OR EndDateTime>=?))AND Course=?",
                        exam.StartDateTime.getTime(), exam.EndDateTime.getTime(),
                        exam.StartDateTime.getTime(), exam.EndDateTime.getTime(), exam.Course.getId())
                .execute();
    }
}
