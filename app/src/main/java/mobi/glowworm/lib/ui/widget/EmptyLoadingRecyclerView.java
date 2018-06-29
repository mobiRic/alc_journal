/*
 * Copyright (C) 2015 Glowworm Software
 * Copyright (C) 2014 Nizamutdinov Adel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// based on https://gist.github.com/adelnizamutdinov/31c8f054d1af4588dc5c

package mobi.glowworm.lib.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * {@link RecyclerView} that can show different views
 * when it has no items to display, or when it is loading data.
 */
public class EmptyLoadingRecyclerView extends RecyclerView {

    /**
     * View in the current layout that will be displayed when this
     * {@link EmptyLoadingRecyclerView} has no items to display.
     * <p>
     * It will not be displayed if {@link #loadingView} is visible.
     */
    @Nullable
    private View emptyView;
    /**
     * View in the current layout that will be displayed when this
     * {@link EmptyLoadingRecyclerView} is loading data.
     */
    @Nullable
    private View loadingView;
    /**
     * Flag indicating if the {@link #loadingView} should be displayed
     * or not.
     */
    private boolean isLoading = false;

    public EmptyLoadingRecyclerView(Context context) {
        super(context);
    }

    public EmptyLoadingRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyLoadingRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        refreshUi();
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        super.swapAdapter(adapter, removeAndRecycleExistingViews);
        refreshUi();
    }

    @NonNull
    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            refreshUi();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            refreshUi();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            refreshUi();
        }
    };

    /**
     * Sets the view that will be displayed when this
     * {@link EmptyLoadingRecyclerView} has no items to display.
     *
     * @param emptyView a {@link View} in the current layout
     */
    public void setEmptyView(@Nullable View emptyView) {
        if (this.emptyView != null) {
            this.emptyView.setVisibility(GONE);
        }

        this.emptyView = emptyView;
        refreshUi();
    }

    /**
     * Sets the view that will be displayed when this
     * {@link EmptyLoadingRecyclerView} is loading data.
     *
     * @param loadingView a {@link View} in the current layout
     */
    public void setLoadingView(@Nullable View loadingView) {
        if (this.loadingView != null) {
            this.loadingView.setVisibility(GONE);
        }

        this.loadingView = loadingView;
        refreshUi();
    }

    /**
     * Sets the loading status of this {@link EmptyLoadingRecyclerView}.
     * <p>
     * If a {@link #loadingView} has been set, it will be displayed
     * based on this flag.
     *
     * @param isLoading <code>true</code> to show the {@link #loadingView}; <code>false</code> to hide it
     */
    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        refreshUi();
    }

    /**
     * Updates view visibility based on items displayed
     * and loading status.
     */
    private void refreshUi() {

        // loading takes precedence
        if (loadingView != null && isLoading) {
            show(loadingView);
            hide(emptyView);
            return;
        } else if (!isLoading) {
            hide(loadingView);
        }

        // check if empty
        if ((getAdapter() == null) || (getAdapter().getItemCount() == 0)) {
            show(emptyView);
        }
    }

    /**
     * Helper method to hide a view if it is not null.
     *
     * @param view view to hide (can be null)
     */
    private void hide(@Nullable View view) {
        if (view == null) {
            return;
        }

        if (view.getVisibility() != GONE) {
            view.setVisibility(GONE);
        }
    }

    /**
     * Helper method to show a view if it is not null.
     *
     * @param view view to show (can be null)
     */
    private void show(@Nullable View view) {
        if (view == null) {
            return;
        }

        if (view.getVisibility() != VISIBLE) {
            view.setVisibility(VISIBLE);
        }
    }

}