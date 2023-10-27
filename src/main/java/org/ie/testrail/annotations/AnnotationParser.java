package org.ie.testrail.annotations;

import org.ie.testrail.annotations.helpers.ReflectionHelper;
import org.ie.testrail.model.TestCase;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class AnnotationParser {
    public static final String VALUE = "value";

    public <T extends Annotation> boolean isAnnotatedTestCase(TestCase testCase, Class<T> annotationType) throws ClassNotFoundException, NoSuchMethodException {
        Method testMethod = ReflectionHelper.getTestMethod(testCase);
        Annotation annotation = testMethod.getAnnotation(annotationType);

        return annotation != null;
    }

    public <T extends Annotation> Optional<AnnotationValue> getClassAnnotationValue(TestCase testCase, Class<T> annotationType) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        Class<?> testClass = ReflectionHelper.getTestClass(testCase);
        return getAnnotationValue(testClass, annotationType);
    }

    public <T extends Annotation> Optional<AnnotationValue> getMethodAnnotationValue(TestCase testCase, Class<T> annotationType) throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        Method testMethod = ReflectionHelper.getTestMethod(testCase);
        return getAnnotationValue(testMethod, annotationType);
    }

    private <T extends Annotation> Optional<AnnotationValue> getAnnotationValue(
            AnnotatedElement element, Class<T> annotationType)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        T annotation = element.getAnnotation(annotationType);
        if (annotation == null) return Optional.empty();

        Method valueMethod = annotationType.getDeclaredMethod(VALUE);
        Object value = valueMethod.invoke(annotation);

        if (value instanceof Integer) return Optional.of(AnnotationValue.ofInt((Integer) value));

        if (value instanceof String) return Optional.of(AnnotationValue.ofString((String) value));

        return Optional.empty();
    }
}