# Gradle TestRail Plugin

This plugin exports automated tests and test run results to TestRail via Gradle tasks.

## Advantage Features

<img src="docs/img/checkbox.jpeg" width="14"> Exports autotests and test run results independently of the test runtime. Flexible for CI/CD integration.<br/>
<img src="docs/img/checkbox.jpeg" width="14"> Works with single and multiple test suites project types of TestRail.<br/>
<img src="docs/img/checkbox.jpeg" width="14"> Compatible with JUnit4 and JUnit5 test reports.</br> 
<img src="docs/img/checkbox.jpeg" width="14"> Doesn't require TestRail CLI Tool for work.</br>  

## Plugin Installation

### From Local Repository

1. Make sure you have built and published the plugin to your local Maven repository.
2. In your test project's `build.gradle`, add the following:

```groovy
buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath "org.ie:testrail-gradle-plugin:${testRailPluginVersion}"
    }
}

dependencies {
    testImplementation "org.ie:testrail-gradle-plugin:${testRailPluginVersion}"
}


apply plugin: "org.ie.testrail-gradle-plugin"
```

### From Maven Central

For version hosted on Maven Central (coming soon)

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.ie:gradle-testrail-plugin:0.0.1'
    }
}

apply plugin: 'com.ie.testrail-gradle-plugin'
```

## Plugin Configuration

The plugin provides several configurations that can be adjusted as per your requirements. Here's an example of how you
can configure the plugin in your `build.gradle`:

```groovy
testRail {
    username = 'testrail_username'
    apiKey = 'testrail_api_key'
    projectId = 'testrail_project_id'
    suiteId = 'default_suite_id'
}
```

## Plugin Usage

Use below annotations to link your tests with TestRail entities.

### Class-Level Annotations

- **@TestRailSuiteID**: Represents the ID of the test suite in TestRail. Apply this annotation to your test class to
  link the entire class (or suite) with a specific TestRail suite.

```java

@TestRailSuiteID(1)
public class ExampleTestSuite {
    // Your test methods here
}
```  

- **@TestRailSectionId**: Denotes the ID of a specific section within a TestRail suite. Apply this to your test class to
  map it to a particular section.

```java

@TestRailSectionId(456)
public class MySectionTests {
    // Your test methods here
}
```

### Method Level Annotations

- **@TestRailCaseID**: Links to testcase in TestRail via ID field in test rail. The ID field should be internal integrer
  number format case ID
  in TestRail(without C* prefix as it displayed in the TMS UI).
- **@TestRailAutomationID**: Links to testcase in TestRail via automation ID (automation_id custom field should be set
  up in TestRail project).
  **Important!:** This link needs for initial export autotests to the given TestRail project.
  To export test run results of those tests they need to be additionally annotated with @TestRailCaseId.

```java
public class MyTestClass {

    @TestRailCaseID(123)
    @TestRailAutomationID("AUT_001")
    @Test
    public void myTestMethod() {
        // Your test code here
    }
}
```

### Plugin tasks

- `exportAutoTests`: Exports tests annotated with `@TestRailAutomationID` to the given project.

```bash
  ./gradlew exportAutoTests
```

- `exportTestRunResults`: Exports the test run results of the tests annotated with `@TestRailCaseID`.

```bash
  ./gradlew exportTestRunResults
```












