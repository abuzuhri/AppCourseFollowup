package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "Tasks", id = "_ID")
public class Task extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public Task() {
        super();
    }

    @Column(name = "Name")
    public String Name;

    @Column
    public Date DueDate;

    @Column
    public Date DateAdded;

    @Column
    public String Title;

    @Column
    public String Detail;

    @Column
    public int TaskType;

    @Column
    public int Progress;

    @Column(name = "Course", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.NO_ACTION)
    public Course Course;

    @Column
    public Boolean IsNotify;

    @Column
    public int Sync_status_typeID;


}
