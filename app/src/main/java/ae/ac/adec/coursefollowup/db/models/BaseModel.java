package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by Tareq on 03/02/2015.
 */
public class BaseModel extends Model {

    // This is the unique id given by the server
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;
}
