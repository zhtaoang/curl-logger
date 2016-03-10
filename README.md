# curl-logger

Enables logging each HTTP request sent by the Apache HTTP client as a CURL command.

cURL is a popular command line tool used for transferring data using various protocol. For
example, to request a HTTP resource:

    curl 'https://www.google.com/' -H 'Accept: ' -H 'Host:www.google.com' -H 'Connection:Keep-Alive' 
    -H 'User-Agent:Apache-HttpClient/4.2.6(java1.5)' --compressed

As a tool reproducing issues and bugs it has become so popular that Chrome browser team has
added "Copy as CURL" action to their browser's network panel. This way a tester and a developer can quickly
reproduce the issue and isolate its root cause. 
