package ae.ac.adec.coursefollowup.db.models;

/**
 * Created by Tareq on 03/03/2015.
 */

import com.activeandroid.annotation.Table;


@Table(name = "Notes", id = "_ID")
public class Note extends BaseModel  {

    // Must have a default constructor for every ActiveAndroid model

    public Note(){
        super();
    }

}
