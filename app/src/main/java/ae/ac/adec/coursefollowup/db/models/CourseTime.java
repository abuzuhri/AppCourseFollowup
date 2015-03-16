package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Table;

import java.util.Date;
import java.util.List;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "CourseTimes", id = "_ID")
public class CourseTime extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public CourseTime(){
        super();
    }

    public Date StartDate;

    public Date EndDate;

    // Used to return items from another table based on the foreign key
    public List<CourseTimeDay> Daies() {
        return getMany(CourseTimeDay.class, "CourseTime_Id");
    }

}
