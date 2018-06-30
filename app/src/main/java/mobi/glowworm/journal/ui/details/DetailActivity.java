package mobi.glowworm.journal.ui.details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.todolist.AppExecutors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;

import mobi.glowworm.journal.R;
import mobi.glowworm.journal.data.model.JournalEntry;
import mobi.glowworm.journal.ui.ABaseActivity;
import mobi.glowworm.journal.utils.DateTime;
import mobi.glowworm.lib.utils.debug.Dbug;

public class DetailActivity extends ABaseActivity {

    public static final String KEY_JOURNAL_ID = "KEY_JOURNAL_ID";
    public static final int NEW_JOURNAL = -1;

    private int journalId = NEW_JOURNAL;
    private TextView tvDay;
    private TextView tvTime;
    private EditText tvTitle;
    private EditText tvDesc;
    private Date date;
    @Nullable
    private JournalEntry journalExistingInDatabase;

    private SimpleDateFormat formatDay;
    private SimpleDateFormat formatTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        date = new Date();
        tvDay = findViewById(R.id.tv_journal_detail_day);
        tvTime = findViewById(R.id.tv_journal_detail_time);
        tvTitle = findViewById(R.id.tv_journal_detail_title);
        tvDesc = findViewById(R.id.tv_journal_detail_description);

        formatDay = DateTime.getFormatDay(this);
        formatTime = DateTime.getFormatTime(this);

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

                DetailViewModelFactory vmFactory = new DetailViewModelFactory(getDatabase(), journalId);
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

        // check for updates
        final JournalEntry journal = createJournalFromUi();
        if (journal.equals(journalExistingInDatabase)) {
            Dbug.log("No changes to journal entry");
        } else {
            Executor dbExecutor = AppExecutors.getInstance().diskIO();
            dbExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    if (journal.getId() == NEW_JOURNAL) {
                        // do not add empty journals to the database
                        if (!journal.isEmpty()) {
                            // TODO update id fields to use long - not going to be an issue until we have many journals
                            journalId = (int) getDao().insertJournal(journal);
                            Dbug.log("Created new journal [", journalId, "]");
                        } else {
                            Dbug.log("Ignoring blank journal");
                        }

                        journalExistingInDatabase = journal;
                    } else {
                        if (journal.isEmpty()) {
                            // delete journals from the database if the contents has been deleted
                            deleteJournal(journal);
                        } else {
                            getDao().updateJournal(journal);
                            Dbug.log("Updated journal [", journalId, "]");

                            journalExistingInDatabase = journal;
                        }
                    }
                }
            });
        }
        super.onStop();
    }

    /**
     * Creates a new Journal representing the current screen.
     *
     * @return {@link JournalEntry} object being edited
     */
    @NonNull
    private JournalEntry createJournalFromUi() {
        String title = tvTitle.getText().toString();
        String description = tvDesc.getText().toString();
        JournalEntry journal = new JournalEntry(getCurrentUserId(), title, description, date);
        if (journalId != NEW_JOURNAL) {
            journal.setId(journalId);
        }
        return journal;
    }

    /**
     * Helper method to delete a given journal from the database.
     *
     * @param journal {@link JournalEntry} containing the correct ID to delete
     */
    private void deleteJournal(JournalEntry journal) {
        getDao().deleteJournal(journal);
        Dbug.log("Deleted journal [", journalId, "]");

        journalId = NEW_JOURNAL;
        journalExistingInDatabase = null;
    }

    private void initUiForExistingJournal() {
        // TODO add code to update any onscreen elements that will be different for an existing vs new journal
        Dbug.log("Existing journal [", journalId, "]");
    }

    private void initUiForNewJournal() {
        Dbug.log("New journal");
        updateUiForDate(date);
        tvTitle.requestFocus();

        // show keyboard - delay is required to make it work
        // TODO find the right way to get the keyboard to show 
        tvTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(tvTitle, InputMethodManager.SHOW_FORCED);
            }
        }, 50);
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
        updateUiForDate(date);
        String title = updatedJournal.getTitle();
        tvTitle.setText(title);
        tvTitle.setSelection(title != null ? title.length() : 0);

        String description = updatedJournal.getDescription();
        tvDesc.setText(description);
        tvDesc.setSelection(title != null ? description.length() : 0);
    }

    private void updateUiForDate(Date date) {
        tvDay.setText(formatDay.format(date));
        tvTime.setText(formatTime.format(date));
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
            case R.id.action_delete: {
                onDeletePressed();
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

    /**
     * Handles the confirmation flow when the user selects to delete a journal entry with the menu option.
     */
    private void onDeletePressed() {
        alert(
                R.string.alert_delete_journal_title,
                R.string.alert_delete_journal_message,
                android.R.drawable.ic_delete,
                R.string.alert_delete_journal_btn_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Executor dbExecutor = AppExecutors.getInstance().diskIO();
                        dbExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                JournalEntry journal = createJournalFromUi();
                                deleteJournal(journal);
                            }
                        });

                        finish();
                    }
                },
                R.string.alert_delete_journal_btn_negative, null);
    }
}
