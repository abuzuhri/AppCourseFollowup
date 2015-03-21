package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "Courses", id = "_ID")
public class Course extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public Course() {
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

    @Column
    public String ColorCode;

    @Column(name = "semester", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.NO_ACTION)
    public Semester Semester;

    @Column
    public Date StartDate;

    @Column
    public Date EndDate;

    @Column
    public Boolean IsNotify;


    @Column
    public int Sync_Status_Id;
}
