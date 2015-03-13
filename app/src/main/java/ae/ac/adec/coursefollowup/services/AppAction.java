package ae.ac.adec.coursefollowup.services;

import android.content.Context;
import android.content.Intent;

import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;

/**
 * Created by Tareq on 03/13/2015.
 */
public class AppAction {

    public  static final  String  IDEXTRA="IDEXTRA";
    public  static final  String  FRAGMENTEXTRA="FRAGMENT";
    public  static void OpenActivityIntent(Context context,Intent intent){
        context.startActivity(intent);
    }

    public static void OpenActivity(Context context,Class<?> cls){

    }
    public static void OpenActivityWithID(Context context,Class<?> cls, Long ID){
        Intent intent = new Intent(context, cls);
        if(ID!=null)
            intent.putExtra(IDEXTRA, ID);
        OpenActivityIntent(context,intent);
    }
    public static void OpenActivityWithFRAGMENT(Context context,Class<?> cls, String name){
        Intent intent = new Intent(context, cls);
        if(name!=null)
            intent.putExtra(FRAGMENTEXTRA, name);
        OpenActivityIntent(context, intent);
    }
    public static void OpenActivityWithFRAGMENT(Context context, String name, Long ID){
        Intent intent = new Intent(context, OneFragmentActivity.class);
        if(name!=null)
            intent.putExtra(FRAGMENTEXTRA, name);
        if(ID!=null)
            intent.putExtra(IDEXTRA, ID);

        OpenActivityIntent(context,intent);
    }
}
