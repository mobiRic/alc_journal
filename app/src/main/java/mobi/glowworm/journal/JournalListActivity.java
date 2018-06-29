package mobi.glowworm.journal;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import mobi.glowworm.journal.data.model.JournalEntry;
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
public class JournalListActivity extends ADataActivity implements JournalAdapter.OnJournalClickListener {

    @NonNull
    private EmptyLoadingRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

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

        recyclerView = findViewById(R.id.journal_list);
        assert recyclerView != null;
        initRecyclerView();

        // get the journals for this user
        final LiveData<List<JournalEntry>> liveJournals = getDao().loadAllJournalsForUser(getCurrentUserId());
        liveJournals.observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntry> journals) {
                recyclerView.swapAdapter(new JournalAdapter(journals, JournalListActivity.this), false);
                recyclerView.setLoading(false);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setEmptyView(findViewById(R.id.journal_list_empty_view));
        recyclerView.setLoadingView(findViewById(R.id.journal_list_loading_view));
        recyclerView.setLoading(true);
    }

    @Override
    public void onJournalClicked(int journalId) {
        launchJournalDetails(journalId);
    }

    private int getCurrentUserId() {
        // TODO implement this to return a real user id after Firebase Auth has been added
        return JournalEntry.LOCAL_USER_NOT_LOGGED_IN;
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
        startActivity(i);
    }

    /**
     * Helper method to launch the {@link DetailActivity} without sending
     * a journal id to it. This will allow the user to create a new journal.
     */
    private void launchNewJournal() {
        Intent i = new Intent(this, DetailActivity.class);
        startActivity(i);
    }
}
