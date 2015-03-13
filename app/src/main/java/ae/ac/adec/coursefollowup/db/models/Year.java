package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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

    // Used to return items from another table based on the foreign key
    public List<Semester> Semesters() {
        return getMany(Semester.class, "Year_Id");
    }


    /*
    // Finds existing user based on remoteId or creates new user and returns
    public static Year findOrCreateFromJson(JSONObject json) {
        Year user = Year.fromJSON(json);
        Year existingUser =
                new Select().from(Year.class).where("remote_id = ?", this.remoteId).executeSingle();
        if (existingUser != null) {
            // found and return existing
            return existingUser;
        } else {
            // create and return new
            Year user = Year.fromJSON(json);
            user.save();
            return user;
        }
    }
    */
}
