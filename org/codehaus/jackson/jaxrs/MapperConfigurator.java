package org.codehaus.jackson.jaxrs;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.util.ArrayList;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class MapperConfigurator {
    protected Annotations[] _defaultAnnotationsToUse;
    protected ObjectMapper _defaultMapper;
    protected Class<? extends AnnotationIntrospector> _jaxbIntrospectorClass;
    protected ObjectMapper _mapper;

    /* renamed from: org.codehaus.jackson.jaxrs.MapperConfigurator.1 */
    static /* synthetic */ class C09381 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$jaxrs$Annotations;

        static {
            $SwitchMap$org$codehaus$jackson$jaxrs$Annotations = new int[Annotations.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$jaxrs$Annotations[Annotations.JACKSON.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$jaxrs$Annotations[Annotations.JAXB.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public MapperConfigurator(ObjectMapper mapper, Annotations[] defAnnotations) {
        this._mapper = mapper;
        this._defaultAnnotationsToUse = defAnnotations;
    }

    public synchronized ObjectMapper getConfiguredMapper() {
        return this._mapper;
    }

    public synchronized ObjectMapper getDefaultMapper() {
        if (this._defaultMapper == null) {
            this._defaultMapper = new ObjectMapper();
            _setAnnotations(this._defaultMapper, this._defaultAnnotationsToUse);
        }
        return this._defaultMapper;
    }

    public synchronized void setMapper(ObjectMapper m) {
        this._mapper = m;
    }

    public synchronized void setAnnotationsToUse(Annotations[] annotationsToUse) {
        _setAnnotations(mapper(), annotationsToUse);
    }

    public synchronized void configure(Feature f, boolean state) {
        mapper().configure(f, state);
    }

    public synchronized void configure(SerializationConfig.Feature f, boolean state) {
        mapper().configure(f, state);
    }

    public synchronized void configure(JsonParser.Feature f, boolean state) {
        mapper().configure(f, state);
    }

    public synchronized void configure(JsonGenerator.Feature f, boolean state) {
        mapper().configure(f, state);
    }

    protected ObjectMapper mapper() {
        if (this._mapper == null) {
            this._mapper = new ObjectMapper();
            _setAnnotations(this._mapper, this._defaultAnnotationsToUse);
        }
        return this._mapper;
    }

    protected void _setAnnotations(ObjectMapper mapper, Annotations[] annotationsToUse) {
        AnnotationIntrospector intr;
        if (annotationsToUse == null || annotationsToUse.length == 0) {
            intr = AnnotationIntrospector.nopInstance();
        } else {
            intr = _resolveIntrospectors(annotationsToUse);
        }
        mapper.getDeserializationConfig().setAnnotationIntrospector(intr);
        mapper.getSerializationConfig().setAnnotationIntrospector(intr);
    }

    protected AnnotationIntrospector _resolveIntrospectors(Annotations[] annotationsToUse) {
        ArrayList<AnnotationIntrospector> intr = new ArrayList();
        for (Annotations a : annotationsToUse) {
            if (a != null) {
                intr.add(_resolveIntrospector(a));
            }
        }
        if (intr.size() == 0) {
            return AnnotationIntrospector.nopInstance();
        }
        AnnotationIntrospector curr = (AnnotationIntrospector) intr.get(0);
        int len = intr.size();
        for (int i = 1; i < len; i++) {
            curr = AnnotationIntrospector.pair(curr, (AnnotationIntrospector) intr.get(i));
        }
        return curr;
    }

    protected AnnotationIntrospector _resolveIntrospector(Annotations ann) {
        switch (C09381.$SwitchMap$org$codehaus$jackson$jaxrs$Annotations[ann.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return new JacksonAnnotationIntrospector();
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                try {
                    if (this._jaxbIntrospectorClass == null) {
                        this._jaxbIntrospectorClass = JaxbAnnotationIntrospector.class;
                    }
                    return (AnnotationIntrospector) this._jaxbIntrospectorClass.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException("Failed to instantiate JaxbAnnotationIntrospector: " + e.getMessage(), e);
                }
            default:
                throw new IllegalStateException();
        }
    }
}
