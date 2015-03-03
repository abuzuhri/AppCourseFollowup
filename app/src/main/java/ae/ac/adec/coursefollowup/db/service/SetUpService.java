package ae.ac.adec.coursefollowup.db.service;

import android.text.format.Time;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

import ae.ac.adec.coursefollowup.Utils.Constants;
import ae.ac.adec.coursefollowup.db.models.CourseTime;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;

/**
 * Created by Tareq on 03/02/2015.
 */
public class SetUpService extends  BaseService  {

    public static List<CourseTime> getAllCourse(Constants.TimeFrame timeFrame) {
        // get all today course
        return new Select()
                .from(CourseTime.class)
                .where("Category = ?", category.getId())
                .orderBy("Name ASC")
                .execute();

        From query = new Select()
                .from(CourseTime.class)
                .innerJoin(CourseTimeDay.class)
                .on("CourseTimes.id=CourseTimeDaies.CourseTime_Id");

        if(timeFrame == Constants.TimeFrame.Current)
        {
            query=query.where("CourseTimes.StartDate > ? and CourseTimes.EndDate < ?", Constants.getDate().getTime(),Constants.getDate().getTime());
        }
        else if(timeFrame == Constants.TimeFrame.Current)
        {

        }
        else if(timeFrame == Constants.TimeFrame.Past){

        }

        return query.execute();

    }


}
