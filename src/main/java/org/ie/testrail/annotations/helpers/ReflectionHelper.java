package org.ie.testrail.annotations.helpers;

import org.ie.testrail.model.TestCase;
import org.ie.testrail.service.helpers.UrlManager;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ReflectionHelper {
    private static final URL[] classpathUrls = UrlManager.getInstance().getUrls();

    public static Class<?> getTestClass(TestCase testCase) throws ClassNotFoundException {
        return Class.forName(testCase.getTestClassName(), true, new URLClassLoader(classpathUrls, ReflectionHelper.class.getClassLoader()));
    }

    public static Method getTestMethod(TestCase testCase) throws ClassNotFoundException, NoSuchMethodException {
        String sanitizedMethodName = testCase.getTestName().replaceAll("\\(\\)", "");
        Class<?> testClass = Class.forName(testCase.getTestClassName(), true, new URLClassLoader(classpathUrls, ReflectionHelper.class.getClassLoader()));
        try {
            return testClass.getMethod(sanitizedMethodName);
        } catch (NoSuchMethodException e) {
            return testClass.getDeclaredMethod(sanitizedMethodName);
        }
    }
}