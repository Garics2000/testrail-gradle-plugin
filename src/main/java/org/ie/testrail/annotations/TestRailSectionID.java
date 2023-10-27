package org.ie.testrail.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to associate a test class with a TestRail Section ID.
 * <p>
 * This annotation is used to map test classes to corresponding sections in TestRail.
 * If it isn't specified, test cases and test outcomes are exported to the default.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * {@code
 * \@TestRailSectionID("111")
 * public class ExampleTestClass {
 *     \@Test
 *     public void testMethod() {
 *         // Test implementation
 *     }
 * }
 * }
 * </pre>
 *
 * @author Garics2000
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestRailSectionID {
    int value();
}