package mobi.glowworm.journal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mobi.glowworm.journal.data.model.JournalEntry;
import mobi.glowworm.journal.dummy.DummyContent;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {

    private final List<DummyContent.DummyItem> mValues;
    @NonNull
    private final OnJournalClickListener journalClickListener;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
            journalClickListener.onJournalClicked(Integer.parseInt(item.id));
        }
    };

    public JournalAdapter(List<DummyContent.DummyItem> items, @NonNull OnJournalClickListener journalClickListener) {
        mValues = items;
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
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.id_text);
            mContentView = view.findViewById(R.id.content);
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