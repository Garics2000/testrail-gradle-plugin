package org.ie.testrail.api.request.paginator;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

@Slf4j
public class PaginatorImpl implements Paginator {
    @Override
    public boolean hasNext(String responseBody) {
        return getNextUrl(responseBody) != null;
    }

    @Override
    public String getNextUrl(String responseBody) {
        try {
            JSONObject responseObject = new JSONObject(responseBody);
            return responseObject.getJSONObject("_links").optString("next", null);
        } catch (JSONException e) {
            log.error("Failed to parse JSON response: {}", e.getMessage());
            return null;
        }
    }
}