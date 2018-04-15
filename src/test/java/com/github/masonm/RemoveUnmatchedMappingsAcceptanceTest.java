package com.github.masonm;

import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.junit.Test;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RemoveUnmatchedMappingsAcceptanceTest extends StubMappingTrackerAcceptanceTestBase {
    @Test
    public void withNoRequests() {
        assertThat(testClient.delete(ADMIN_URL_UNMATCHED).statusCode(), is(HTTP_OK));
    }

    @Test
    public void withOneUnusedMapping() {
        stubFor(get(urlEqualTo("/stub-1")));
        assertThat(listAllStubMappings().getMappings().size(), is(1));

        assertThat(testClient.delete(ADMIN_URL_UNMATCHED).statusCode(), is(HTTP_OK));
        assertThat(listAllStubMappings().getMappings().size(), is(0));
    }

    @Test
    public void withOneUsedMapping() {
        stubFor(get(urlEqualTo("/stub-1")));
        assertThat(testClient.get("/stub-1").statusCode(), is(HTTP_OK));

        assertThat(testClient.delete(ADMIN_URL_UNMATCHED).statusCode(), is(HTTP_OK));
        assertThat(listAllStubMappings().getMappings().size(), is(1));
    }

    @Test
    public void withMultipleMappings() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        StubMapping stub1 = stubFor(get(urlEqualTo("/stub-1")).withId(id1));
        stubFor(get(urlEqualTo("/stub-2")).withId(id2));

        assertThat(testClient.get("/stub-1").statusCode(), is(HTTP_OK));

        assertThat(testClient.delete(ADMIN_URL_UNMATCHED).statusCode(), is(HTTP_OK));
        assertThat(getSingleStubMapping(id1), is(stub1));
        assertThat(listAllStubMappings().getMappings().size(), is(1));
    }
}
