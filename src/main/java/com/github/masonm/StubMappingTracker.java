package com.github.masonm;

import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum StubMappingTracker {
    INSTANCE;
    private final Map<UUID, Boolean> tracker = new HashMap<>();

    public void clear() {
        tracker.clear();
    }

    public Boolean isMatched(StubMapping stubMapping) {
        return tracker.containsKey(stubMapping.getId());
    }

    public Boolean isNotMatched(StubMapping stubMapping) {
        return !isMatched(stubMapping);
    }

    public void add(StubMapping stubMapping) {
        tracker.putIfAbsent(stubMapping.getId(), true);
    }
}
