package mobi.glowworm.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mobi.glowworm.journal.data.model.JournalEntry;
import mobi.glowworm.lib.ui.widget.EmptyRecyclerView;

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
public class JournalListActivity extends AppCompatActivity implements JournalAdapter.OnJournalClickListener {

    @NonNull
    private EmptyRecyclerView recyclerView;

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
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView.setEmptyView(findViewById(R.id.journal_list_empty_view));
        recyclerView.setAdapter(new JournalAdapter(null, this));
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
