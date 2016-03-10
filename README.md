# curl-logger

Enables logging each HTTP request sent by the Apache HTTP client as a CURL command.

[cURL][1] is a popular command line tool used for transferring data using various protocol. For
example, to request a HTTP resource:

    curl 'https://www.google.com/' -H 'Accept: ' -H 'Host:www.google.com' -H 'Connection:Keep-Alive' 
    -H 'User-Agent:Apache-HttpClient/4.2.6(java1.5)' --compressed

As a tool reproducing issues and bugs it has become so popular that Chrome browser team has
added "Copy as CURL" action to their browser's network panel. This way a tester and a developer can quickly
reproduce the issue and isolate its root cause. 

# Usage

Latest snapshot:

```xml
<dependency>
  <groupId>com.github.dzieciou.testing</groupId>
  <artifactId>curl-logger</artifactId>
  <version>0.1-SNAPSHOT</version>
</dependency>
```

Available from this repository: 

    https://oss.sonatype.org/content/repositories/snapshots/
    
When creating HTTP client instance, you must configure it to use CurlLoggingInterceptor:
    
```java
private static class MyHttpClientFactory implements HttpClientConfig.HttpClientFactory {

  @Override
  public HttpClient createHttpClient() {
    AbstractHttpClient client = new DefaultHttpClient();
    client.addRequestInterceptor(new CurlLoggingInterceptor());
    return client;
  }
}
```    

and use it in th test:

```java  
@Test
public void test() throws IOException {

  given()
    .redirects().follow(false)
    .config(config()
      .httpClient(httpClientConfig()
        .reuseHttpClientInstance().httpClientFactory(new MyHttpClientFactory())))
  .when()
    .get("http://google.com")
  .then()
    .statusCode(302); 
}
```

# Prerequisities

* JDK 8

# Bugs and features request

Report or request in [JIRA][2].

  [1]: https://curl.haxx.se/
  [2]: https://github.com/dzieciou/curl-logger/issues
    
   