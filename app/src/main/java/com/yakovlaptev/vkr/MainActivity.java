package com.yakovlaptev.vkr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yakovlaptev.vkr.Models.Event;
import com.yakovlaptev.vkr.Models.User;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final String TAG = "myLogs";
    public static final String SERVER = "http://vkrserver-env.hdnu6ttdsc.eu-west-3.elasticbeanstalk.com/:8080";
    static User currentUser;
    private ImageView avatar;
    private TextView name, email;

    static Intent chatIntent;
    private View coordLayout;

    ViewPager pager;
    PagerAdapter pagerAdapter;
    static FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chatIntent != null) {
                    startActivity(new Intent(MainActivity.this, VideoChatActivity.class));
                } else {
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectItem(2);
                        }
                    };
                    Snackbar.make(coordLayout, "We have no chat opened", Snackbar.LENGTH_LONG)
                            .setAction("See events", listener).show();
                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        currentUser = (User) (Objects.requireNonNull(this.getIntent().getExtras())).get("user");
        Log.d("MAINACTIVITY User",currentUser.toString());

        View header = navigationView.getHeaderView(0);
        avatar = header.findViewById(R.id.avatar);
        name = header.findViewById(R.id.name);
        email = header.findViewById(R.id.email);
        coordLayout = findViewById(R.id.coordinator);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem(0);
            }
        };

        avatar.setOnClickListener(onClickListener);
        name.setText(currentUser.getName());
        name.setOnClickListener(onClickListener);
        email.setText(currentUser.getEmail());
        email.setOnClickListener(onClickListener);

        Fragment fragment = new Events();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    private void selectItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                setTitle("Profile");
                fragment = new MyEvents();
                break;
            case 1:
                setTitle("Users");
                fragment = new Users();
                break;
            case 2:
                setTitle("Events");
                fragment = new Events();
                break;
            case 3:
                setTitle("My Events");
                fragment = new MyEvents();
                break;
            case 4:
                setTitle("My Requests");
                fragment = new MyRequests();
                break;
            default:
                break;
        }

        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else {
            Log.e(this.getClass().getName(), "Error. Fragment is not created");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("log_out", true);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            selectItem(0);
        } else if (id == R.id.nav_users) {
            selectItem(1);
        } else if (id == R.id.nav_events) {
            selectItem(2);
        } else if (id == R.id.nav_my_chats) {
            if(chatIntent != null) {
                startActivity(new Intent(this, VideoChatActivity.class));
            } else {
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectItem(2);
                    }
                };
                Snackbar.make(coordLayout, "We have no chat opened", Snackbar.LENGTH_LONG)
                        .setAction("See events", listener).show();
            }
        } else if (id == R.id.nav_my_events) {
            selectItem(3);
        } else if (id == R.id.nav_my_requests) {
            selectItem(4);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
