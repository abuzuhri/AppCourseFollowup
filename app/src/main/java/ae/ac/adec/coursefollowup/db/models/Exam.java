package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "Semesters")
public class Exam extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public Exam(){
        super();
    }

    @Column
    public Date Date;

    @Column
    public  Boolean IsResit;

    @Column
    public int Duration;

    @Column
    public String Seat;

    @Column
    public String Room;

    @Column(name = "Course_Id", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Course course;
}
