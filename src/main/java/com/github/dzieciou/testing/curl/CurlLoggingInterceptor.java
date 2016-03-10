package com.github.dzieciou.testing.curl;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Logs each HTTP request as CURL command in "curl" log.
 */

public class CurlLoggingInterceptor implements HttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger("curl");

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        try {
            log.debug(CurlGenerator.generateCurl(request));
        } catch (Exception e) {
            log.warn("Failed to generate CURL command for HTTP request", e);
        }
    }
}
