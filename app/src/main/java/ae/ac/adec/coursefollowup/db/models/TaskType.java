package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "TaskTypes", id = "_ID")
public class TaskType extends  BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public TaskType(){
        super();
    }



    @Column(name = "Name")
    public String Name;

}
