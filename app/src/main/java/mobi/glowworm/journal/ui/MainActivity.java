package mobi.glowworm.journal.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.module.GlideApp;

import mobi.glowworm.journal.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView ivNavUserProfile;
    private TextView tvNavUserName;
    private TextView tvNavUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // populate navigation drawer header with current user details
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        if (headerView != null) {
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO implement action when user header is clicked
                }
            });

            ivNavUserProfile = headerView.findViewById(R.id.iv_nav_header_user_profile);
            tvNavUserName = (TextView) headerView.findViewById(R.id.tv_nav_header_user_name);
            tvNavUserEmail = (TextView) headerView.findViewById(R.id.tv_nav_header_user_email);

            setUserProfilePic(getUserProfilePicUrl());
            tvNavUserName.setText(getUserName());
            tvNavUserEmail.setText(getUserEmail());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Displays the user profile picture in the navigation drawer header.
     *
     * @param userProfilePicUrl profile pic URL of current user
     */
    protected void setUserProfilePic(@Nullable String userProfilePicUrl) {
        if (TextUtils.isEmpty(userProfilePicUrl)) {
            return;
        }

        GlideApp
                .with(this)
                .load(Uri.parse(userProfilePicUrl))
                .placeholder(R.mipmap.ic_launcher_round)
                .fitCenter()
                .into(ivNavUserProfile);
    }

    /**
     * Returns URL of the profile picture to display in the navigation drawer.
     *
     * @return profile pic URL of current user
     */
    @Nullable
    protected String getUserProfilePicUrl() {
        return "https://pbs.twimg.com/profile_images/2728387185/36389198bc50022eb5bf95e3ebfa549b_400x400.jpeg";
    }

    /**
     * Returns the user name to display in the navigation drawer.
     *
     * @return correctly formatted user name if available
     */
    @Nullable
    protected String getUserName() {
        return "Bob Cool";
    }

    /**
     * Returns the email address to display in the navigation drawer.
     *
     * @return email address of current user if available
     */
    @Nullable
    protected String getUserEmail() {
        return "bob.cool@glowworm-utils.com";
    }

}
