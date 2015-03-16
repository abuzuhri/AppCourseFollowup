package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Tareq on 03/13/2015.
 */

@Table(name = "SyncDeleted", id = "_ID")
public class SyncDeleted extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model
    public SyncDeleted(){
        super();
    }

    @Column(name = "ObjectName")
    public String ObjectName;

    @Column(name = "ObjectID")
    public long ObjectID;

}
