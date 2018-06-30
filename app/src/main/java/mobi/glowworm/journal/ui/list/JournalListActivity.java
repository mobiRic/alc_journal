package mobi.glowworm.journal.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.module.GlideApp;
import com.dgreenhalgh.android.simpleitemdecoration.linear.StartOffsetItemDecoration;

import java.util.List;

import mobi.glowworm.journal.R;
import mobi.glowworm.journal.data.model.JournalEntry;
import mobi.glowworm.journal.ui.ABaseActivity;
import mobi.glowworm.journal.ui.details.DetailActivity;
import mobi.glowworm.journal.utils.DateTime;
import mobi.glowworm.lib.ui.AboutPage;
import mobi.glowworm.lib.ui.widget.EmptyLoadingRecyclerView;

/**
 * Main activity that shows a list of all {@link JournalEntry}
 * items from the database.
 * <p>
 * Clicking an item will open it in {@link DetailActivity}
 * where it can be edited.
 * <p>
 * Clicking the {@link FloatingActionButton} will open a
 * blank {@link DetailActivity} allowing a new journal
 * to be recorded.
 */
public class JournalListActivity extends ABaseActivity
        implements JournalAdapter.OnJournalClickListener, NavigationView.OnNavigationItemSelectedListener {

    @NonNull
    private EmptyLoadingRecyclerView recyclerView;
    private ImageView ivNavUserProfile;
    private TextView tvNavUserName;
    private TextView tvNavUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isUserSignedIn()) {
            onSignedOut();
            return;
        }

        setContentView(R.layout.nav_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNewJournal();
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

        recyclerView = findViewById(R.id.journal_list);
        assert recyclerView != null;
        initRecyclerView();

        // get the journals for this user
        JournalListViewModelFactory vmFactory = new JournalListViewModelFactory(getDatabase(), getCurrentUserId());
        JournalListViewModel viewModel = ViewModelProviders.of(this, vmFactory).get(JournalListViewModel.class);
        final LiveData<List<JournalEntry>> liveJournals = viewModel.getJournalList();
        liveJournals.observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntry> journals) {
                recyclerView.swapAdapter(
                        new JournalAdapter(journals, JournalListActivity.this, DateTime.getFormatDateTime(JournalListActivity.this)),
                        false);
                recyclerView.setLoading(false);
            }
        });
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

    private void initRecyclerView() {
        recyclerView.setEmptyView(findViewById(R.id.journal_list_empty_view));
        recyclerView.setLoadingView(findViewById(R.id.journal_list_loading_view));
        recyclerView.setLoading(true);

        // spacing between rows and below last row
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable spacer = ContextCompat.getDrawable(this, R.drawable.divider_16dp);
        mDividerItemDecoration.setDrawable(spacer);
        recyclerView.addItemDecoration(mDividerItemDecoration);

        // spacing before first row
        StartOffsetItemDecoration mStartItemDecoration = new StartOffsetItemDecoration(spacer);
        recyclerView.addItemDecoration(mStartItemDecoration);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_new_journal: {
                launchNewJournal();
                break;
            }
            case R.id.nav_logout: {
                signOut();
                break;
            }
            case R.id.nav_about: {
                AboutPage.launchAboutPage(this);
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Displays the user profile picture in the navigation drawer header.
     *
     * @param userProfilePic profile pic URL of current user
     */
    protected void setUserProfilePic(@Nullable Uri userProfilePic) {
        if (userProfilePic == null) {
            return;
        }

        GlideApp
                .with(this)
                .load(userProfilePic)
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
    protected Uri getUserProfilePicUrl() {
        return getCurrentUser().getPhotoUrl();
    }

    /**
     * Returns the user name to display in the navigation drawer.
     *
     * @return correctly formatted user name if available
     */
    @Nullable
    protected String getUserName() {
        return getCurrentUser().getDisplayName();
    }

    /**
     * Returns the email address to display in the navigation drawer.
     *
     * @return email address of current user if available
     */
    @Nullable
    protected String getUserEmail() {
        return getCurrentUser().getEmail();
    }

    @Override
    public void onJournalClicked(int journalId) {
        launchJournalDetails(journalId);
    }

    /**
     * Helper method to launch the {@link DetailActivity} for a
     * given {@link JournalEntry}.
     *
     * @param journalId id of journal to show
     */
    private void launchJournalDetails(int journalId) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(DetailActivity.KEY_JOURNAL_ID, journalId);
        launch(i);
    }

    /**
     * Helper method to launch the {@link DetailActivity} without sending
     * a journal id to it. This will allow the user to create a new journal.
     */
    private void launchNewJournal() {
        launch(DetailActivity.class);
    }
}
