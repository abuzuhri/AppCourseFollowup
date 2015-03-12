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
    
    @Column
    public String Code;
    
    @Column
    public String Room;
    
    @Column
    public String Building;
    
    @Column
    public String Teacher;

    @Column(name = "colorType_Id", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public ColorType colorType;

    @Column
    public Date StartDate;
    
    @Column
    public Date EndDate;

    @Column
    public int IsNotify;

    @Column
    public int RepeatId;

    @Column
    public String Days;

}
