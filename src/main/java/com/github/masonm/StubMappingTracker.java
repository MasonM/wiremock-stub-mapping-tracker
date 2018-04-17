package com.github.masonm;

import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import java.util.HashSet;
import java.util.Set;

public enum StubMappingTracker {
    INSTANCE;
    private final Set<StubMapping> matchedStubMappings = new HashSet<>();

    public void clear() {
        matchedStubMappings.clear();
    }

    public Boolean isMatched(StubMapping stubMapping) {
        return matchedStubMappings.contains(stubMapping);
    }

    public Boolean isNotMatched(StubMapping stubMapping) {
        return !isMatched(stubMapping);
    }

    public void add(StubMapping stubMapping) {
        matchedStubMappings.add(stubMapping);
    }
}
