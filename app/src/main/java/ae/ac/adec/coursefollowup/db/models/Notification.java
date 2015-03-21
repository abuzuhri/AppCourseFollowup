package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by JMA on 3/21/2015.
 */

@Table(name = "Notification", id = "_ID")
public class Notification extends BaseModel {
    public Notification() {
        super();
    }

    @Column
    public Date CalenderDateTime;

    @Column
    public Date NotificationDateTime;

    @Column(name = "Course", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.NO_ACTION)
    public Course Course;

    @Column(name = "Task", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.NO_ACTION)
    public Course Task;

    @Column(name = "Exam", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.NO_ACTION)
    public Course Exam;

    @Column
    public Boolean IsHoliday;

    @Column
    public Boolean isDone;

    @Column
    public Boolean IsNoNeedNothing;

    @Column
    public int Remote_Id;

    @Column
    public int Sync_status_typeID;

}
