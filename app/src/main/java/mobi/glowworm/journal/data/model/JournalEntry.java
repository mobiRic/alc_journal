package mobi.glowworm.journal.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@SuppressWarnings("unused")
@Entity(tableName = "journal_entry")
public class JournalEntry {

    /**
     * The user id for a local user who has not logged into the app.
     * Once a user signs in with Google Authentication this id will
     * be updated.
     */
    public static final int LOCAL_USER_NOT_LOGGED_IN = -1;

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;

    @ColumnInfo(name = "user_id")
    private int userId = LOCAL_USER_NOT_LOGGED_IN;

    private Date date;

    @Ignore
    public JournalEntry(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public JournalEntry(int id, String title, String description, Date date) {
        this(title, description, date);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
