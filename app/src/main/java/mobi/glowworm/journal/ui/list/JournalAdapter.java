package mobi.glowworm.journal.ui.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mobi.glowworm.journal.R;
import mobi.glowworm.journal.data.model.JournalEntry;
import mobi.glowworm.journal.ui.details.DetailActivity;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {

    @Nullable
    private final List<JournalEntry> items;
    @NonNull
    private final OnJournalClickListener journalClickListener;

    public JournalAdapter(@Nullable List<JournalEntry> items, @NonNull OnJournalClickListener journalClickListener) {
        this.items = items;
        this.journalClickListener = journalClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        @SuppressWarnings("ConstantConditions")
        JournalEntry entry = items.get(position);

        holder.tvTitle.setText(entry.getTitle());
        holder.tvTitle.setVisibility(TextUtils.isEmpty(entry.getTitle()) ? View.GONE : View.VISIBLE);
        holder.tvSummary.setText(entry.getDescription());
        holder.tvSummary.setVisibility(TextUtils.isEmpty(entry.getDescription()) ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO change item id to be a long as per previous todo
                journalClickListener.onJournalClicked((int) getItemId(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return (items != null) ? items.get(position).getId() : DetailActivity.NEW_JOURNAL;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final TextView tvSummary;

        ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_journal_title);
            tvSummary = view.findViewById(R.id.tv_journal_description);
        }
    }

    public interface OnJournalClickListener {
        /**
         * Notifies a listener when a {@link JournalEntry} has been selected
         * in the list.
         *
         * @param journalId id of the journal that was clicked
         */
        void onJournalClicked(int journalId);
    }

}