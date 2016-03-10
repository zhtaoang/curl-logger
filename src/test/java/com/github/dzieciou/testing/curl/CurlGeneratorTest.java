package com.github.dzieciou.testing.curl;


import com.google.common.collect.ImmutableList;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Test(groups = "unit")
public class CurlGeneratorTest {

    @Test
    public void shouldPrintGetRequestProperly() throws Exception {
        HttpGet getRequest = new HttpGet("http://test.com:8080/items/query?x=y#z");
        assertThat("curl 'http://test.com:8080/items/query?x=y#z' --compressed",
                equalTo(CurlGenerator.generateCurl(getRequest)));
    }

    @Test
    public void shouldPrintPostRequestProperly() throws Exception {
        HttpPost posttRequest = new HttpPost("http://google.pl/");
        List<NameValuePair> postParameters = ImmutableList.<NameValuePair>builder()
                .add(new BasicNameValuePair("param1", "param1_value"))
                .add(new BasicNameValuePair("param2", "param2_value"))
                .build();
        posttRequest.setEntity(new UrlEncodedFormEntity(postParameters));
        posttRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
        assertThat("curl 'http://google.pl/' -H 'Content-Type: application/x-www-form-urlencoded' --data 'param1=param1_value&param2=param2_value' --compressed",
                equalTo(CurlGenerator.generateCurl(posttRequest)));
    }

    @Test
    public void shouldPrintPostRequestWithBinaryDataProperly() {
        // TODO
    }

    @Test
    public void shouldPrintDeleteRequestProperly() throws Exception {
        HttpDelete deleteRequest = new HttpDelete("http://test.com/items/12345");
        assertThat("curl 'http://test.com/items/12345' -X DELETE --compressed",
                equalTo(CurlGenerator.generateCurl(deleteRequest)));
    }

    @Test
    public void shouldPrintHeadRequestProperly() throws Exception {
        HttpHead headRequest = new HttpHead("http://test.com/items/12345");
        assertThat("curl 'http://test.com/items/12345' -X HEAD --compressed",
                equalTo(CurlGenerator.generateCurl(headRequest)));
    }

    @Test
    public void shouldPrintPutRequestProperly() throws Exception {
        HttpPut putRequest = new HttpPut("http://test.com/items/12345");
        putRequest.setEntity(new StringEntity("details={\"name\":\"myname\",\"age\":\"20\"}"));
        putRequest.setHeader("Content-Type", "application/json");
        assertThat("curl 'http://test.com/items/12345' -X PUT -H 'Content-Type: application/json' --data 'details={\"name\":\"myname\",\"age\":\"20\"}' --compressed",
                equalTo(CurlGenerator.generateCurl(putRequest)));
    }

}
