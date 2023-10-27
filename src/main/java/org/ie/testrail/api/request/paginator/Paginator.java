package org.ie.testrail.api.request.paginator;

public interface Paginator {
    boolean hasNext(String responseBody);

    String getNextUrl(String responseBody);
}