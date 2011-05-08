package ru.thevery.smartadapters.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ru.thevery.smartadapters.annotations.InjectView;
import ru.thevery.smartadapters.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author Ildar Karimov
 */
public abstract class SmartBaseAdapter<THolder> extends BaseAdapter {
    protected ArrayList<ViewMembersInjector> viewsForInjection = new ArrayList<ViewMembersInjector>();
    private Context context;
    private int layoutId;
    private LayoutInflater inflater;

    private void init(Class<?> holderType) {
        inflater = LayoutInflater.from(context);
        prepareFields(holderType);
    }

    public SmartBaseAdapter(Context context, int layoutId, Class<?> holderType) {
        this.context = context;
        this.layoutId = layoutId;
        init(holderType);
    }

    protected abstract THolder createNewViewHolder(int position, ViewGroup parent);

    protected abstract void bindView(int position, THolder holder);

    protected View createNewView(int position, ViewGroup parent) {
        return inflater.inflate(layoutId, null, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        THolder holder;
        if (convertView == null) {
            convertView = createNewView(position, parent);
            holder = createNewViewHolder(position, parent);
            convertView.setTag(holder);
            injectViews(holder, convertView);
        } else {
            holder = (THolder) convertView.getTag();
        }
        bindView(position, holder);
        return convertView;
    }

    public void injectViews(THolder holder, View view) {
        for (ViewMembersInjector viewMembersInjector : viewsForInjection) {
            viewMembersInjector.reallyInjectMembers(holder, view);
        }
    }

    private void prepareFields(Class clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectView.class)) {
                field.setAccessible(true);
                viewsForInjection.add(new ViewMembersInjector(field, field.getAnnotation(InjectView.class)));
            }
        }
    }

    class ViewMembersInjector {
        protected Field field;
        protected InjectView annotation;

        public ViewMembersInjector(Field field, InjectView annotation) {
            this.field = field;
            this.annotation = annotation;
        }

        public void reallyInjectMembers(Object holder, View view) {
            Object value = null;
            try {
                value = view.findViewById(annotation.value());
                if (value == null && field.getAnnotation(Nullable.class) == null) {
                    throw new NullPointerException(String.format("Can't inject null value into %s.%s when field is not @Nullable", field.getDeclaringClass(), field
                            .getName()));
                }
                field.set(holder, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException f) {
                throw new IllegalArgumentException(String.format("Can't assign %s value %s to %s field %s", value != null ? value.getClass() : "(null)", value,
                        field.getType(), field.getName()));
            }
        }
    }
}