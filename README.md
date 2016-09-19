# CURL Logger

Logs each HTTP request sent by REST-assured as a [CURL][1] command.

The following request from REST-assured test
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
  <version>0.5</version>
</dependency>
```
   
### Using with REST-assured client 
    
When creating HTTP client instance, you must configure it to use `CurlLoggingInterceptor`:
    
```java
private static class MyHttpClientFactory implements HttpClientConfig.HttpClientFactory {
  @Override
  public HttpClient createHttpClient() {
    AbstractHttpClient client = new DefaultHttpClient();
    client.addRequestInterceptor(CurlLoggingInterceptor.defaultBuilder().build());
    return client;
  }
}

private static RestAssuredConfig restAssuredConfig() {
  return config()
          .httpClient(httpClientConfig()
            .reuseHttpClientInstance().httpClientFactory(new MyHttpClientFactory()));
}

```   
 
and use it in the test:
```java  
given()
  .config(restAssuredConfig())
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

## Features

### Logging stacktrace

If your test is sending multiple requests it might be hard to understand which REST-assured request generated a given 
curl command. The library provides a way to log stacktrace where the curl generation was requested. You configure 
`CurlLoggingInterceptor` accordingly:

```java
CurlLoggingInterceptor.defaultBuilder().logStacktrace().build();
```

### Logging attached files

When you attach a file to your requests

```java
given()
  .config(restAssuredConfig())
  .baseUri("http://someHost.com")
  .multiPart("myfile", new File("README.md"), "application/json")
.when()
  .post("/uploadFile");
```

the library will include reference to it instead of pasting its content:
```
curl 'http://somehost.com/uploadFile' -F 'myfile=@README.md;type=application/json' -X POST ...
```

### Printing command in multiple lines

For leggibility reasons you may want to print your command in multiple lines:
```
curl 'http://google.pl/' \ 
  -H 'Content-Type: application/x-www-form-urlencoded' \ 
  --data 'param1=param1_value&param2=param2_value' \ 
  --compressed \ 
  --insecure \ 
  --verbose
```
or in Windows:
```
curl 'http://google.pl/' ^ 
  -H 'Content-Type: application/x-www-form-urlencoded' ^ 
  --data 'param1=param1_value&param2=param2_value' ^ 
  --compressed ^ 
  --insecure ^ 
  --verbose
```
To achieve this configure your `CurlLoggingInterceptor` as follows:
```java
CurlLoggingInterceptor.defaultBuilder().printMultiliner().build();
```
By default `CurlLoggingInterceptor` prints curl command in a single line.

## Prerequisities

* JDK 8
* Dependencies with which I tested the solution

```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.2</version>
</dependency>
<dependency>
    <groupId>io.restassured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>3.0.1</version>
</dependency>
```

## Releases

0.5:

* Upgraded to REST-assured 3.0.1 that contains important fix impacting curl-logger: Cookie attributes are no longer sent in request in accordance with RFC6265. 
* Fixed bug: cookie values can have = sign inside so we need to get around them somehow
* Cookie strings are now escaped
* CurlLoggingInterceptor's constructor is now protected to make extending it possible 
* CurlLoggingInterceptor can now be configured to print a curl command in multiple lines
 

0.4:
 
 * Upgraded to REST-assured 3.0

0.3:

 * Each cookie is now defined with "-b" option instead of -"H"
 * Removed heavy dependencies like Guava
 * Libraries like REST-assured and Apache must be now provided by the user (didn't want to constrain users to a specific version)
 * Can log stacktrace where curl generation was requested

0.2:

 * Support for multipart/mixed and multipart/form content types
 * Now all generated curl commands are "--insecure --verbose"
 
0.1:

 * Support for logging basic operations

## Bugs and features request

Report or request in [JIRA][2].

## Similar tools
  
* Chrome Web browser team has ["Copy as CURL"][7] in the network panel, similarly [Firebug add-on][8] for Firefox.
* OkHttp client provides similar request [interceptor][3] to log HTTP requests as curl command. 
* [Postman add-on][6] for Chrome provides a way to convert prepared requests as curl commands.


  [1]: https://curl.haxx.se/
  [2]: https://github.com/dzieciou/curl-logger/issues
  [3]: https://github.com/mrmike/Ok2Curl 
  [4]: http://www.slf4j.org/
  [5]: http://logback.qos.ch/
  [6]: https://www.getpostman.com/docs/creating_curl
  [7]: https://coderwall.com/p/-fdgoq/chrome-developer-tools-adds-copy-as-curl
  [8]: http://www.softwareishard.com/blog/planet-mozilla/firebug-tip-resend-http-request/
