package com.github.masonm;

import com.github.tomakehurst.wiremock.AcceptanceTestBase;
import org.junit.BeforeClass;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class StubMappingTrackerAcceptanceTestBase extends AcceptanceTestBase {
    protected static final String ADMIN_URL_BASE = "/__admin/mappings_tracker";
    protected static final String ADMIN_URL_MATCHED = ADMIN_URL_BASE + "/matched";
    protected static final String ADMIN_URL_UNMATCHED = ADMIN_URL_BASE + "/unmatched";
    protected static final String ADMIN_URL_RESET = ADMIN_URL_BASE + "/reset";

    @BeforeClass
    public static void setupServer() {
        setupServer(wireMockConfig()
            .dynamicPort()
            .extensions(StubMappingTrackerAdminExtension.class, StubMappingTrackerPostServeExtension.class)
            .withRootDirectory(setupTempFileRoot().getAbsolutePath())
        );
    }
}
