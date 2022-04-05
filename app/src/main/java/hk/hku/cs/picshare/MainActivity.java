package hk.hku.cs.picshare;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import hk.hku.cs.picshare.account.AccountFragment;
import hk.hku.cs.picshare.base.BaseActivity;
import hk.hku.cs.picshare.base.BaseFragment;
import hk.hku.cs.picshare.list.PictureListFragment;
import hk.hku.cs.picshare.search.SearchFragment;

public class MainActivity extends BaseActivity {
    private ViewPager2 mViewPager;
    private MainPageAdapter mPageAdapter;
    private TabLayout mTabLayout;
    private TabLayoutMediator mTabLayoutMediator;
    private List<BaseFragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserVerifyStatus();
        initFragments();
        initView();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initView() {
        mViewPager = findViewById(R.id.fragment_view_pager);
        mPageAdapter = new MainPageAdapter(this);
        mViewPager.setAdapter(mPageAdapter);
        mTabLayout = findViewById(R.id.main_bottom_view);
        mTabLayout.setTabTextColors(Color.parseColor("#dbdbdb"), getColor(R.color.main));
        mTabLayout.setSelectedTabIndicator(null);
        mTabLayoutMediator = new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("list");
                    break;
                case 1:
                    tab.setText("search");
                    break;
                case 2:
                    tab.setText("account");
                    break;
            }
        });
        mTabLayoutMediator.attach();
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onPageSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        onPageSelect(0);
    }

    private void initFragments() {
        PictureListFragment pictureListFragment = new PictureListFragment();
        SearchFragment searchFragment = new SearchFragment();
        AccountFragment accountFragment = new AccountFragment();
        mFragmentList.add(pictureListFragment);
        mFragmentList.add(searchFragment);
        mFragmentList.add(accountFragment);
    }

    private void onPageSelect(int position) {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            BaseFragment fragment = mFragmentList.get(i);
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (i == position) {
                tab.setIcon(fragment.getOnSelectTabIcon());
            } else {
                tab.setIcon(fragment.getOnUnSelectTabIcon());
            }
        }
    }

    private class MainPageAdapter extends FragmentStateAdapter {
        public MainPageAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }
    }

    private void checkUserVerifyStatus()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("VerifyStatus", Context.MODE_PRIVATE);
        int CurrentUserVerifyStatus=sharedPreferences.getInt("CurrentUserVerify",0);
        if(CurrentUserVerifyStatus==0)
        {
            Intent JumpToLogin=new Intent(this,LoginActivity.class);
            startActivity(JumpToLogin);
        }

    }

}