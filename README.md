# Overview

[![Build Status](https://travis-ci.org/MasonM/wiremock-stub-mapping-tracker.svg?branch=master)](https://travis-ci.org/MasonM/wiremock-unused-stubs-extension)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.masonm/wiremock-stub-mapping-tracker/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.masonm/wiremock-stub-mapping-tracker)

wiremock-stub-mapping-tracker is a set of [WireMock](http://wiremock.org) extensions for tracking which stub mappings have been used in a request, and easily delete unmatched mappings. This is useful in conjunction with [Record and Playback](http://wiremock.org/docs/record-playback-legacy/) for pruning generated stub mappings. Tracking is done by recording a list of stub mappings IDs that have been used to serve a request, and is independent of the request journal.

# Building

Run `./gradlew jar` to build the JAR without dependencies or `./gradlew fatJar` to build a standalone JAR. These will be placed in `build/libs/`.

# Running

Standalone server:
```sh
java -jar build/libs/wiremock-stub-mapping-tracker-0.1-standalone.jar
```

With WireMock standalone JAR:
```sh
wget -nc http://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-standalone/2.14.0/wiremock-standalone-2.14.0.jar
java \
        -cp wiremock-standalone-2.14.0.jar:build/libs/wiremock-stub-mapping-tracker-0.1.jar \
        com.github.tomakehurst.wiremock.standalone.WireMockServerRunner \
        --extensions="com.github.masonm.StubMappingTrackerAdminExtension,com.github.masonm.StubMappingTrackerPostServeExtension"
```

Programmatically in Java:
```java
new WireMockServer(wireMockConfig()
    .extensions("com.github.masonm.StubMappingTrackerAdminExtension", "com.github.masonm.StubMappingTrackerPostServeExtension"))
```

# Usage

* `GET /__admin/mappings_tracker/matched` - Return all stub mappings that have matched a request
* `GET /__admin/mappings_tracker/unmatched` - Return all stub mappings that haven't matched a request
* `POST /__admin/mappings_tracker/reset` - Clear list of tracked stub mappings
* `DELETE /__admin/mappings_tracker/unmatched` - Delete all stub mappings that haven't matched a request


