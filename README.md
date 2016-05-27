# CURL Logger

Logs each HTTP request sent by REST Assured as a [CURL][1] command.

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

Latest release:

```xml
<dependency>
  <groupId>com.github.dzieciou.testing</groupId>
  <artifactId>curl-logger</artifactId>
  <version>0.1</version>
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

CURL commands are logged to a "curl" logger. The library requires only the logger to be [slf4j][4]-compliant, e.g.,
using [logback][5]. Sample logback configuration that logs all CURL commands to standard system output would be:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%-4relative [%thread] %-5level %logger{35} - %msg %n</pattern>
        </encoder>
    </appender>
    <logger name="curl" level="DEBUG">
        <appender-ref ref="STDOUT"/>
    </logger>
</configuration>
```

## Prerequisities

* JDK 8

## Releases

0.2:

 * Support for multipart/mixed and multipart/form content types
 * Now all generated curl commands are "--insecure --verbose"
 
0.1:

 * Support for logging basic operations

## Bugs and features request

Report or request in [JIRA][2].

## Similar tools
  
* Chrome Web browser team has "Copy as CURL" in the network panel.
* OkHttp client provides similar request [interceptor][3] to log HTTP requests as curl command. 


  [1]: https://curl.haxx.se/
  [2]: https://github.com/dzieciou/curl-logger/issues
  [3]: https://github.com/mrmike/Ok2Curl 
  [4]: http://www.slf4j.org/
  [5]: http://logback.qos.ch/
