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
import ae.ac.adec.coursefollowup.services.BusinessRoleError;

/**
 * Created by Tareq on 03/04/2015.
 */
public class HolidayDao extends BaseDao {

    public Holiday getById(long Id) {
        return Holiday.load(Holiday.class, Id);
    }

    public void Edit(long ID, String Name, long startDate, long endDate) throws BusinessRoleError {
        AppLog.i("Edit => " + ID);
        AddEdit(ID, Name, startDate, endDate);
    }

    public void Add(String Name, long startDate, long endDate) throws BusinessRoleError {
        AddEdit(null, Name, startDate, endDate);
    }

    private void AddEdit(Long ID, String Name, long startDate, long endDate) throws BusinessRoleError {
        Holiday holiday = null;
        if (ID != null && ID != 0)
            holiday = Holiday.load(Holiday.class, ID.longValue());
        else holiday = new Holiday();

        holiday.Name = Name;

        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDate);
        holiday.StartDate = startDateCalendar.getTime();

        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.setTimeInMillis(endDate);
        holiday.EndDate = endDateCalendar.getTime();

        // BR BR_AUH_001
        if (endDate < startDate)
            throw new BusinessRoleError(R.string.BR_HLD_001);

        // BR BR_AUH_002
        long diff = endDate - startDate;
        long numOfDays = diff / (1000 * 60 * 60 * 24);
        int max_holiday_days = myApplication.getContext().getResources().getInteger(R.integer.MaxHoildayPeriod);
        if (numOfDays > max_holiday_days)
            throw new BusinessRoleError(R.string.BR_HLD_002);

        // BR_HLD_012
        if (getConflictHolidays(holiday, ID) > 0)
            throw new BusinessRoleError(R.string.BR_HLD_006);

        // BR BR_AUH_003
        long cCount;
        if (ID != null && ID != 0)
            cCount = new Select().from(Holiday.class).where("Name=? AND _ID!=?", holiday.Name, holiday.getId()).count();
        else
            cCount = new Select().from(Holiday.class).where("Name=?", holiday.Name).count();
        if (cCount > 0)
            throw new BusinessRoleError(R.string.BR_HLD_003);

        AppLog.i("Name =>" + Name + " startDate=>" + holiday.StartDate.toString() + " endDate=>" + holiday.EndDate.toString());
        Long id = holiday.save();
        AppLog.i("Saved id= " + id);
    }

    public void delete(long Id) throws BusinessRoleError {

        Holiday holiday = Holiday.load(Holiday.class, Id);

        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(holiday);
            holiday.delete();
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public List<Holiday> getAll(int position) {

        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        if (position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(Holiday.class)
                    .where("EndDate > ?", currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        } else if (position == ConstantVariable.TimeFrame.Past.id) {
            return new Select()
                    .from(Holiday.class)
                    .where("EndDate <= ?", currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        } else {
            return new Select()
                    .from(Holiday.class)
                    .orderBy("StartDate ASC")
                    .execute();
        }
    }

    public long getConflictHolidays(Holiday holiday, Long id) {
        if (id != null && id != 0)
            return new Select()
                    .from(Holiday.class)
                    .where("((StartDate<=? OR StartDate<=?)AND(EndDate>=? OR EndDate>=?)) AND _ID!=?",
                            holiday.StartDate.getTime(), holiday.EndDate.getTime(),
                            holiday.StartDate.getTime(), holiday.EndDate.getTime(), id)
                    .count();
        else
            return new Select()
                    .from(Holiday.class)
                    .where("((StartDate<=? OR StartDate<=?)AND(EndDate>=? OR EndDate>=?))",
                            holiday.StartDate.getTime(), holiday.EndDate.getTime(),
                            holiday.StartDate.getTime(), holiday.EndDate.getTime())
                    .count();
    }

    public List<Holiday> getHolidaysOnDate(long startDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(startDate);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);

        Calendar endCal = (Calendar) startCal.clone();
        endCal.add(Calendar.DATE, 1);
        return new Select()
                .from(Holiday.class)
                .where("StartDate>=? AND StartDate<?",
                        startCal.getTime().getTime(), endCal.getTime().getTime())
                .execute();
    }
}
