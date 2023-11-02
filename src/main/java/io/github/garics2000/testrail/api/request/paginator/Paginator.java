package io.github.garics2000.testrail.api.request.paginator;

public interface Paginator {
    boolean hasNext(String responseBody);

    String getNextUrl(String responseBody);
}