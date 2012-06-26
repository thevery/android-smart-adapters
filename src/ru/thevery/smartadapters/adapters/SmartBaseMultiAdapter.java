package ru.thevery.smartadapters.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ru.thevery.smartadapters.annotations.InjectView;
import ru.thevery.smartadapters.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Ildar Karimov
 */
public abstract class SmartBaseMultiAdapter extends BaseAdapter {
    protected Map<Class, List<ViewMembersInjector>> clazz2views = new HashMap<Class, List<ViewMembersInjector>>();
    protected Context context;
    protected LayoutInflater inflater;

    private boolean inited = false;

    //must be called after ctor because holderType(s) are not known yet
    private void initHoldersLazy() {
        int count = getViewTypeCount();
        for (int i = 0; i < count; i++) {
            Class<?> clazz = getHolderClass(i);
            prepareFields(clazz);
        }
        inited = true;
    }

    public SmartBaseMultiAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    protected abstract int getViewType(int position);

    protected abstract int getLayoutForType(int type);

    protected abstract Class<?> getHolderClass(int type);

    protected abstract Object createNewViewHolder(int type);

    protected abstract void bindView(int type, int position, Object holder);

    protected View createNewView(int type, ViewGroup parent) {
        return inflater.inflate(getLayoutForType(type), parent, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (!inited) {
            initHoldersLazy();
        }
        Object holder;
        int viewType = getViewType(position);
        if (convertView == null) {
            convertView = createNewView(viewType, parent);
            holder = createNewViewHolder(viewType);
            convertView.setTag(holder);
            injectViews(holder, convertView);
        } else {
            holder = convertView.getTag();
        }
        bindView(viewType, position, holder);
        return convertView;
    }

    public void injectViews(Object holder, View view) {
        for (ViewMembersInjector viewMembersInjector : clazz2views.get(holder.getClass())) {
            viewMembersInjector.reallyInjectMembers(holder, view);
        }
    }

    private void prepareFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<ViewMembersInjector> viewsForInjection = new ArrayList<ViewMembersInjector>(fields.length);
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectView.class)) {
                field.setAccessible(true);
                viewsForInjection.add(new ViewMembersInjector(field, field.getAnnotation(InjectView.class)));
            }
        }
        clazz2views.put(clazz, viewsForInjection);
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