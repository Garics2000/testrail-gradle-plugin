package org.ie.testrail.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to associate a test method with a TestRail Automation ID.
 * <p>
 * This annotation should be placed on test methods to map them to
 * corresponding TestRail test cases through an automation ID.
 * It's required for export auto tests task.
 * <p/>
 * <p>
 * <b>Notice:<b/> This annotation is not required for exporting test run results.
 * </p>
 *
 * <h2>Usage Example</h2>
 * <pre>
 * {@code
 * \@TestRailAutomationID("TR-123")
 * \@Test
 * public void someTestMethod() {
 *     // Test implementation
 * }
 * }
 * </pre>
 *
 * @author Garics2000
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestRailAutomationID {
    String value();
}