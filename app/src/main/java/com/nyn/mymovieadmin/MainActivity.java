package com.nyn.mymovieadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.add(new drama(),"Drama");
        viewPagerAdapter.add(new Movie(),"Movie");
        viewPagerAdapter.add(new Anime(),"Anime");
        viewPagerAdapter.add(new Category(),"Category");
        viewPagerAdapter.add(new DramaTitle(),"Drama Title");
        viewPagerAdapter.add(new MovieType(),"Movie Type");
        viewPagerAdapter.add(new AnimeType(),"Anime Type");
        viewPagerAdapter.add(new dramaType(),"Drama Type");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
