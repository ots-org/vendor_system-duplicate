package com.android.water_distribution.Activity;


import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.water_distribution.R;
import com.android.water_distribution.fragment.AddUserFragment;
import com.android.water_distribution.fragment.SearchUserFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StatusListActivity extends AppCompatActivity {
    private static final String TAG = StatusListActivity.class.getSimpleName();

    Toolbar toolbar;
    ActionBar action;
    TabLayout tabs;
    ViewPager viewpager;
    ProgressBar progressBar;
    CardView emptyView;
    Adapter adapter;
    Gson gson;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_enterprise);
        tabs = findViewById(R.id.tabs);
        viewpager = findViewById(R.id.viewpager);
        progressBar = findViewById(R.id.progress_bar);
        emptyView = findViewById(R.id.empty_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        adapter = new Adapter(getSupportFragmentManager());
        gson = new Gson();

        viewpager.setOffscreenPageLimit(3);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            action = getSupportActionBar();
            action.setDisplayHomeAsUpEnabled(true);
            action.setHomeButtonEnabled(true);

            action.setDisplayShowTitleEnabled(false);
            action.setDisplayShowCustomEnabled(true);

            //this.action.setTitle((CharSequence) "Update Stock");

            View viewActionBar = getLayoutInflater().inflate(R.layout.view_custom_toolbar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            TextView toolbarTitle = (TextView) viewActionBar.findViewById(R.id.toolbar_title);
            toolbarTitle.setText("My Orders");
            action.setCustomView(viewActionBar, params);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        progressBar.setVisibility(View.GONE);
        getEnterpriseSearchResults(AVAILABLE_TYPE.NEW);
        getEnterpriseSearchResults(AVAILABLE_TYPE.OPEN);
        getEnterpriseSearchResults(AVAILABLE_TYPE.ASSIGNED);
        getEnterpriseSearchResults(AVAILABLE_TYPE.CLOSED);
    }

    private void getEnterpriseSearchResults(final AVAILABLE_TYPE type) {

                switch (type) {
                    case NEW:
                        Fragment subscribedFragment = CustomerStatusListFragment.newInstance();
                        Bundle bundle = new Bundle();
                        bundle.putString("status","NEW");
                        subscribedFragment.setArguments(bundle);
                        adapter.addFragment(subscribedFragment, "New");
                        break;
                    case OPEN:
                            Fragment unsubscribedFragment = CustomerStatusListFragment.newInstance();
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("status","Open");
                            unsubscribedFragment.setArguments(bundle1);
                            adapter.addFragment(unsubscribedFragment, "Open");
                        break;
                    case ASSIGNED:
                            Fragment usubscribedFragment = CustomerStatusListFragment.newInstance();
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("status","Assigned");
                            usubscribedFragment.setArguments(bundle2);
                            adapter.addFragment(usubscribedFragment, "Assigned");
                        break;
                    case CLOSED:
                            Fragment unsubscribedFagment = CustomerStatusListFragment.newInstance();
                            Bundle bundle3 = new Bundle();
                            bundle3.putString("status","Close");
                            unsubscribedFagment.setArguments(bundle3);
                            adapter.addFragment(unsubscribedFagment, "Closed");
                        break;
                }

                    if (progressBar != null)
                        progressBar.setVisibility(View.GONE);

                    if (adapter.getCount() > 0) {
                        if (tabs != null) {
                            tabs.setupWithViewPager(viewpager);
                            tabs.setVisibility(View.VISIBLE);
                        }
                        if (viewpager != null) {
                            viewpager.setAdapter(adapter);
                            viewpager.setOffscreenPageLimit(2);
                        }

                    } else {

                        if (tabs != null)
                            tabs.setVisibility(View.GONE);

                        if (emptyView != null)
                            emptyView.setVisibility(View.VISIBLE);
                    }

    }


    public enum AVAILABLE_TYPE {
        NEW,
        OPEN,
        ASSIGNED,
        CLOSED
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < mFragments.size())
                return mFragments.get(position);

            return mFragments.get(0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitles.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
