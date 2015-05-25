package ae.ac.adec.coursefollowup.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ae.ac.adec.coursefollowup.Lib.SlidingTabs.SlidingTabLayout;
import ae.ac.adec.coursefollowup.Lib.SlidingTabs.SlidingTabLayout_icons;
import ae.ac.adec.coursefollowup.R;
import ae.ac.adec.coursefollowup.views.event.IRemovableShadowToolBarShadow;

/**
 * Created by Tareq on 03/12/2015.
 */
public class TabFragment_Note extends BaseFragment {

    SlidingTabLayout_icons mSlidingTabLayout;
    ViewPager mViewPager;
    public static final String FRAGMENT = "FRAGMENT";
    String FragmentName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        FragmentName = getArguments().getString(FRAGMENT);

        ((IRemovableShadowToolBarShadow) getActivity()).RemoveToolBarShadow();
    }

    @Override
    public void onResume() {
        super.onResume();

        fillData();
    }

    private void fillData() {
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator_icon, android.R.id.icon1);
        mSlidingTabLayout.setContentDescription(1, getActivity().getString(R.string.text));
        mSlidingTabLayout.setContentDescription(2, getActivity().getString(R.string.image));
        mSlidingTabLayout.setContentDescription(3, getActivity().getString(R.string.voice));
        mSlidingTabLayout.setContentDescription(4, getActivity().getString(R.string.video));


        //Resources res = getResources();
        //res.getColor();
        mSlidingTabLayout.setSelectedIndicatorColors(Color.WHITE);

        mSlidingTabLayout.setDistributeEvenly(true);
        FragmentManager fragmentManager = getChildFragmentManager();
        String tabTitles[] = new String[]{getActivity().getString(R.string.text),
                getActivity().getString(R.string.image),
                getActivity().getString(R.string.voice),
                getActivity().getString(R.string.video)};
        mViewPager.setAdapter(new SampleFragmentPagerAdapter(fragmentManager, getActivity(), tabTitles, FragmentName));
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab_header_icon, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        mSlidingTabLayout = (SlidingTabLayout_icons) rootView.findViewById(R.id.sliding_tabs);

        removeShadowForNewApi21(rootView);

        fillData();

        return rootView;
    }


    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 4;
        private Context context;
        private String tabTitles[];
        private String FragmentName;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context, String tabTitles[], String FragmentName) {
            super(fm);
            this.context = context;
            this.tabTitles = tabTitles;
            this.FragmentName = FragmentName;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = Fragment.instantiate(context, FragmentName);
            //Fragment fragment=HolidayFragment.newInstance(position);
            Bundle args = new Bundle();
            args.putInt(BaseFragment.POSITION, position);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }


}
