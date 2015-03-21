package ae.ac.adec.coursefollowup.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import ae.ac.adec.coursefollowup.ConstantApp.AppLog;
import ae.ac.adec.coursefollowup.ConstantApp.ConstantVariable;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.fragments.CalenderFragment;
import ae.ac.adec.coursefollowup.fragments.CoursesFragment;
import ae.ac.adec.coursefollowup.fragments.DashboardFragment;
import ae.ac.adec.coursefollowup.fragments.ExamFragment;
import ae.ac.adec.coursefollowup.fragments.HolidayFragment;
import ae.ac.adec.coursefollowup.fragments.NoteFragment;
import ae.ac.adec.coursefollowup.fragments.ProfileFragment;
import ae.ac.adec.coursefollowup.fragments.SearchFragment;
import ae.ac.adec.coursefollowup.fragments.SemesterFragment;
import ae.ac.adec.coursefollowup.fragments.SettingFragment;
import ae.ac.adec.coursefollowup.fragments.TabFragment;
import ae.ac.adec.coursefollowup.fragments.TaskFragment;
import ae.ac.adec.coursefollowup.fragments.YearsFragment;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by Tareq on 02/27/2015.
 */
public class BaseActivity   extends ActionBarActivity implements IRemovableShadowToolBarShadow {

    public Toolbar toolbar=null;
    public Drawer.Result result;
    public AccountHeader.Result headerResult;
    public View shadowView;
    public View toolbarContainer;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

//test 1  يسلم ايديك
        //test 2 iwant to show you
        //test 3
        //test 4
        //Holiday & year working ...  BY

        //OK it work no .. check all your work exist ...
    }

    public void SetupToolbarShadow(){
        //Shadow View
        shadowView=findViewById(R.id.shadow_main_activity);
        toolbarContainer=findViewById(R.id.activity_main_toolbar_container);
        // Solve Android bug in API < 21 by app custom shadow
        SetToolbarShadow();
    }

    public void SetToolbarShadow()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shadowView.setVisibility(View.GONE);
            final float scale = getResources().getDisplayMetrics().density;
            toolbarContainer.setElevation(5f*scale);
        }else{
            shadowView.setVisibility(View.VISIBLE);
        }
    }

    public void Drawable(){
        // Create the AccountHeader
        String Name="Jafar Edit Name";
        String Email="Jafar@Edit.com";
        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.blueheader)
                .addProfiles(
                        new ProfileDrawerItem().withName(Name).withEmail(Email).withIcon(getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public void onProfileChanged(View view, IProfile profile) {
                        selectItem(ConstantVariable.Category.Profile.id);
                    }
                })
                .build();


        result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withHeader(R.layout.header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.category_dashboard).withIdentifier(ConstantVariable.Category.Dashboard.id).withIcon(FontAwesome.Icon.faw_dashboard),
                        new PrimaryDrawerItem().withName(R.string.category_calender).withIdentifier(ConstantVariable.Category.Calender.id).withIcon(FontAwesome.Icon.faw_calendar),
                        new PrimaryDrawerItem().withName(R.string.category_classes).withIdentifier(ConstantVariable.Category.Classes.id).withIcon(FontAwesome.Icon.faw_book),
                        // new SectionDrawerItem().withName("Sec"),
                        new PrimaryDrawerItem().withName(R.string.category_tasks).withIdentifier(ConstantVariable.Category.Tasks.id).withIcon(FontAwesome.Icon.faw_tasks),
                        new PrimaryDrawerItem().withName(R.string.category_notes).withIdentifier(ConstantVariable.Category.Notes.id).withIcon(FontAwesome.Icon.faw_comment),
                        new PrimaryDrawerItem().withName(R.string.category_exams).withIdentifier(ConstantVariable.Category.Exams.id).withIcon(FontAwesome.Icon.faw_edit),
                        new PrimaryDrawerItem().withName(R.string.category_semesters).withIdentifier(ConstantVariable.Category.Semesters.id).withIcon(FontAwesome.Icon.faw_cubes),
                        new PrimaryDrawerItem().withName(R.string.category_year).withIdentifier(ConstantVariable.Category.Years.id).withIcon(FontAwesome.Icon.faw_university),


                        new PrimaryDrawerItem().withName(R.string.category_holidays).withIdentifier(ConstantVariable.Category.Holiday.id).withIcon(FontAwesome.Icon.faw_hotel),
                        new PrimaryDrawerItem().withName(R.string.category_search).withIdentifier(ConstantVariable.Category.Search.id).withIcon(FontAwesome.Icon.faw_search),
                        new PrimaryDrawerItem().withName(R.string.category_setting).withIdentifier(ConstantVariable.Category.Setting.id).withIcon(FontAwesome.Icon.faw_gear),
                        // For Test Only
                        new PrimaryDrawerItem().withName(R.string.category_test).withIdentifier(ConstantVariable.Category.Test.id).withIcon(FontAwesome.Icon.faw_exchange)
                )
                .withSelectedItem(0)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {
                                toolbar.setTitle(((Nameable) drawerItem).getNameRes());
                                //toolbar.setSubtitle(((Nameable) drawerItem).getNameRes());
                            }

                            selectItem(drawerItem.getIdentifier());

                        }
                    }
                })
                .build();


        //disable scrollbar :D it's ugly
        result.getListView().setVerticalScrollBarEnabled(false);
        selectItem(ConstantVariable.Category.Dashboard.id);
    }
    public  void  selectItem(int filter){
        Fragment fragment = null;
        Bundle args = new Bundle();
        AppLog.i("position=> " + filter);


        if (filter == ConstantVariable.Category.Dashboard.id) {
            fragment = new DashboardFragment();
        }else  if (filter == ConstantVariable.Category.Notes.id) {
            fragment = new TabFragment();
            args.putString(TabFragment.FRAGMENT, NoteFragment.class.getName());
        }else if (filter == ConstantVariable.Category.Setting.id) {
            fragment = new SettingFragment();
        }else if (filter == ConstantVariable.Category.Search.id) {
            fragment = new SearchFragment();
        }else if (filter == ConstantVariable.Category.Classes.id) {
            fragment = new TabFragment();
            args.putString(TabFragment.FRAGMENT, CoursesFragment.class.getName());
        }else if (filter == ConstantVariable.Category.Calender.id) {
            fragment = new CalenderFragment();
        }else if (filter == ConstantVariable.Category.Exams.id) {
            fragment = new TabFragment();
            args.putString(TabFragment.FRAGMENT, ExamFragment.class.getName());
        }else if (filter == ConstantVariable.Category.Semesters.id) {
            fragment = new TabFragment();
            args.putString(TabFragment.FRAGMENT, SemesterFragment.class.getName());
        }else if (filter == ConstantVariable.Category.Years.id) {
            fragment = new TabFragment();
            args.putString(TabFragment.FRAGMENT, YearsFragment.class.getName());
        }else if (filter == ConstantVariable.Category.Tasks.id) {
            fragment = new TabFragment();
            args.putString(TabFragment.FRAGMENT, TaskFragment.class.getName());
        }else if (filter == ConstantVariable.Category.Holiday.id) {
            fragment = new TabFragment();
            args.putString(TabFragment.FRAGMENT, HolidayFragment.class.getName());
        }else if (filter == ConstantVariable.Category.Profile.id) {
            fragment = new ProfileFragment();
        }else if (filter == ConstantVariable.Category.Test.id) {
            fragment = new TabFragment();
            args.putString(TabFragment.FRAGMENT, HolidayFragment.class.getName());
        }

        fragment.setArguments(args);

        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_main, fragment)
                    .commit();
        }
        SetToolbarShadow();
    }

    @Override
    public void RemoveToolBarShadow() {
        shadowView.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toolbarContainer.setElevation(0);
    }
}
