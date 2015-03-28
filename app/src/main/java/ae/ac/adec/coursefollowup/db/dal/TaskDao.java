package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.Login_Activity;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Holiday;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;

/**
 * Created by JMA on 3/21/2015.
 */
public class TaskDao extends BaseDao {
    public Task getById(long id) {
        return Task.load(Task.class, id);
    }

    public void Edit(long ID, String name, long dueDate, long dateAdded, String title, String details, int taskType, int progress,
                     Course course) throws BusinessRoleError {
        AppLog.i("Edit => " + ID);
        //ToDo Jaffer Delete title column ?! and dateAdded
        AddEdit(ID, name, dueDate, dateAdded, title, details, taskType, progress, course);
    }

    public void Add(String name, long dueDate, long dateAdded, String title, String details, int taskType, int progress,
                    Course course) throws BusinessRoleError {
        AddEdit(null, name, dueDate, dateAdded, title, details, taskType, progress, course);
    }

    private void AddEdit(Long ID, String name, long dueDate, long dateAdded, String title, String details, int taskType, int progress,
                         Course course) throws BusinessRoleError {
        Task task = null;
        if (ID != null && ID != 0)
            task = Task.load(Task.class, ID.longValue());
        else
            task = new Task();

        Calendar dD = Calendar.getInstance();
        dD.setTimeInMillis(dueDate);
        task.DueDate = dD.getTime();

        Calendar dA = Calendar.getInstance();
        dA.setTimeInMillis(dateAdded);
        task.DateAdded = dA.getTime();
        task.Name = name;
        task.Title = title;
        task.Detail = details;
        task.TaskType = taskType;
        task.Progress = progress;
        task.Course = course;


        // BR_TSK_002
        long cCount;
        if (ID != null && ID != 0)
            cCount = new Select().from(Task.class).where("Name=? AND _ID!=?", task.Name, task.getId()).count();
        else
            cCount = new Select().from(Task.class).where("Name=?", task.Name).count();
        if (cCount > 0)
            throw new BusinessRoleError(R.string.BR_TSK_002);

        long result = task.save();
        AppLog.i("Result: row " + name + " added, result id >" + result);
    }

    public void delete(long Id) throws BusinessRoleError {

        Task task = Task.load(Task.class, Id);

        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(task);
            task.delete();
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public List<Task> getAll(int position) {
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        if (position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(Task.class)
                    .where("DueDate > ?", currentTimeInMillis)
                    .orderBy("DueDate ASC")
                    .execute();
        } else if (position == ConstantVariable.TimeFrame.Past.id) {
            return new Select()
                    .from(Task.class)
                    .where("DueDate <= ?", currentTimeInMillis)
                    .orderBy("DueDate ASC")
                    .execute();
        } else {
            return new Select()
                    .from(Task.class)
                    .orderBy("DueDate ASC")
                    .execute();
        }
    }

    public List<Task> getTasksWithinCourse(Course course) {
        return new Select()
                .from(Task.class)
                .where("Course=?", course.getId())
                .execute();
    }
}
