package org.codehaus.jackson.map.ser;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public abstract class FilteredBeanPropertyWriter {

    private static final class MultiView extends BeanPropertyWriter {
        protected final Class<?>[] _views;

        protected MultiView(BeanPropertyWriter base, Class<?>[] views) {
            super(base);
            this._views = views;
        }

        protected MultiView(MultiView fromView, JsonSerializer<Object> ser) {
            super(fromView, ser);
            this._views = fromView._views;
        }

        public BeanPropertyWriter withSerializer(JsonSerializer<Object> ser) {
            return new MultiView(this, (JsonSerializer) ser);
        }

        public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
            Class<?> activeView = prov.getSerializationView();
            if (activeView != null) {
                int i = 0;
                int len = this._views.length;
                while (i < len && !this._views[i].isAssignableFrom(activeView)) {
                    i++;
                }
                if (i == len) {
                    return;
                }
            }
            super.serializeAsField(bean, jgen, prov);
        }
    }

    private static final class SingleView extends BeanPropertyWriter {
        protected final Class<?> _view;

        protected SingleView(BeanPropertyWriter base, Class<?> view) {
            super(base);
            this._view = view;
        }

        protected SingleView(SingleView fromView, JsonSerializer<Object> ser) {
            super(fromView, ser);
            this._view = fromView._view;
        }

        public BeanPropertyWriter withSerializer(JsonSerializer<Object> ser) {
            return new SingleView(this, (JsonSerializer) ser);
        }

        public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
            Class<?> activeView = prov.getSerializationView();
            if (activeView == null || this._view.isAssignableFrom(activeView)) {
                super.serializeAsField(bean, jgen, prov);
            }
        }
    }

    public static BeanPropertyWriter constructViewBased(BeanPropertyWriter base, Class<?>[] viewsToIncludeIn) {
        if (viewsToIncludeIn.length == 1) {
            return new SingleView(base, viewsToIncludeIn[0]);
        }
        return new MultiView(base, (Class[]) viewsToIncludeIn);
    }
}
