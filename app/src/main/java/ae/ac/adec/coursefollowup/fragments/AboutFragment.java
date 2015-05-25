package ae.ac.adec.coursefollowup.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Tareq on 03/11/2015.
 */
public class AboutFragment extends BaseFragment {

    TextView tv_name1_label,tv_name2_label,tv_name1,tv_name2
            ,tv_email1,tv_email2,tv_email1_label,tv_email2_label
            ,tv_univ1_label,tv_univ2_label,tv_univ1,tv_univ2;
    CircleImageView img1,img2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        initializingViews(rootView);
        populatingData();

        return rootView;
    }

    private void populatingData() {
        tv_name1.setText(getString(R.string.dev1_name));
        tv_email1.setText(getString(R.string.dev1_email));
        tv_univ1.setText(getString(R.string.dev1_univ));
        img1.setImageResource(R.drawable.ahmed);

        tv_name2.setText(getString(R.string.dev2_name));
        tv_email2.setText(getString(R.string.dev2_email));
        tv_univ2.setText(getString(R.string.dev2_univ));
        img2.setImageResource(R.drawable.mohammed);

    }

    private void initializingViews(View rootView) {
        tv_name1_label = (TextView) rootView.findViewById(R.id.txtNameLabel_about1);
        tv_name1_label.setTypeface(tf_roboto_light);
        tv_name1_label.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_name2_label = (TextView) rootView.findViewById(R.id.txtNameLabel_about2);
        tv_name2_label.setTypeface(tf_roboto_light);
        tv_name2_label.setTextColor(getResources().getColor(R.color.colorPrimary));

        tv_email1_label = (TextView) rootView.findViewById(R.id.txtEmailLabel_about1);
        tv_email1_label.setTypeface(tf_roboto_light);
        tv_email1_label.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_email2_label = (TextView) rootView.findViewById(R.id.txtEmailLabel_about2);
        tv_email2_label.setTypeface(tf_roboto_light);
        tv_email2_label.setTextColor(getResources().getColor(R.color.colorPrimary));

        tv_univ1_label = (TextView) rootView.findViewById(R.id.txtUnivLabel_about1);
        tv_univ1_label.setTypeface(tf_roboto_light);
        tv_univ1_label.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv_univ2_label = (TextView) rootView.findViewById(R.id.txtUnivLabel_about2);
        tv_univ2_label.setTypeface(tf_roboto_light);
        tv_univ2_label.setTextColor(getResources().getColor(R.color.colorPrimary));

        tv_name1 = (TextView) rootView.findViewById(R.id.txtName_about1);
        tv_name1.setTypeface(tf_roboto_light);
        tv_name2 = (TextView) rootView.findViewById(R.id.txtName_about2);
        tv_name2.setTypeface(tf_roboto_light);

        tv_email1 = (TextView) rootView.findViewById(R.id.txtEmail_about1);
        tv_email1.setTypeface(tf_roboto_light);
        tv_email2 = (TextView) rootView.findViewById(R.id.txtEmail_about2);
        tv_email2.setTypeface(tf_roboto_light);

        tv_univ1 = (TextView) rootView.findViewById(R.id.txtUniv_about1);
        tv_univ1.setTypeface(tf_roboto_light);
        tv_univ2 = (TextView) rootView.findViewById(R.id.txtUniv_about2);
        tv_univ2.setTypeface(tf_roboto_light);

        img1 = (CircleImageView) rootView.findViewById(R.id.profile_image_about1);
        img2 = (CircleImageView) rootView.findViewById(R.id.profile_image_about2);
    }
}
