package io.github.garics2000.testrail.service.helpers;

import java.net.URL;

public class UrlManager {
    private static UrlManager instance;
    private URL[] urls;

    private UrlManager() {
    }

    public static UrlManager getInstance() {
        if (instance == null) {
            instance = new UrlManager();
        }
        return instance;
    }

    public URL[] getUrls() {
        return this.urls;
    }

    public void setUrls(URL[] urls) {
        this.urls = urls;
    }
}