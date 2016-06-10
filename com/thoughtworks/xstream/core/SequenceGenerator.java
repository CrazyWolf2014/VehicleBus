package com.thoughtworks.xstream.core;

import com.thoughtworks.xstream.core.ReferenceByIdMarshaller.IDGenerator;

public class SequenceGenerator implements IDGenerator {
    private int counter;

    public SequenceGenerator(int startsAt) {
        this.counter = startsAt;
    }

    public String next(Object item) {
        int i = this.counter;
        this.counter = i + 1;
        return String.valueOf(i);
    }
}
