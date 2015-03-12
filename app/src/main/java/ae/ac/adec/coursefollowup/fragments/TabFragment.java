package ae.ac.adec.coursefollowup.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ae.ac.adec.coursefollowup.Lib.SlidingTabs.SlidingTabLayout;
import ae.ac.adec.coursefollowup.R;

/**
 * Created by Tareq on 03/12/2015.
 */
public class TabFragment extends BaseFragment {

    SlidingTabLayout mSlidingTabLayout;
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab_header, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        mSlidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);

        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setContentDescription(1, "Item1");
        mSlidingTabLayout.setContentDescription(2, "Item2");

        //Resources res = getResources();
        //res.getColor();
        mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);

        mSlidingTabLayout.setDistributeEvenly(true);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mSlidingTabLayout.setViewPager(mViewPager);


        if (mSlidingTabLayout != null) {
            mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        return rootView;
    }


    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Item " + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = getActivity(). getLayoutInflater().inflate(R.layout.test_item, container, false);
            TextView txt = (TextView) view.findViewById(R.id.item_subtitle);
            txt.setText("Page:"+position);
            // Add the newly created View to the ViewPager
            container.addView(view);

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
