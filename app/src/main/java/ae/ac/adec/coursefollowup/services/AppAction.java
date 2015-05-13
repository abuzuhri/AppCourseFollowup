package ae.ac.adec.coursefollowup.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by Tareq on 03/13/2015.
 */
public class AppAction {

    public static final String IDEXTRA = "IDEXTRA";
    public static final String FRAGMENTEXTRA = "FRAGMENT";
    public static final String COURSE_ID = "COURSE_ID";
    public static final String NOTETYPE = "NOTETYPE";

    public static void OpenActivityIntent(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static void OpenActivity(Context context, Class<?> cls) {

    }

    public static void OpenActivityWithID(Context context, Class<?> cls, Long ID) {
        Intent intent = new Intent(context, cls);
        if (ID != null)
            intent.putExtra(IDEXTRA, ID);
        OpenActivityIntent(context, intent);
    }

    public static void OpenActivityWithFRAGMENT(Context context, Class<?> cls, String name, long id) {
        Intent intent = new Intent(context, cls);
        if (name != null)
            intent.putExtra(FRAGMENTEXTRA, name);
        if (id != -1)
            intent.putExtra(COURSE_ID, id);
        OpenActivityIntent(context, intent);
    }

    public static void OpenActivityWithFRAGMENT(Context context, Class<?> cls, String name, long id, int noteType) {
        Intent intent = new Intent(context, cls);
        if (name != null)
            intent.putExtra(FRAGMENTEXTRA, name);
        if (id != -1)
            intent.putExtra(COURSE_ID, id);
        intent.putExtra(NOTETYPE, noteType);
        OpenActivityIntent(context, intent);
    }

    public static void OpenActivityWithFRAGMENT(Context context, String name, Long ID, Long course_id) {
        Intent intent = new Intent(context, OneFragmentActivity.class);
        if (name != null)
            intent.putExtra(FRAGMENTEXTRA, name);
        if (ID != null)
            intent.putExtra(IDEXTRA, ID);
        if (course_id != -1)
            intent.putExtra(COURSE_ID, course_id);

        OpenActivityIntent(context, intent);
    }

    public static void OpenActivityWithFRAGMENT(Context context, String name, Long ID) {
        Intent intent = new Intent(context, OneFragmentActivity.class);
        if (name != null)
            intent.putExtra(FRAGMENTEXTRA, name);
        if (ID != null)
            intent.putExtra(IDEXTRA, ID);

        OpenActivityIntent(context, intent);
    }

    public static void DiaplayError(Activity activity, String msg) {
        Crouton.makeText(activity, msg, Style.ALERT).show();
    }
}
