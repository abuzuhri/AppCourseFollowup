package ae.ac.adec.coursefollowup.services.auths;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ae.ac.adec.coursefollowup.R;

/**
 * Created by Tareq on 03/03/2015.
 */
public class AppFacebookAuth {


    public AppFacebookAuth(final Activity c){
        // list of permissions
        List<String> permissions = new ArrayList<String>();
        permissions.add("email");
        // open a new session if there is no session
        openFacebookSession(c,permissions);

    }

    private void openFacebookSession(Activity activity,List<String> perms){
        if(Session.getActiveSession()!=null && Session.getActiveSession().isOpened())
            return;
        Session.openActiveSession(activity, true,perms, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (exception != null) {
                    Log.d("Facebook", exception.getMessage());
                }
                Log.d("Facebook", "Session State: " + session.getState());

                // you can make request to the /me API or do other stuff like post, etc. here
                Request.executeMeRequestAsync(session,new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser graphUser, Response response) {
                        if (graphUser != null) {
                            try {
                                String userID =graphUser.getId();
                                String userName=graphUser.getName();
                                String email =graphUser.getProperty("email").toString();
                                // here you can store user data on the server and application, so he does not
                                // need to login after first login.
                                Log.d("****User ID****", userID);
                                Log.d("****User name****", userName);
                                Log.d("****User email****", email);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            }
                    }
                });
            }
        });
    }

}
