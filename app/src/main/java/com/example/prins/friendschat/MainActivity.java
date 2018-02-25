package com.example.prins.friendschat;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.prins.friendschat.adapters.GenericViewPagerAdapter;
import com.example.prins.friendschat.fragments.ChatFragment;
import com.example.prins.friendschat.fragments.GroupsFragment;
import com.example.prins.friendschat.fragments.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_tabs)
    TabLayout mainTabs;
    @BindView(R.id.main_viewPager)
    ViewPager mainViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initPager();
    }

    private void initPager(){
        GenericViewPagerAdapter adapter = new GenericViewPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new ChatFragment(), "Chat");
        adapter.addPage(new GroupsFragment(), "Group");
        adapter.addPage(new ProfileFragment(), "Profile");
        mainViewPager.setAdapter(adapter);
        mainTabs.setupWithViewPager(mainViewPager);
    }

}
