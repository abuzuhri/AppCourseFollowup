package ae.ac.adec.coursefollowup.Application;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by Tareq on 03/03/2015.
 */
public class myApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
