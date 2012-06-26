package ru.thevery.smartadapters.adapters;

import android.content.Context;

import java.util.List;

/**
 * @author Ildar Karimov
 */
public abstract class SmartArrayAdapter2<THolder, TData> extends SmartBaseMultiAdapter {

    private final int layoutId;
    private final Class<?> holderType;
    private final List<TData> items;

    public SmartArrayAdapter2(Context context, int layoutId, Class<?> holderType, List<TData> items) {
        super(context);
        this.context = context;
        this.layoutId = layoutId;
        this.holderType = holderType;
        this.items = items;
    }

    @Override
    protected abstract THolder createNewViewHolder(int type);

    protected abstract void bindView(int position, THolder holder);

    @Override
    protected Class<?> getHolderClass(int type) {
        return holderType;
    }

    @Override
    protected int getViewType(int position) {
        return 1;
    }

    @Override
    protected int getLayoutForType(int type) {
        return layoutId;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void bindView(int type, int position, Object holder) {
        bindView(position, (THolder) holder);
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