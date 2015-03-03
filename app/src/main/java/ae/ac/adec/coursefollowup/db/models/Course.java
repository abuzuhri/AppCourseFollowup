package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "Courses")
public class Course extends BaseModel{

    // Must have a default constructor for every ActiveAndroid model
    public Course(){
        super();
    }

    @Column(name = "Name")
    public String Name;

    public String Code;

    public String Room;

    public String Building;

    public String Teacher;

    @Column(name = "colorType_Id", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public ColorType colorType;

    public Date StartDate;

    public Date EndDate;

    public int IsNotify;

    public int RepeatId;

    public String Days;

}
