package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.query.Select;

import java.util.List;

import ae.ac.adec.coursefollowup.db.models.Year;

/**
 * Created by Tareq on 03/04/2015.
 */
public class YearDao extends BaseDao {

    public  Year  getById(long Id){
        return Year.load(Year.class,Id);
    }

    public  void  add(String Name){
        Year year=new Year();
        year.Name=Name;
        year.save();
    }

    public  void  delete(long Id){
        Year year=Year.load(Year.class,Id);
        year.delete();
    }

    public List<Year> getAll(){
        return new Select()
                .from(Year.class)
                .orderBy("Name ASC")
                .execute();
    }

}
