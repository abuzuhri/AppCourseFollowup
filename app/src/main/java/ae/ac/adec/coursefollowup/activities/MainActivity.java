package ae.ac.adec.coursefollowup.activities;


import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.fragments.CalenderFragment;
import ae.ac.adec.coursefollowup.fragments.CoursesFragment;
import ae.ac.adec.coursefollowup.fragments.DashboardFragment;
import ae.ac.adec.coursefollowup.fragments.ExamFragment;
import ae.ac.adec.coursefollowup.fragments.HolidayFragment;
import ae.ac.adec.coursefollowup.fragments.NoteFragment;
import ae.ac.adec.coursefollowup.fragments.SearchFragment;
import ae.ac.adec.coursefollowup.fragments.SemesterFragment;
import ae.ac.adec.coursefollowup.fragments.SettingFragment;
import ae.ac.adec.coursefollowup.fragments.TaskFragment;
import ae.ac.adec.coursefollowup.services.auths.AppFacebookAuth;
import ae.ac.adec.coursefollowup.services.auths.AppGoogleAuth;


public class MainActivity extends BaseActivity {

    AppGoogleAuth googleApp;
    public Drawer.Result result;


    public enum Category {
        Dashboard(10),
        Calender(20),
        Tasks(30),
        Notes(430),
        Exams(50),
        Semesters(60),
        Classes(70),
        Holiday(80),
        Search(99),
        Setting(100);

        public int id;

        private Category(int id) {
            this.id = id;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.category_dashboard).withIdentifier(Category.Dashboard.id).withIcon(FontAwesome.Icon.faw_dashboard),
                        new PrimaryDrawerItem().withName(R.string.category_calender).withIdentifier(Category.Calender.id).withIcon(FontAwesome.Icon.faw_calendar),
                        // new SectionDrawerItem().withName("Sec"),
                        new PrimaryDrawerItem().withName(R.string.category_tasks).withIdentifier(Category.Tasks.id).withIcon(FontAwesome.Icon.faw_tasks),
                        new PrimaryDrawerItem().withName(R.string.category_notes).withIdentifier(Category.Notes.id).withIcon(FontAwesome.Icon.faw_comment),
                        new PrimaryDrawerItem().withName(R.string.category_exams).withIdentifier(Category.Exams.id).withIcon(FontAwesome.Icon.faw_edit),
                        new PrimaryDrawerItem().withName(R.string.category_semesters).withIdentifier(Category.Semesters.id).withIcon(FontAwesome.Icon.faw_cubes),
                        new PrimaryDrawerItem().withName(R.string.category_classes).withIdentifier(Category.Classes.id).withIcon(FontAwesome.Icon.faw_book),
                        new PrimaryDrawerItem().withName(R.string.category_holidays).withIdentifier(Category.Holiday.id).withIcon(FontAwesome.Icon.faw_hotel),
                        new PrimaryDrawerItem().withName(R.string.category_search).withIdentifier(Category.Search.id).withIcon(FontAwesome.Icon.faw_search),
                        new PrimaryDrawerItem().withName(R.string.category_setting).withIdentifier(Category.Setting.id).withIcon(FontAwesome.Icon.faw_gear)
                )
                .withSelectedItem(0)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {
                                //toolbar.setTitle(((Nameable) drawerItem).getNameRes());
                                toolbar.setSubtitle(((Nameable) drawerItem).getNameRes());
                            }

                            selectItem(drawerItem.getIdentifier());

                            if (onFilterChangedListener != null) {

                            }

                        }
                    }
                })
                .build();

        //disable scrollbar :D it's ugly
        result.getListView().setVerticalScrollBarEnabled(false);
        selectItem(Category.Dashboard.id);

        //Update jma
        // test facebook login
        //AppFacebookAuth test=new AppFacebookAuth(this);

        //Test Google login
        //googleApp = new AppGoogleAuth(this);
    }


    private OnFilterChangedListener onFilterChangedListener;

    public void setOnFilterChangedListener(OnFilterChangedListener onFilterChangedListener) {
        this.onFilterChangedListener = onFilterChangedListener;
    }

    public interface OnFilterChangedListener {
        public void onFilterChanged(int filter);
    }

    public void selectItem(int filter) {
        Fragment fragment = null;
        Log.i("tg", "position=> " + filter);


        if (filter == Category.Dashboard.id) {
            fragment = new DashboardFragment();
        } else if (filter == Category.Notes.id) {
            fragment = new NoteFragment();
        } else if (filter == Category.Setting.id) {
            fragment = new SettingFragment();
            googleApp.googlePlusLogout();
        } else if (filter == Category.Search.id) {
            fragment = new SearchFragment();
            googleApp.googlePlusLogin();
        } else if (filter == Category.Classes.id) {
            fragment = new CoursesFragment();
        } else if (filter == Category.Calender.id) {
            fragment = new CalenderFragment();
        } else if (filter == Category.Exams.id) {
            fragment = new ExamFragment();
        } else if (filter == Category.Semesters.id) {
            fragment = new SemesterFragment();
        } else if (filter == Category.Tasks.id) {
            fragment = new TaskFragment();
        } else if (filter == Category.Holiday.id) {
            fragment = new HolidayFragment();
        }

        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, fragment)
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // update jma
        //googleApp.mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // update jma
        /*if (googleApp.mGoogleApiClient.isConnected()) {
            googleApp.mGoogleApiClient.disconnect();
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Remove comment when using facebook login
        //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

        // Remove comment when using google login
        /*if (requestCode == googleApp.RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                googleApp.signedInUser = false;
            }
            googleApp.mIntentInProgress = false;
            if (!googleApp.mGoogleApiClient.isConnecting()) {
                googleApp.mGoogleApiClient.connect();
            }
        }*/

    }

}
