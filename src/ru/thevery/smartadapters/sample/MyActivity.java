package ru.thevery.smartadapters.sample;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;
import ru.thevery.smartadapters.R;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
    public static final int COUNT = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<TwoLineItem> sampleData = new ArrayList<TwoLineItem>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            sampleData.add(new TwoLineItem("first " + i, "last " + i));
        }

        ListView listView = new ListView(this);
//        TwoLineItemAdapter adapter = new TwoLineItemAdapter(this, R.layout.two_line_list_item,
//                TwoLineItemAdapter.TwoLineItemHolder.class, sampleData);
//        listView.setAdapter(adapter);
        TwoLineItemAdapter2 adapter2 = new TwoLineItemAdapter2(this, R.layout.two_line_list_item,
                TwoLineItemAdapter2.TwoLineItemHolder.class, sampleData);
        listView.setAdapter(adapter2);
        setContentView(listView);
    }
}
