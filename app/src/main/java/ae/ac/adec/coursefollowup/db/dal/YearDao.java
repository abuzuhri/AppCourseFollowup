package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.Application.myApplication;
import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;

/**
 * Created by Tareq on 03/04/2015.
 */
public class YearDao extends BaseDao {
    public Year getById(long Id){
        return Year.load(Year.class, Id);
    }

    public  void  Edit(long ID,String Name,long startDate,long endDate)  throws BusinessRoleError {
        AppLog.i("Edit => " + ID);
        AddEdit(ID, Name, startDate, endDate);
    }
    public  void  Add(String Name,long startDate,long endDate)   throws BusinessRoleError{
        AddEdit(null, Name, startDate, endDate);
    }
    private  void  AddEdit(Long ID,String Name,long startDate,long endDate) throws BusinessRoleError {
        Year year=null;
        if(ID!=null && ID!=0)
            year=Year.load(Year.class,ID.longValue());
        else year=new Year();

        year.Name=Name;

        Calendar startDateCalendar=Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDate);
        year.StartDate= startDateCalendar.getTime();

        Calendar endDateCalendar=Calendar.getInstance();
        endDateCalendar.setTimeInMillis(endDate);
        year.EndDate= endDateCalendar.getTime();

        // BR BR_YER_001
        if(endDate < startDate)
            throw new BusinessRoleError(R.string.BR_YER_001);

        // BR BR_AUH_002
        int countExist=new Select().from(Year.class).where("Name = ?", year.Name).count();
        if(countExist>0)
            throw new BusinessRoleError(R.string.BR_YER_002);

        // BR BR_YER_003
        long diff = endDate - startDate;
        long numOfDays = diff/(1000*60*60*24);
        int MaxYearPeriod=  myApplication.getContext().getResources().getInteger(R.integer.MaxYearPeriod);
        if(numOfDays >  MaxYearPeriod)
            throw new BusinessRoleError(R.string.BR_YER_003);

        AppLog.i("Name =>" + Name + " startDate=>" + year.StartDate.toString() + " endDate=>" + year.EndDate.toString());
        Long id=  year.save();
        AppLog.i("Saved id= "+ id);
    }

    public  void  delete(long Id) throws BusinessRoleError {

        Year year=Year.load(Year.class,Id);

        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(year);
            year.delete();
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }

    }

    public List<Year> getAll(int position){

        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis= calendar.getTimeInMillis();
        if(position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(Year.class)
                    .where("EndDate > ?",currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        }else if(position == ConstantVariable.TimeFrame.Past.id) {
            return new Select()
                    .from(Year.class)
                    .where("EndDate <= ?",currentTimeInMillis)
                    .orderBy("StartDate ASC")
                    .execute();
        }else {
            return new Select()
                    .from(Year.class)
                    .orderBy("StartDate ASC")
                    .execute();
        }
    }
}
