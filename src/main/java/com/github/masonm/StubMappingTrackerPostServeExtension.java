package com.github.masonm;

import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.PostServeAction;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

public class StubMappingTrackerPostServeExtension extends PostServeAction {
    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public void doGlobalAction(ServeEvent serveEvent, Admin admin) {
        if (serveEvent.getWasMatched()) {
            StubMappingTracker.INSTANCE.add(serveEvent.getStubMapping());
        }
    }
}
