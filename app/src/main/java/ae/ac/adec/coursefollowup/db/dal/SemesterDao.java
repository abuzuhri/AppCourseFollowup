package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.Application.myApplication;
import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;

/**
 * Created by JMA on 3/21/2015.
 */
public class SemesterDao extends BaseDao {
    public Semester getById(long id) {
        return Semester.load(Semester.class, id);
    }

    public void Edit(long ID, String Name, long startDate, long endDate, Year year) throws BusinessRoleError {
        AppLog.i("Edit => " + ID);
        AddEdit(ID, Name, startDate, endDate, year);
    }

    public void Add(String Name, long startDate, long endDate, Year year) throws BusinessRoleError {
        AddEdit(null, Name, startDate, endDate, year);
    }

    private void AddEdit(Long ID, String Name, long startDate, long endDate, Year year) throws BusinessRoleError {
        Semester semester = null;
        if (ID != null && ID != 0)
            semester = Semester.load(Semester.class, ID.longValue());
        else semester = new Semester();

        semester.Name = Name;

        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDate);
        semester.StartDate = startDateCalendar.getTime();

        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.setTimeInMillis(endDate);
        semester.EndDate = endDateCalendar.getTime();

        semester.year = year;

        // BR BR_SMR_004
        if (endDate < startDate)
            throw new BusinessRoleError(R.string.BR_SMR_004);

        // BR BR_SMR_002
        if ((startDate < year.StartDate.getTime()) || (endDate > year.EndDate.getTime()))
            throw new BusinessRoleError(R.string.BR_SMR_002);

        // BR BR_SMR_008
        if (getConflictSemesters(semester, ID) > 0)
            throw new BusinessRoleError(R.string.BR_SMR_008);

        // BR BR_SMR_001
        long cCount;
        if (ID != null && ID != 0)
            cCount = new Select().from(Semester.class).where("Name=? AND _ID!=?", semester.Name, semester.getId()).count();
        else
            cCount = new Select().from(Semester.class).where("Name=?", semester.Name).count();
        if (cCount > 0)
            throw new BusinessRoleError(R.string.BR_HLD_003);

        AppLog.i("Name =>" + Name + " startDate=>" + semester.StartDate.toString() + " endDate=>" + semester.EndDate.toString());
        Long id = semester.save();
        AppLog.i("Saved id= " + id);
    }

    public void delete(long Id) throws BusinessRoleError {

        Semester semester = Semester.load(Semester.class, Id);
        List<Course> cList = new CourseDao().getCoursesWithinSemester(semester);
        if (cList != null && cList.size() > 0)
            throw new BusinessRoleError(R.string.BR_SMR_003);
        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(semester);
            semester.delete();
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception ex) {
            throw new BusinessRoleError(R.string.BR_GENERAL_001);
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public List<Semester> getAll(int position) {

        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        if (position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(Semester.class)
                    .where("EndDate > ?", currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        } else if (position == ConstantVariable.TimeFrame.Past.id) {
            return new Select()
                    .from(Semester.class)
                    .where("EndDate <= ?", currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        } else {
            return new Select()
                    .from(Semester.class)
                    .orderBy("StartDate ASC")
                    .execute();
        }
    }

    public List<Semester> getSemestersWithinAYear(Year year) {
        return new Select()
                .from(Semester.class)
                .where("Year_Id=?", year.getId())
                .execute();
    }

    public long getConflictSemesters(Semester semester, Long id) {
        if (id != null && id != 0)
            return new Select()
                    .from(Semester.class)
                    .where("((StartDate<=? OR StartDate<=?)AND(EndDate>=? OR EndDate>=?))AND Year_Id=? AND _ID!=?",
                            semester.StartDate.getTime(), semester.EndDate.getTime(),
                            semester.StartDate.getTime(), semester.EndDate.getTime(), semester.year.getId(), id)
                    .count();
        else
            return new Select()
                    .from(Semester.class)
                    .where("((StartDate<=? OR StartDate<=?)AND(EndDate>=? OR EndDate>=?))AND Year_Id=?",
                            semester.StartDate.getTime(), semester.EndDate.getTime(),
                            semester.StartDate.getTime(), semester.EndDate.getTime(), semester.year.getId())
                    .count();
    }

    public List<Semester> getCurrentSemesters(long currentTime, Year year) {
        return new Select()
                .from(Semester.class)
                .where("((StartDate<=?)AND(EndDate>=?))AND Year_Id=?",
                        currentTime, currentTime, year.getId())
                .execute();
    }
}
