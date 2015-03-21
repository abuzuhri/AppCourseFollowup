package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Tareq on 03/03/2015.
 */
@Table(name = "CourseDaysTimes", id = "_ID")
public class CourseTimeDay extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public CourseTimeDay(){
        super();
    }

    @Column
    public Date Start_time;
    @Column
    public Date End_time;
    @Column
    public int DayOfWeek ;
    @Column
    public Boolean IsRepeat;
    @Column(name = "Course",onUpdate = Column.ForeignKeyAction.CASCADE,onDelete = Column.ForeignKeyAction.NO_ACTION)
    public Course Course;
    @Column
    public int Remote_Id;
    @Column
    public int Sync_status_typeID;

}
