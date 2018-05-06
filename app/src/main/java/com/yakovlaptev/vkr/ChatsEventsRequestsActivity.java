package com.yakovlaptev.vkr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class ChatsEventsRequestsActivity extends Fragment {

    private ViewPager mViewPager;
    private int selected;

    @SuppressLint("ValidFragment")
    public ChatsEventsRequestsActivity(int selected) {
        this.selected = selected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_chats_events_requests, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = view.findViewById(R.id.pager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(selected);
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
            switch (position) {
                case 0:
                    return "Events";
                case 1:
                    return "Requests";
                default:
                    return "";
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            switch (position) {
                case 0:
                    view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_my_events, container, false);
                    break;
                case 1:
                    view = LayoutInflater.from(container.getContext()).inflate(R.layout.activity_my_requests, container, false);
                    break;
                default:
                    view = null;
                    break;
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
