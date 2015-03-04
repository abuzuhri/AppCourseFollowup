package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.query.Select;

import java.util.List;

import ae.ac.adec.coursefollowup.db.models.Holiday;

/**
 * Created by Tareq on 03/04/2015.
 */
public class HolidayDao extends BaseDao {
    public Holiday getById(long Id){
        return Holiday.load(Holiday.class, Id);
    }

    public  void  add(String Name){
        Holiday holiday=new Holiday();
        holiday.Name=Name;
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
