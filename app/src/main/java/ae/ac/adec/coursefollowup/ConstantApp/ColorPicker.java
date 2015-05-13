package ae.ac.adec.coursefollowup.ConstantApp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import ae.ac.adec.coursefollowup.R;

/**
 * Created by JMA on 4/2/2015.
 */
public class ColorPicker {
    String[] colors;
    Activity context;
    public CustomColorDialogClass dialog;

    public ColorPicker(Activity context, int screenWidth, int screenHeight, AdapterView.OnItemClickListener listener) {
        this.context = context;
        populateColorsList();
        dialog = new CustomColorDialogClass(context, context.getString(R.string.select_color), screenWidth, screenHeight, colors
                , listener);
        dialog.show(context.getFragmentManager(), "jma");
    }

    private void populateColorsList() {
        colors = context.getResources().getStringArray(R.array.colors);
    }
}
