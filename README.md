# CURL Logger

Logs each HTTP request sent by the Apache HTTP client as a [CURL][1] command.

The following HTTP request
```java  
HttpGet getRequest = new HttpGet("http://google.com");
createHttpClient().execute(getRequest);
```
will be logged as:
```
curl 'http://google.com/' --compressed 
```

The following request from REST-Assured test
```java  
given()
  .redirects().follow(false)
.when()
  .get("http://google.com")
.then()
  .statusCode(302); 
```
will be logged as:
```
curl 'http://google.com/' -H 'Accept: */*' -H 'Content-Length: 0' -H 'Host: google.com' -H 'Connection: Keep-Alive' -H 'User-Agent: Apache-HttpClient/4.5.1 (Java/1.8.0_45)' --compressed 
```

This way testers and developers can quickly reproduce an issue and isolate its root cause. 

## Usage

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
   
### Using with REST-Assured client 
    
When creating HTTP client instance, you must configure it to use `CurlLoggingInterceptor`:
    
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
and use it in the test:
```java  
given()
  .config(config()
    .httpClient(httpClientConfig()
      .reuseHttpClientInstance().httpClientFactory(new MyHttpClientFactory())))
...
```

## Prerequisities

* JDK 8

## Bugs and features request

Report or request in [JIRA][2].

## Similar tools
  
* Chrome Web browser team has "Copy as CURL" in the network panel.
* OkHttp client provides similar request [interceptor][3] to log HTTP requests as curl command. 


  [1]: https://curl.haxx.se/
  [2]: https://github.com/dzieciou/curl-logger/issues
  [3]: https://github.com/mrmike/Ok2Curl 
