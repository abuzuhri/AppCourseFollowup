package ae.ac.adec.coursefollowup.Application;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

/**
 * Created by Tareq on 03/03/2015.
 */
public class myApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initializeDB();
    }


    protected void initializeDB() {
        Configuration.Builder configurationBuilder = new Configuration.Builder(this);
        //configurationBuilder.addModelClasses(Test.class);

        ActiveAndroid.initialize(configurationBuilder.create());
    }

}
