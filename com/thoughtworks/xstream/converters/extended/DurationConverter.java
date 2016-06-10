package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

public class DurationConverter extends AbstractSingleValueConverter {
    private final DatatypeFactory factory;

    /* renamed from: com.thoughtworks.xstream.converters.extended.DurationConverter.1 */
    class C08811 {
        C08811() {
        }

        DatatypeFactory getFactory() {
            try {
                return DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                return null;
            }
        }
    }

    public DurationConverter() {
        this(new C08811().getFactory());
    }

    public DurationConverter(DatatypeFactory factory) {
        this.factory = factory;
    }

    public boolean canConvert(Class c) {
        return this.factory != null && Duration.class.isAssignableFrom(c);
    }

    public Object fromString(String s) {
        return this.factory.newDuration(s);
    }
}
