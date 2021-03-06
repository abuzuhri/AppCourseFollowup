package ae.ac.adec.coursefollowup.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.CustomColorDialogClass;
import ae.ac.adec.coursefollowup.ConstantApp.CustomDialogClass;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.dal.CourseTimeDayDao;
import ae.ac.adec.coursefollowup.db.dal.NoteDao;
import ae.ac.adec.coursefollowup.db.dal.NotificationDao;
import ae.ac.adec.coursefollowup.db.dal.SemesterDao;
import ae.ac.adec.coursefollowup.db.dal.TaskDao;
import ae.ac.adec.coursefollowup.db.dal.TestDao;
import ae.ac.adec.coursefollowup.db.dal.YearDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.db.models.CourseTimeDay;
import ae.ac.adec.coursefollowup.db.models.Note;
import ae.ac.adec.coursefollowup.db.models.Notification;
import ae.ac.adec.coursefollowup.db.models.Semester;
import ae.ac.adec.coursefollowup.db.models.Task;
import ae.ac.adec.coursefollowup.db.models.Year;
import ae.ac.adec.coursefollowup.fragments.HolidayFragmentAddEdit;
import ae.ac.adec.coursefollowup.fragments.YearFragmentAddEdit;
import ae.ac.adec.coursefollowup.services.BusinessRoleError;
import ae.ac.adec.coursefollowup.services.auths.AppFacebookAuth;
import ae.ac.adec.coursefollowup.services.auths.AppGoogleAuth;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Courses;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Years;

public class Login_Activity extends ActionBarActivity {
    TextView tv_title, tv_loading;
    EditText et_username, et_pass;
    ImageView img_logo;
    LinearLayout ll_loginForm;
    RelativeLayout ll_logoLoading;
    FloatingActionButton btn_fb, btn_google, btn_next;
    Display display;
    int screenHeight, screenWidth;
    float text_size;
    AppGoogleAuth googleAuth;
    Activity activity;
    final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        NotificationDao n = new NotificationDao();
//        List<Notification> notifications = n.getAll(2);
//        Toast.makeText(getBaseContext(), notifications.size() + " Notification", Toast.LENGTH_LONG).show();
//        for (Notification notification : notifications) {
//            Calendar c = Calendar.getInstance();
//            Toast.makeText(getBaseContext(), notification.Course.Name + " ON: " + notification.CalenderDateTime.toGMTString()
//                    +" IsHoliday: "+notification.IsHoliday, Toast.LENGTH_LONG).show();
//        }

        Intent intent = new Intent(Login_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();

        setContentView(R.layout.activity_login);

        activity = this;
        // initialize Google Auth, Facebook does not need to initialize, it makes login directly
        googleAuth = new AppGoogleAuth(activity);

        initializeResponsiveScreenParameters();
        initializeViews();
        ResizeViews();
        hideLoginForm();
        waitAndShowLoginForm();

        btn_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // starts Facebook authentication
                AppFacebookAuth fbAuth = new AppFacebookAuth(activity);
            }
        });
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Google login
                if (!googleAuth.mGoogleApiClient.isConnected())
                    googleAuth.googlePlusLogin();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void waitAndShowLoginForm() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Animation animTranslate = AnimationUtils.loadAnimation(Login_Activity.this, R.anim.logo_translate);
                        animTranslate.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation arg0) {
                                Animation animFade = AnimationUtils.loadAnimation(Login_Activity.this, R.anim.login_loadin_fade_in);
                                tv_loading.startAnimation(animFade);
                            }

                            @Override
                            public void onAnimationRepeat(Animation arg0) {
                            }

                            @Override
                            public void onAnimationEnd(Animation arg0) {
                                ll_loginForm.setVisibility(View.VISIBLE);
                                Animation animFade = AnimationUtils.loadAnimation(Login_Activity.this, R.anim.login_form_fade);
                                ll_loginForm.startAnimation(animFade);
                            }
                        });
                        ll_logoLoading.startAnimation(animTranslate);
                    }
                });
            }
        }, SPLASH_DELAY);
    }

    private void hideLoginForm() {
        ll_loginForm.setVisibility(View.GONE);
    }

    private void showLoginForm() {
        ll_loginForm.setVisibility(View.VISIBLE);
    }

    private void ResizeViews() {
        img_logo.getLayoutParams().width = (int) (0.45 * screenWidth);
        img_logo.getLayoutParams().height = (int) (0.25 * screenHeight);
    }

    private void initializeResponsiveScreenParameters() {
        display = getWindowManager().getDefaultDisplay();
        screenHeight = display.getHeight();
        screenWidth = display.getWidth();
        text_size = getResources().getDimension(R.dimen.general_text_size);
    }

    private void initializeViews() {
        img_logo = (ImageView) findViewById(R.id.img_login_logo);
        tv_title = (TextView) findViewById(R.id.tv_login_title);
        tv_loading = (TextView) findViewById(R.id.tv_login_loading);
        et_username = (EditText) findViewById(R.id.et_login_username);
        et_pass = (EditText) findViewById(R.id.et_login_pass);
        btn_fb = (FloatingActionButton) findViewById(R.id.fab_login_facebook);
        btn_google = (FloatingActionButton) findViewById(R.id.fab_login_google);
        btn_next = (FloatingActionButton) findViewById(R.id.fab_login_next);
        ll_loginForm = (LinearLayout) findViewById(R.id.ll_login_form);
        ll_logoLoading = (RelativeLayout) findViewById(R.id.rl_login_logoLoading);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        googleAuth.mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleAuth.mGoogleApiClient.isConnected()) {
            googleAuth.mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == googleAuth.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                googleAuth.signedInUser = false;
            }
            googleAuth.mIntentInProgress = false;
            if (!googleAuth.mGoogleApiClient.isConnecting()) {
                googleAuth.mGoogleApiClient.connect();
            }
        } else if (data != null)
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

    }
}
