package ae.ac.adec.coursefollowup.db.dal;

import android.util.Log;

import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.db.models.Holiday;

/**
 * Created by Tareq on 03/04/2015.
 */
public class HolidayDao extends BaseDao {
    public Holiday getById(long Id){
        return Holiday.load(Holiday.class, Id);
    }

    public  void  add(String Name,long startDate,long endDate){
        Holiday holiday=new Holiday();
        holiday.Name=Name;

        Calendar startDateCalendar=Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDate);
        holiday.StartDate= startDateCalendar.getTime();

        Calendar endDateCalendar=Calendar.getInstance();
        endDateCalendar.setTimeInMillis(endDate);
        holiday.EndDate= endDateCalendar.getTime();

        Log.i("tg","Name =>"+Name+" startDate=>"+holiday.StartDate.toString()+" endDate=>"+holiday.EndDate.toString());
        holiday.save();
    }

    public  void  delete(long Id){
        Holiday holiday=Holiday.load(Holiday.class,Id);
        holiday.delete();
    }

    public List<Holiday> getAll(){
        return new Select()
                .from(Holiday.class)
                .orderBy("Name ASC")
                .execute();
    }
}
