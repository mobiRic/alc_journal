package mobi.glowworm.journal;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.todolist.AppExecutors;

import java.util.Date;
import java.util.concurrent.Executor;

import mobi.glowworm.journal.data.Db;
import mobi.glowworm.journal.data.model.JournalEntry;
import mobi.glowworm.lib.utils.debug.Dbug;

public class DetailActivity extends AppCompatActivity {

    public static final String KEY_JOURNAL_ID = "KEY_JOURNAL_ID";
    public static final int NEW_JOURNAL = -1;

    private int journalId = NEW_JOURNAL;
    private Db db;
    private TextView tvTitle;
    private TextView tvDesc;
    private Date date;
    @Nullable
    private JournalEntry journalExistingInDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        date = new Date();
        tvTitle = findViewById(R.id.tv_journal_detail_title);
        tvDesc = findViewById(R.id.tv_journal_detail_description);

        db = Db.getInstance(this);

        // check for rotation
        if (savedInstanceState != null) {
            journalId = savedInstanceState.getInt(KEY_JOURNAL_ID, NEW_JOURNAL);
        }

        // check for existing journal
        Intent i = getIntent();
        if ((i != null) && i.hasExtra(KEY_JOURNAL_ID)) {

            // ensure we launched the screen with an existing journal
            if (journalId == NEW_JOURNAL) {
                journalId = i.getIntExtra(KEY_JOURNAL_ID, NEW_JOURNAL);

                DetailViewModelFactory vmFactory = new DetailViewModelFactory(db, journalId);
                DetailViewModel viewModel = ViewModelProviders.of(this, vmFactory).get(DetailViewModel.class);
                final LiveData<JournalEntry> liveJournal = viewModel.getJournal();
                liveJournal.observe(this, new Observer<JournalEntry>() {
                    @Override
                    public void onChanged(@Nullable JournalEntry journal) {
                        // no need to listen for further changes to the data
                        liveJournal.removeObserver(this);
                        journalExistingInDatabase = journal;
                        updateUi(journal);
                    }
                });
            }
        }

        if (journalId == NEW_JOURNAL) {
            initUiForNewJournal();
        } else {
            initUiForExistingJournal();
        }
    }

    /**
     * Automatically save changes to the journal entry.
     * <p>
     * This provides a much more natural user experience than
     * requiring the user to save changes manually.
     */
    @Override
    protected void onStop() {
        Dbug.log("Checking for changes");

        String title = tvTitle.getText().toString();
        String description = tvDesc.getText().toString();
        final JournalEntry journal = new JournalEntry(title, description, date);

        // check for updates
        if (journal.equals(journalExistingInDatabase)) {
            Dbug.log("No changes to journal entry");
        } else {
            Executor dbExecutor = AppExecutors.getInstance().diskIO();
            dbExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    if (journalId == NEW_JOURNAL) {
                        // do not add empty journals to the database
                        if (!journal.isEmpty()) {
                            // TODO update id fields to use long - not going to be an issue until we have many journals
                            journalId = (int) db.dao().insertJournal(journal);
                            Dbug.log("Created new journal [", journalId, "]");
                        } else {
                            Dbug.log("Ignoring blank journal");
                        }

                        journalExistingInDatabase = journal;
                    } else {
                        journal.setId(journalId);
                        if (journal.isEmpty()) {
                            // delete journals from the database if the contents has been deleted
                            db.dao().deleteJournal(journal);
                            Dbug.log("Deleted journal [", journalId, "]");

                            journalId = NEW_JOURNAL;
                            journalExistingInDatabase = null;
                        } else {
                            db.dao().updateJournal(journal);
                            Dbug.log("Updated journal [", journalId, "]");

                            journalExistingInDatabase = journal;
                        }
                    }
                }
            });
        }
        super.onStop();
    }

    private void initUiForExistingJournal() {
        // TODO add code to update any onscreen elements that will be different for an existing vs new journal
        Dbug.log("Existing journal [", journalId, "]");
    }

    private void initUiForNewJournal() {
        // TODO add code to update any onscreen elements that will be different for an existing vs new journal
        Dbug.log("New journal");
    }

    private void updateUi(@Nullable JournalEntry updatedJournal) {
        if (updatedJournal == null) {
            // create blank entry
            journalId = NEW_JOURNAL;
            date = new Date();
            tvTitle.clearComposingText();
            tvDesc.clearComposingText();
            return;
        }

        date = updatedJournal.getDate();
        tvTitle.setText(updatedJournal.getTitle());
        tvDesc.setText(updatedJournal.getDescription());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_JOURNAL_ID, journalId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.action_done_editing: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO do we need to implement a confirmation dialog?
        super.onBackPressed();
    }
}
