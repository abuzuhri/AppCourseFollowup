package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Tareq on 03/03/2015.
 */
@Table(name = "CourseTimeDaies")
public class CourseTimeDay extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public CourseTimeDay(){
        super();
    }

    @Column
    public int DayOfWeek ;

    @Column(name = "CourseTime_Id", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public CourseTime courseTime;


}
