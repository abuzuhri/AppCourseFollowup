package ae.ac.adec.coursefollowup.db.dal;

import android.util.Log;

import ae.ac.adec.coursefollowup.db.models.BaseModel;
import ae.ac.adec.coursefollowup.db.models.SyncDeleted;

/**
 * Created by Tareq on 03/04/2015.
 */
public class BaseDao  {


    public void DeleteSyncer(BaseModel obj){
        if(obj.RemoteID!=null && obj.RemoteID!=0)
        {
            SyncDeleted sync=new SyncDeleted();
            sync.ObjectID=obj.RemoteID;
            sync.ObjectName=obj.getClass().getName();
            Log.i("tg","Delete from "+sync.ObjectName +" id=>"+sync.ObjectID);
            sync.save();
        }
    }
}
