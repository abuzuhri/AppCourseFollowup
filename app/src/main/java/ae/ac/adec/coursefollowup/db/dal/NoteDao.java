package ae.ac.adec.coursefollowup.db.dal;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.Exam;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;

/**
 * Created by JMA on 3/21/2015.
 */
public class NoteDao extends BaseDao {
    public Note getById(long id) {
        return Note.load(Note.class, id);
    }

    public void Edit(long ID, Course course, int noteType, String details, String filePath, long dateAdded) throws BusinessRoleError {
        AppLog.i("Edit => " + ID);
        AddEdit(ID, course, noteType, details, filePath, dateAdded);
    }

    public void Add(Course course, int noteType, String details, String filePath, long dateAdded) throws BusinessRoleError {
        AddEdit(null, course, noteType, details, filePath, dateAdded);
    }

    private void AddEdit(Long ID, Course course, int noteType, String details, String filePath, long dateAdded) throws BusinessRoleError {
        Note note = null;
        if (ID != null && ID != 0)
            note = Note.load(Note.class, ID.longValue());
        else
            note = new Note();

        Calendar dA = Calendar.getInstance();
        dA.setTimeInMillis(dateAdded);
        note.DateAdded = dA.getTime();

        note.Course=course;
        note.NoteType=noteType;
        note.Details=details;
        note.FilePath=filePath;

        /*int countC = new Select().from(Course.class).where("Name=?", course.Name).count();
        if (countC > 0)
            throw new BusinessRoleError(R.string.BR_HLD_003);*/

        long result = note.save();
        AppLog.i("Result: row " + result + " added, result id >" + result);
    }

    public void delete(long Id) throws BusinessRoleError {

        Note note = Note.load(Note.class, Id);

        ActiveAndroid.beginTransaction();
        try {
            DeleteSyncer(note);
            note.delete();
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

    }

    public List<Note> getAll(int position) {
        Calendar calendar = Calendar.getInstance();
        long currentTimeInMillis = calendar.getTimeInMillis();
        if (position == ConstantVariable.TimeFrame.Current.id) {
            return new Select()
                    .from(Note.class)
                    .where("DateAdded > ?", currentTimeInMillis)
                    .orderBy("DateAdded ASC")
                    .execute();
        } else if (position == ConstantVariable.TimeFrame.Past.id) {
            return new Select()
                    .from(Note.class)
                    .where("DateAdded <= ?", currentTimeInMillis)
                    .orderBy("DateAdded ASC")
                    .execute();
        } else {
            return new Select()
                    .from(Note.class)
                    .orderBy("DateAdded ASC")
                    .execute();
        }
    }
    public List<Note> getNotesWithinCourse(Course course) {
        return new Select()
                .from(Note.class)
                .where("Course=?", course.getId())
                .execute();
    }
}
