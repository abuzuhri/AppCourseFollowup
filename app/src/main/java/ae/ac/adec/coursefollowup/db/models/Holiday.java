package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "Holidaies")
public class Holiday extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public Holiday(){
        super();
    }

    @Column(name = "Name")
    public String Name;

    public Date StartDate;

    public Date EndDate;
}
