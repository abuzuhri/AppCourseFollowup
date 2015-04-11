package ae.ac.adec.coursefollowup.db.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Tareq on 03/02/2015.
 */
public class BaseService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BaseService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }
}
