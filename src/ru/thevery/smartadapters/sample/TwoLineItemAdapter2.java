package ru.thevery.smartadapters.sample;

import android.content.Context;
import android.widget.TextView;
import ru.thevery.smartadapters.R;
import ru.thevery.smartadapters.adapters.SmartArrayAdapter2;
import ru.thevery.smartadapters.annotations.InjectView;

import java.util.List;

public class TwoLineItemAdapter2 extends SmartArrayAdapter2<TwoLineItemAdapter2.TwoLineItemHolder, TwoLineItem> {
    public TwoLineItemAdapter2(Context context, int layoutId, Class<?> holderType, List<TwoLineItem> items) {
        super(context, layoutId, holderType, items);
    }

    @Override
    protected TwoLineItemHolder createNewViewHolder(int position) {
        return new TwoLineItemHolder();
    }

    @Override
    protected void bindView(int position, TwoLineItemHolder twoLineItemHolder) {
        TwoLineItem twoLineItem = getItem(position);
        twoLineItemHolder.firstNameView.setText(twoLineItem.firstName);
        twoLineItemHolder.lastNameView.setText(twoLineItem.lastName);
    }

    public static class TwoLineItemHolder {
        @InjectView(R.id.first_name)
        public TextView firstNameView;

        @InjectView(R.id.last_name)
        public TextView lastNameView;
    }
}
