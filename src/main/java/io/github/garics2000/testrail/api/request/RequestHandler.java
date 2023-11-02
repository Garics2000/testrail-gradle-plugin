package io.github.garics2000.testrail.api.request;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.github.garics2000.testrail.api.http.HttpMethod;
import io.github.garics2000.testrail.api.request.paginator.Paginator;
import io.github.garics2000.testrail.api.request.paginator.PaginatorImpl;
import io.github.garics2000.testrail.api.response.ApiResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler {
    private final RequestStrategy requestStrategy;

    public RequestHandler(RequestStrategy requestStrategy) {
        this.requestStrategy = requestStrategy;
    }

    public ApiResponse makeRequest(String path, HttpMethod method) throws IOException, InterruptedException {
        return makeRequest(path, method, new JSONObject());
    }

    public ApiResponse makeRequest(String path, HttpMethod method, @NotNull JSONObject payload) throws IOException, InterruptedException {
        return requestStrategy.execute(path, method, payload);
    }

    public List<ApiResponse> makePaginatedRequest(String initialUrl, HttpMethod method, JSONObject payload) throws IOException, InterruptedException {
        Paginator paginator = new PaginatorImpl();
        List<ApiResponse> paginatedResponses = new ArrayList<>();
        String currentUrl = initialUrl;
        String lastResponseBody;

        do {
            ApiResponse response = makeRequest(currentUrl, method, payload);
            if (response == null || !response.isSuccess()) {
                break;
            }

            lastResponseBody = response.getBody();
            paginatedResponses.add(response);

            currentUrl = paginator.getNextUrl(lastResponseBody);

        } while (paginator.hasNext(lastResponseBody));

        return paginatedResponses;
    }
}