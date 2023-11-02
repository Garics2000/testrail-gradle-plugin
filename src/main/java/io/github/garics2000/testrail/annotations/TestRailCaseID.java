package io.github.garics2000.testrail.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to associate a test method with a TestRail Case ID.
 * <p>
 * This annotation is used to map test methods to corresponding TestRail test cases
 * through a specific Case ID. It's required for exporting latest test run result to TMS.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * {@code
 * \@TestRailCaseID(123)
 * \@Test
 * public void testMethod() {
 *     // Test implementation
 * }
 * }
 * </pre>
 *
 * @author Garics2000
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestRailCaseID {
    int value();
}