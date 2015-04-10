package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "Exams", id = "_ID")
public class Exam extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public Exam() {
        super();
    }

    @Column
    public Date StartDateTime;
    @Column
    public Date EndDateTime;
    @Column
    public Date DateAdded;

    @Column
    public Boolean IsResit;

    @Column
    public String Seat;

    @Column
    public String Room;

    @Column(name = "Course", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.NO_ACTION)
    public Course Course;

    @Column
    public Boolean IsNotify;

    @Column
    public int Sync_status_typeID;
}
