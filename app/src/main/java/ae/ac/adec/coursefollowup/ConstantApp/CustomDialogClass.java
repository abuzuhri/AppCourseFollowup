package ae.ac.adec.coursefollowup.ConstantApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.db.dal.BaseDao;
import ae.ac.adec.coursefollowup.db.dal.CourseDao;
import ae.ac.adec.coursefollowup.db.models.Course;
import ae.ac.adec.coursefollowup.fragments.HolidayFragmentAddEdit;
import ae.ac.adec.coursefollowup.services.AppAction;
import ae.ac.adec.coursefollowup.views.adapters.CustomLVAdapter_Times;

/**
 * Created by JMA on 3/23/2015.
 */
public class CustomDialogClass<T> extends DialogFragment {
    String title;
    BaseAdapter adapter;
    Context activity;
    String fragmentName;
    long course_id;
    Boolean isMultiple, isDays = false;
    AdapterView.OnItemClickListener listener;
    DialogInterface.OnClickListener clickListener;
    public ListView lv;

    public CustomDialogClass(Context activity, String fragmentName, String title, BaseAdapter adapter, Boolean isMultiple, long course_id, AdapterView.OnItemClickListener listener) {
        this.title = title;
        this.adapter = adapter;
        this.activity = activity;
        this.fragmentName = fragmentName;
        this.listener = listener;
        this.isMultiple = isMultiple;
        this.course_id = course_id;
    }

    public CustomDialogClass(Context activity, String fragmentName, String title, BaseAdapter adapter, Boolean isMultiple,
                             long course_id, Boolean isDays) {
        this.title = title;
        this.adapter = adapter;
        this.activity = activity;
        this.fragmentName = fragmentName;
        this.isMultiple = isMultiple;
        this.course_id = course_id;
        this.isDays = isDays;
    }
    public void setClickListener(DialogInterface.OnClickListener clickListener){
        this.clickListener=clickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.custom_courses_dialoge, null);

        TextView tv_title = (TextView) v.findViewById(R.id.tv_customDialog_title);
        lv = (ListView) v.findViewById(R.id.lv_customDialog);
        if (isMultiple)
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        tv_title.setText(title);
        populateListView(lv);
        lv.setOnItemClickListener(listener);

        if (isDays)
            builder.setView(v).setPositiveButton("Ok", clickListener);
        else if (fragmentName != "")
            builder.setView(v).setPositiveButton("New", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    AppAction.OpenActivityWithFRAGMENT(getActivity(), OneFragmentActivity.class, fragmentName, course_id);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog

                }
            });
        else
            builder.setView(v).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog

                }
            });

        return builder.create();
    }


    private void populateListView(ListView lv) {
        lv.setAdapter(adapter);
    }


}