package org.codehaus.jackson.annotate;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

@JacksonAnnotation
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonAutoDetect {

    /* renamed from: org.codehaus.jackson.annotate.JsonAutoDetect.1 */
    static /* synthetic */ class C09311 {
        static final /* synthetic */ int[] f1671x87517c95;

        static {
            f1671x87517c95 = new int[Visibility.values().length];
            try {
                f1671x87517c95[Visibility.ANY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1671x87517c95[Visibility.NONE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1671x87517c95[Visibility.NON_PRIVATE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1671x87517c95[Visibility.PROTECTED_AND_PUBLIC.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f1671x87517c95[Visibility.PUBLIC_ONLY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum Visibility {
        ANY,
        NON_PRIVATE,
        PROTECTED_AND_PUBLIC,
        PUBLIC_ONLY,
        NONE,
        DEFAULT;

        public boolean isVisible(Member m) {
            switch (C09311.f1671x87517c95[ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    return true;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    return false;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    if (Modifier.isPrivate(m.getModifiers())) {
                        return false;
                    }
                    return true;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    if (Modifier.isProtected(m.getModifiers())) {
                        return true;
                    }
                    break;
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    break;
                default:
                    return false;
            }
            return Modifier.isPublic(m.getModifiers());
        }
    }

    Visibility creatorVisibility() default Visibility.DEFAULT;

    Visibility fieldVisibility() default Visibility.DEFAULT;

    Visibility getterVisibility() default Visibility.DEFAULT;

    Visibility isGetterVisibility() default Visibility.DEFAULT;

    Visibility setterVisibility() default Visibility.DEFAULT;

    JsonMethod[] value() default {JsonMethod.ALL};
}
