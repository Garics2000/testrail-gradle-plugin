package org.ie.testrail.model;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TestCase {
    private int id;
    private String testClassName;
    private String testName;
    private float time;
    private List<TestFailure> failures = new ArrayList<>();
    private List<TestFailure> errors = new ArrayList<>();

    public int getTestRailStatus() {
        return failures.size() + errors.size() == 0 ? TestCaseStatus.PASSED.getValue() : TestCaseStatus.FAILED.getValue();
    }

    public String getFailureErrorMessages() {
        StringBuilder result = new StringBuilder();

        if (!failures.isEmpty()) {
            String failureMessages = failures.stream()
                    .map(TestFailure::getMessage)
                    .collect(Collectors.joining(", "));
            result.append("Failures: ").append(failureMessages).append("\n");
        }

        if (!errors.isEmpty()) {
            String errorMessages = errors.stream()
                    .map(TestFailure::getMessage)
                    .collect(Collectors.joining(", "));
            result.append("Errors: ").append(errorMessages);
        }

        return result.toString();
    }

    @Getter
    enum TestCaseStatus {
        PASSED(1),
        FAILED(5);

        private final int value;

        TestCaseStatus(int value) {
            this.value = value;
        }
    }
}