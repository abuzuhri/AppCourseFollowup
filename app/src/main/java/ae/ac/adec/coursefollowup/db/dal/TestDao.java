package ae.ac.adec.coursefollowup.db.dal;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.BaseDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;

/**
 * Created by JMA on 3/20/2015.
 */
public class TestDao extends BaseDao {
    public void addCourse(String name, String code, String room, String building, String teacher,
                          String cCode, Semester semester, long startDate, long endDate, Boolean isNotify, int remoteID,
                          int syncStatusID) throws BusinessRoleError {
        Course course = new Course();
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTimeInMillis(startDate);
        course.StartDate = startDateCalendar.getTime();

        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.setTimeInMillis(endDate);
        course.EndDate = endDateCalendar.getTime();

        if (endDate < startDate)
            throw new BusinessRoleError(R.string.BR_YER_001);

        course.Name = name;
        course.Code = code;
        course.Room = room;
        course.Building = building;
        course.Teacher = teacher;
        course.ColorCode = cCode;
        course.Semester = semester;
        course.IsNotify = isNotify;
        course.Sync_Status_Id = syncStatusID;
        long result = course.save();
        Log.d("JMA", result + "");
    }

    public void addDateTime(Date startTime, Date endTime, Boolean isRepeat, Course course, int dayOfWeek, int remoteID,
                            int syncStatusID) throws BusinessRoleError {
        CourseTimeDay ctd = new CourseTimeDay();

        ctd.Start_time = startTime;
        ctd.End_time = endTime;
        ctd.IsRepeat = isRepeat;
        ctd.Course = course;
        ctd.DayOfWeek = dayOfWeek;
        ctd.Remote_Id = remoteID;
        ctd.Sync_status_typeID = syncStatusID;

        long result = ctd.save();
        Log.d("JMA_CourseTimeDay", result + "");
    }

    public void addTask(String name, Date dueDate, Date DateAdded, String title, String details, int taskType, int progress,
                        Course course, int remoteID,
                        int syncStatusID) throws BusinessRoleError {
        Task task = new Task();

        task.Name = name;
        task.Title = title;
        task.Detail = details;
        task.Course = course;
        task.DateAdded = DateAdded;
        task.DueDate = dueDate;
        task.Progress = progress;
        task.Remote_Id = remoteID;
        task.Sync_status_typeID = syncStatusID;
        task.TaskType = taskType;

        long result = task.save();
        Log.d("JMA_Task", result + "");
    }

    public void addNote(int noteType, String filePath, String details,
                        Course course, int remoteID,Date dateAdded,
                        int syncStatusID) throws BusinessRoleError {
        Note note = new Note();

        note.Course=course;
        note.DateAdded=dateAdded;
        note.NoteType=noteType;
        note.FilePath=filePath;
        note.Details=details;
        note.Remote_Id=remoteID;
        note.Sync_status_typeID=syncStatusID;

        long result = note.save();
        Log.d("JMA_Note", result + "");
    }

    public void addExam(Date startDateTime, Date endDateTime, Date dateAdded,
                        Boolean isResit,String seat,String room,
                        Course course, int remoteID,
                        int syncStatusID) throws BusinessRoleError {
        Exam exam = new Exam();

        exam.StartDateTime=startDateTime;
        exam.EndDateTime=endDateTime;
        exam.DateAdded=dateAdded;
        exam.IsResit=isResit;
        exam.Seat=seat;
        exam.Room=room;
        exam.Course=course;
        exam.Remote_Id=remoteID;
        exam.Sync_status_typeID=syncStatusID;

        long result = exam.save();
        Log.d("JMA_Exam", result + "");
    }

    public void addSemester(String name,Date startDate, Date endDate, Year year, int remoteID,
                            int syncStatusID) throws BusinessRoleError {
        Semester semester = new Semester();

        semester.Name=name;
        semester.StartDate=startDate;
        semester.EndDate=endDate;
        semester.year = year;
        semester.Remote_Id=remoteID;
        semester.Sync_status_typeID=syncStatusID;

        long result = semester.save();
        Log.d("JMA_Semester", result + "");
    }


}
