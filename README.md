# curl-logger

The library provides a way to log each HTTP request sent by Apache HTTP client as cURL commands.

cURL is a popular command line tool used for transferring data using various protocol, for
instance to request HTTP resource:

    curl 'https://www.google.com/' -H 'Accept: ' -H 'Host:www.google.com' -H 'Connection:Keep-Alive' 
    -H 'User-Agent:Apache-HttpClient/4.2.6(java1.5)' --compressed

As a tool reproducing issues and bugs it has become so popular that Chrome browser team has
added "Copy as CURL" action to their browser network panel. This way a tester can quickly
reproduce the issue, isolate a root cause in the system or in the test and file a bug report. 
