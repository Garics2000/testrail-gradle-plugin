package io.github.garics2000.testrail.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TestSuite {
    private String name;
    private String timestamp;
    private List<TestCase> testCases = new ArrayList<>();
}
