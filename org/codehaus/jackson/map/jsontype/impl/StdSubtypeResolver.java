package org.codehaus.jackson.map.jsontype.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;

public class StdSubtypeResolver extends SubtypeResolver {
    protected LinkedHashSet<NamedType> _registeredSubtypes;

    public void registerSubtypes(NamedType... types) {
        if (this._registeredSubtypes == null) {
            this._registeredSubtypes = new LinkedHashSet();
        }
        for (NamedType type : types) {
            this._registeredSubtypes.add(type);
        }
    }

    public void registerSubtypes(Class<?>... classes) {
        NamedType[] types = new NamedType[classes.length];
        int len = classes.length;
        for (int i = 0; i < len; i++) {
            types[i] = new NamedType(classes[i]);
        }
        registerSubtypes(types);
    }

    public Collection<NamedType> collectAndResolveSubtypes(AnnotatedMember property, MapperConfig<?> config, AnnotationIntrospector ai) {
        Collection<NamedType> st = ai.findSubtypes(property);
        if (st == null) {
            st = new ArrayList();
        }
        st.add(new NamedType(property.getRawType(), null));
        return _collectAndResolve(property, config, ai, st);
    }

    public Collection<NamedType> collectAndResolveSubtypes(AnnotatedClass type, MapperConfig<?> config, AnnotationIntrospector ai) {
        HashMap<NamedType, NamedType> subtypes = new HashMap();
        if (this._registeredSubtypes != null) {
            Class<?> rawBase = type.getRawType();
            Iterator i$ = this._registeredSubtypes.iterator();
            while (i$.hasNext()) {
                NamedType subtype = (NamedType) i$.next();
                if (rawBase.isAssignableFrom(subtype.getType())) {
                    _collectAndResolve(AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), ai, config), subtype, config, ai, subtypes);
                }
            }
        }
        _collectAndResolve(type, new NamedType(type.getRawType(), null), config, ai, subtypes);
        return new ArrayList(subtypes.values());
    }

    protected Collection<NamedType> _collectAndResolve(AnnotatedMember property, MapperConfig<?> config, AnnotationIntrospector ai, Collection<NamedType> subtypeList) {
        HashSet<NamedType> seen = new HashSet(subtypeList);
        ArrayList<NamedType> subtypes = new ArrayList(subtypeList);
        for (int i = 0; i < subtypes.size(); i++) {
            NamedType type = (NamedType) subtypes.get(i);
            AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(type.getType(), ai, config);
            if (!type.hasName()) {
                type.setName(ai.findTypeName(ac));
            }
            List<NamedType> moreTypes = ai.findSubtypes(ac);
            if (moreTypes != null) {
                for (NamedType t2 : moreTypes) {
                    if (seen.add(t2)) {
                        subtypes.add(t2);
                    }
                }
            }
        }
        return subtypes;
    }

    protected void _collectAndResolve(AnnotatedClass annotatedType, NamedType namedType, MapperConfig<?> config, AnnotationIntrospector ai, HashMap<NamedType, NamedType> collectedSubtypes) {
        if (!namedType.hasName()) {
            String name = ai.findTypeName(annotatedType);
            if (name != null) {
                namedType = new NamedType(namedType.getType(), name);
            }
        }
        if (!collectedSubtypes.containsKey(namedType)) {
            collectedSubtypes.put(namedType, namedType);
            Collection<NamedType> st = ai.findSubtypes(annotatedType);
            if (st != null && !st.isEmpty()) {
                for (NamedType subtype : st) {
                    NamedType subtype2;
                    AnnotatedClass subtypeClass = AnnotatedClass.constructWithoutSuperTypes(subtype2.getType(), ai, config);
                    if (!subtype2.hasName()) {
                        subtype2 = new NamedType(subtype2.getType(), ai.findTypeName(subtypeClass));
                    }
                    _collectAndResolve(subtypeClass, subtype2, config, ai, collectedSubtypes);
                }
            }
        } else if (namedType.hasName() && !((NamedType) collectedSubtypes.get(namedType)).hasName()) {
            collectedSubtypes.put(namedType, namedType);
        }
    }
}
