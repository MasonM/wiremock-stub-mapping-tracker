package com.github.masonm;

import com.github.tomakehurst.wiremock.standalone.WireMockServerRunner;

import java.util.Arrays;

public final class StubMappingTrackerExtensionStandalone {
    private StubMappingTrackerExtensionStandalone() {}

    // When WireMock is run in standalone mode, WireMockServerRunner.run() is the entry point,
    // so we just delegate to that, passing along a CSV string with each extension class to load
    public static void main(String... args) {
        String[] newArgs = Arrays.copyOf(args, args.length + 1);
        newArgs[args.length] = "--extensions="
            + StubMappingTrackerAdminExtension.class.getName() + ","
            + StubMappingTrackerPostServeExtension.class.getName();
        new WireMockServerRunner().run(newArgs);
    }
}
