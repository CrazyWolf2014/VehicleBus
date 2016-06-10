package com.hp.hpl.sparta;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

class NodeListWithPosition {
    private static final Integer EIGHT;
    private static final Integer FIVE;
    private static final Integer FOUR;
    private static final Integer NINE;
    private static final Integer ONE;
    private static final Integer SEVEN;
    private static final Integer SIX;
    private static final Integer TEN;
    private static final Integer THREE;
    private static final Integer TWO;
    private Hashtable positions_;
    private final Vector vector_;

    static {
        ONE = new Integer(1);
        TWO = new Integer(2);
        THREE = new Integer(3);
        FOUR = new Integer(4);
        FIVE = new Integer(5);
        SIX = new Integer(6);
        SEVEN = new Integer(7);
        EIGHT = new Integer(8);
        NINE = new Integer(9);
        TEN = new Integer(10);
    }

    NodeListWithPosition() {
        this.vector_ = new Vector();
        this.positions_ = new Hashtable();
    }

    private static Integer identity(Node node) {
        return new Integer(System.identityHashCode(node));
    }

    void add(Node node, int i) {
        Object obj;
        this.vector_.addElement(node);
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                obj = ONE;
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                obj = TWO;
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                obj = THREE;
                break;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                obj = FOUR;
                break;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                obj = FIVE;
                break;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                obj = SIX;
                break;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                obj = SEVEN;
                break;
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                obj = EIGHT;
                break;
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                obj = NINE;
                break;
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                obj = TEN;
                break;
            default:
                obj = new Integer(i);
                break;
        }
        this.positions_.put(identity(node), obj);
    }

    void add(String str) {
        this.vector_.addElement(str);
    }

    Enumeration iterator() {
        return this.vector_.elements();
    }

    int position(Node node) {
        return ((Integer) this.positions_.get(identity(node))).intValue();
    }

    void removeAllElements() {
        this.vector_.removeAllElements();
        this.positions_.clear();
    }

    public String toString() {
        try {
            StringBuffer stringBuffer = new StringBuffer("{ ");
            Enumeration elements = this.vector_.elements();
            while (elements.hasMoreElements()) {
                Object nextElement = elements.nextElement();
                if (nextElement instanceof String) {
                    stringBuffer.append(new StringBuffer().append("String(").append(nextElement).append(") ").toString());
                } else {
                    Node node = (Node) nextElement;
                    stringBuffer.append(new StringBuffer().append("Node(").append(node.toXml()).append(")[").append(this.positions_.get(identity(node))).append("] ").toString());
                }
            }
            stringBuffer.append("}");
            return stringBuffer.toString();
        } catch (Throwable e) {
            return e.toString();
        }
    }
}
