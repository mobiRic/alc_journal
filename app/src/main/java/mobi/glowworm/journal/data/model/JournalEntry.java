package mobi.glowworm.journal.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.text.TextUtils;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalEntry)) return false;

        JournalEntry that = (JournalEntry) o;

        if (getId() != that.getId()) return false;
        if (getUserId() != that.getUserId()) return false;
        if (getTitle() != null ? !getTitle().equals(that.getTitle()) : that.getTitle() != null) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null) return false;
        return getDate() != null ? getDate().equals(that.getDate()) : that.getDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getTitle() != null ? getTitle().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + getUserId();
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        return result;
    }

    /**
     * Checks if this journal has any entry data to save.
     * <p>
     * This is based on having a valid title or description.
     *
     * @return <code>true</code> if the journal contains any data
     */
    public boolean isEmpty() {
        return TextUtils.isEmpty(title) && TextUtils.isEmpty(description);
    }
}
