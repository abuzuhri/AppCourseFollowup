package ae.ac.adec.coursefollowup.db.dal;

import android.util.Log;

import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.services.BusinessRoleExcption;

/**
 * Created by Tareq on 03/04/2015.
 */
public class HolidayDao extends BaseDao {
    public Holiday getById(long Id){
        return Holiday.load(Holiday.class, Id);
    }

    public  void  add(String Name,long startDate,long endDate) throws BusinessRoleExcption {
        Holiday holiday=new Holiday();
        holiday.Name=Name;

        Calendar startDateCalendar=Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDate);
        holiday.StartDate= startDateCalendar.getTime();

        Calendar endDateCalendar=Calendar.getInstance();
        endDateCalendar.setTimeInMillis(endDate);
        holiday.EndDate= endDateCalendar.getTime();

        if(endDate < startDate)
            throw new BusinessRoleExcption(R.string.BR_AUH_001);

        Log.i("tg","Name =>"+Name+" startDate=>"+holiday.StartDate.toString()+" endDate=>"+holiday.EndDate.toString());
        holiday.save();
    }

    public  void  delete(long Id){
        Holiday holiday=Holiday.load(Holiday.class,Id);
        holiday.delete();
    }

    public List<Holiday> getAll(int position){

        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis= calendar.getTimeInMillis();
        if(position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(Holiday.class)
                    .where("EndDate > ?",currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        }else {
            return new Select()
                    .from(Holiday.class)
                    .where("EndDate <= ?",currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        }
    }
}
