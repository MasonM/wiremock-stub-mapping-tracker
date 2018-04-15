package com.github.masonm;

import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.testsupport.WireMatchers.equalToJson;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResetTrackerAcceptanceTest extends StubMappingTrackerAcceptanceTestBase {
    @Test
    public void withOneMatchingMapping() {
        StubMapping stub1 = stubFor(get(urlEqualTo("/stub-1")));
        assertThat(testClient.get("/stub-1").statusCode(), is(HTTP_OK));
        assertThat(testClient.get(ADMIN_URL_MATCHED).content(), equalToJson("[" + stub1.toString() + "]"));
        assertThat(testClient.get(ADMIN_URL_UNMATCHED).content(), equalToJson("[]"));

        HttpEntity emptyBody = new StringEntity("", ContentType.TEXT_PLAIN);
        assertThat(testClient.post(ADMIN_URL_RESET, emptyBody).statusCode(), is(HTTP_OK));
        assertThat(testClient.get(ADMIN_URL_MATCHED).content(), equalToJson("[]"));
        assertThat(testClient.get(ADMIN_URL_UNMATCHED).content(), equalToJson("[" + stub1.toString() + "]"));
    }
}
