package ru.thevery.smartadapters.adapters;

import android.content.Context;

import java.util.Collections;
import java.util.List;

/**
 * @author Ildar Karimov
 */
public abstract class SmartArrayAdapter<THolder, TData> extends SmartBaseAdapter<THolder> {
    private List<TData> items = Collections.emptyList();

    public SmartArrayAdapter(Context context, int layoutId, Class<?> holderType, List<TData> items) {
        super(context, layoutId, holderType);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public TData getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}