package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "Tasks")
public class Task extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public Task(){
        super();
    }


    @Column(name = "TaskType_Id", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public TaskType taskType;





}
