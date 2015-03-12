package ae.ac.adec.coursefollowup.db.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Tareq on 03/03/2015.
 */

@Table(name = "ColorTypes")
public class ColorType extends BaseModel {

    @Column(name = "Name")
    public String Name;

    @Column
    public String Code;
}
