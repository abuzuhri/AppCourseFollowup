package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;
import java.util.List;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "Years", id = "_ID")
public class Year extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public Year(){
        super();
    }

    @Column(name = "Name")
    public String Name;

    @Column
    public Date StartDate;

    @Column
    public Date EndDate;

    // Used to return items from another table based on the foreign key
    public List<Semester> Semesters() {
        return getMany(Semester.class, "Year_Id");
    }


}
