package io.github.garics2000.testrail.api.request;

public final class Paths {

    private static final String BASE_API_PATH = "/index.php?/api/v2";
    public static final String GET_PROJECT = buildPath("get_project/");
    public static final String ADD_CASE = buildPath("add_case/");
    public static final String GET_CASES = buildPath("get_cases/");
    public static final String ADD_TEST_RUN = buildPath("add_run/");
    public static final String ADD_RESULTS = buildPath("add_results_for_cases/");

    private Paths() {
    }

    private static String buildPath(String specificPath) {
        return BASE_API_PATH + "/" + specificPath;
    }
}
