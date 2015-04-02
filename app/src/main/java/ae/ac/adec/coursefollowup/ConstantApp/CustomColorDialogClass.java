package ae.ac.adec.coursefollowup.ConstantApp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.activities.OneFragmentActivity;
import ae.ac.adec.coursefollowup.services.AppAction;

/**
 * Created by JMA on 3/23/2015.
 */
public class CustomColorDialogClass extends DialogFragment {
    String title;
    Context activity;
    int screenWidth, screenHeight;
    public String[] colors;
    AdapterView.OnItemClickListener listener;

    public CustomColorDialogClass(Context activity, String title, int screenWidth, int screenHeight, String[] colors,
                                  AdapterView.OnItemClickListener listener) {
        this.title = title;
        this.activity = activity;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.colors = colors;
        this.listener=listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.custom_color_dialoge, null);

        TextView tv_title = (TextView) v.findViewById(R.id.tv_customDialog_title);
        tv_title.setText(title);

        GridView gv_colors = (GridView) v.findViewById(R.id.gv_colors);
        gv_colors.setAdapter(new GridViewAdapter(getActivity(), colors));
        gv_colors.setOnItemClickListener(listener);

        builder.setView(v);


        return builder.create();
    }

    private class GridViewAdapter extends BaseAdapter {
        Context c;
        String[] colors;

        public GridViewAdapter(Context c, String[] colors) {
            this.c = c;
            this.colors = colors;
        }

        @Override
        public int getCount() {
            return colors.length;
        }

        @Override
        public Object getItem(int position) {
            return colors[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null)
                v = getActivity().getLayoutInflater().inflate(R.layout.gridview_colors_item,
                        parent, false);
            try {
                ImageView img = (ImageView) v.findViewById(R.id.gv_img_item);
                img.getLayoutParams().width = (int) (screenWidth * 0.1);
                img.getLayoutParams().height = (int) (screenHeight * 0.05);

                img.setBackgroundColor(Color.parseColor(colors[position]));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return v;
        }

    }
}