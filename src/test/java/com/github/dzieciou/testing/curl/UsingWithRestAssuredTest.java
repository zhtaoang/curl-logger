package com.github.dzieciou.testing.curl;


import com.jayway.restassured.config.HttpClientConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.config;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.config.HttpClientConfig.httpClientConfig;

public class UsingWithRestAssuredTest {

    @Test(groups = "end-to-end-samples")
    public void test()  {

        //@formatter:off
        given()
                .redirects().follow(false)
                .config(config()
                        .httpClient(httpClientConfig()
                                .reuseHttpClientInstance().httpClientFactory(new MyHttpClientFactory())))
        .when()
                .get("http://google.com")
        .then()
                .statusCode(302);
        //@formatter:on
    }

    private static class MyHttpClientFactory implements HttpClientConfig.HttpClientFactory {

        @Override
        public HttpClient createHttpClient() {
            AbstractHttpClient client = new DefaultHttpClient();
            client.addRequestInterceptor(new CurlLoggingInterceptor());
            return client;
        }
    }

}
