package com.github.dzieciou.testing.curl;


import com.jayway.restassured.config.HttpClientConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.annotations.Test;

import java.io.File;

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

    @Test
    public void shouldPrintPostRequestWithMultipartDataProperly() {
        given().
        config(config()
                .httpClient(httpClientConfig()
                        .reuseHttpClientInstance().httpClientFactory(new MyHttpClientFactory()))).
                log().all().multiPart(new File("README.md")).formParam("name", "value")
                .when().post("http://www.google.com");

        // RFC for multipart: https://tools.ietf.org/html/rfc2046#page-17
        // TODO handling multipart with boundaries
        // TODO handling multiparts without boundaries
        // TODO how do we handle binary data? I think in multipart we filename, so we can use @file syntax from curl,
        //      but file itself must be provided by a testers
        // TODO Use POSTMAN plugin for Chrome can generate curls as well (their code for curl is not available, but I can reproduce the cases)

        /* Google Chrome / WebKit generates such CURL. What's the meaning of $'? Is file content actually sent? It's missing in request...

        curl 'https://images.google.com/searchbyimage/upload'
        -H 'origin: https://images.google.com'
        -H 'accept-encoding: gzip, deflate'
        -H 'accept-language: en-US,en;q=0.8,it;q=0.6,pl;q=0.4'
        -H 'cookie: CONSENT=YES+PL.en+20151207-13-0; GMAIL_RTT=157; SID=OAOvbd9-ohy_2OdT8BAtSqSf83Pq-QtJGPS5uODg8dSJP3cqget6-au01HsZ1nUITU-jyQ.; HSID=AQHwYW1prdsRvC95Z; SSID=ABGVoaZJHV-WEL_XV; APISID=pksuOSpPwDA9kSUl/AJXOIxcTlojgmsO8D; SAPISID=f4nu6IJr0u6vT67I/ANdGKceF1QujNINC6; NID=79=smcSPNnz0WZnnTpxO5wF9Mr9VO9vTu63OiidjeBwcoZT2qmH3xm9Zsb2iAIOpWW-NWG7MvcSOyH0hRiZ5F3rDlpS9IJxSJPg19_2BPUvigaHjV2TSXj7GPQiM4FoN5K2k-zHJjMD414S_SV0MVG2xtebE5ya1TDs2zFgsPgCk3MEf4xEcsJ5Oeqs'
        -H 'x-client-data: CJa2yQEIpLbJAQjBtskBCP2VygE='
        -H 'upgrade-insecure-requests: 1'
        -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.86 Safari/537.36'
        -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundaryXbDOOzljTdAAgUiO'
        -H 'accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*//*;q=0.8' -H 'cache-control: max-age=0' -H 'authority: images.google.com'
        -H 'referer: https://images.google.com/'
        --data-binary $'------WebKitFormBoundaryXbDOOzljTdAAgUiO\r\nContent-Disposition: form-data; name="image_url"\r\n\r\n\r\n------WebKitFormBoundaryXbDOOzljTdAAgUiO\r\nContent-Disposition: form-data; name="encoded_image"; filename="_7009283.jpeg"\r\nContent-Type: image/jpeg\r\n\r\n\r\n------WebKitFormBoundaryXbDOOzljTdAAgUiO\r\nContent-Disposition: form-data; name="image_content"\r\n\r\n\r\n------WebKitFormBoundaryXbDOOzljTdAAgUiO\r\nContent-Disposition: form-data; name="filename"\r\n\r\n\r\n------WebKitFormBoundaryXbDOOzljTdAAgUiO\r\nContent-Disposition: form-data; name="hl"\r\n\r\nen\r\n------WebKitFormBoundaryXbDOOzljTdAAgUiO--\r\n' --compressed

         */
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
