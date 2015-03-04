package ae.ac.adec.coursefollowup.db.service;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.List;

import ae.ac.adec.coursefollowup.Utils.Constants;
import ae.ac.adec.coursefollowup.db.models.CourseTime;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Year;

/**
 * Created by Tareq on 03/02/2015.
 */
public class SetUpService extends  BaseService  {



    public static List<CourseTime> getAllCourse(Constants.TimeFrame timeFrame) {
        // get all today course

        From query = new Select()
                .from(CourseTime.class)
                .innerJoin(CourseTimeDay.class)
                .on("CourseTimes.id=CourseTimeDaies.CourseTime_Id");


        query=query.where("CourseTimes.StartDate > ? and CourseTimes.EndDate < ? and CourseTimeDaies.DayOfWeek  = ?", Constants.getCurrentDate().getTime(),Constants.getCurrentDate().getTime(), Constants.getCurrentDayOfWeek());

        /*if(timeFrame == Constants.TimeFrame.Current)
        {
            // If current day is Sunday, day=1. Saturday, day=7.
            query=query.where("CourseTimes.StartDate > ? and CourseTimes.EndDate < ? and CourseTimeDaies.DayOfWeek  = ?", Constants.getCurrentDate().getTime(),Constants.getCurrentDate().getTime(), Constants.getCurrentDayOfWeek());
        }
        else if(timeFrame == Constants.TimeFrame.Future)
        {
            query=query.where("CourseTimes.StartDate > ? and CourseTimes.EndDate < ? and CourseTimeDaies.DayOfWeek  = ?", Constants.getCurrentDate().getTime(),Constants.getCurrentDate().getTime(), Constants.getCurrentDayOfWeek());
        }
        else if(timeFrame == Constants.TimeFrame.Past){

        }*/

        return query.execute();

    }


}
