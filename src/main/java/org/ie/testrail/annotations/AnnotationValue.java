package org.ie.testrail.annotations;

public class AnnotationValue {
    private final String stringValue;
    private final Integer intValue;

    private AnnotationValue(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    public static AnnotationValue ofString(String value) {
        return new AnnotationValue(value, null);
    }

    public static AnnotationValue ofInt(Integer value) {
        return new AnnotationValue(null, value);
    }

    public boolean isString() {
        return stringValue != null;
    }

    public boolean isInt() {
        return intValue != null;
    }

    public String asString() {
        return stringValue;
    }

    public Integer asInt() {
        return intValue;
    }
}