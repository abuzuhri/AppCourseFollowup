package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.Application.myApplication;
import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
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

        // BR BR_AUH_001
        if (endDate < startDate)
            throw new BusinessRoleError(R.string.BR_HLD_001);

        // BR BR_AUH_002

        /*long diff = endDate - startDate;
        long numOfDays = diff/(1000*60*60*24);
        int MaxHoildayPeriod=  myApplication.getContext().getResources().getInteger(R.integer.MaxHoildayPeriod);
        if(numOfDays >  MaxHoildayPeriod)
            throw new BusinessRoleError(R.string.BR_HLD_002);*/

        // BR BR_AUH_003
        /*int countExist = new Select().from(Semester.class).where("Name = ?", semester.Name).count();
        if (countExist > 0)
            throw new BusinessRoleError(R.string.BR_HLD_003);*/

        AppLog.i("Name =>" + Name + " startDate=>" + semester.StartDate.toString() + " endDate=>" + semester.EndDate.toString());
        Long id = semester.save();
        AppLog.i("Saved id= " + id);
    }

    public void delete(long Id) throws BusinessRoleError {

        Semester holiday = Semester.load(Semester.class, Id);

        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(holiday);
            holiday.delete();
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public List<Semester> getAll(int position){

        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis= calendar.getTimeInMillis();
        if(position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(Semester.class)
                    .where("EndDate > ?",currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        }else if(position == ConstantVariable.TimeFrame.Past.id) {
            return new Select()
                    .from(Semester.class)
                    .where("EndDate <= ?",currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        } else {
            return new Select()
                    .from(Semester.class)
                    .orderBy("StartDate ASC")
                    .execute();
        }
    }
}
