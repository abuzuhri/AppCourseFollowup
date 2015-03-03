package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "Years")
public class Year extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public Year(){
        super();
    }

    @Column(name = "Name")
    public String Name;

    // Used to return items from another table based on the foreign key
    public List<Semester> Semesters() {
        return getMany(Semester.class, "Semester");
    }

}
