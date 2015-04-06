package ae.ac.adec.coursefollowup.db.models;

/**
 * Created by Tareq on 03/03/2015.
 */

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;


@Table(name = "Notes", id = "_ID")
public class Note extends BaseModel {

    // Must have a default constructor for every ActiveAndroid model

    public Note() {
        super();
    }

    @Column(name = "Course", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.NO_ACTION)
    public Course Course;
    @Column
    public int NoteType;
    @Column
    public String Details;
    @Column
    public String FilePath;
    @Column
    public Date DateAdded;
    @Column
    public int Sync_status_typeID;

}
