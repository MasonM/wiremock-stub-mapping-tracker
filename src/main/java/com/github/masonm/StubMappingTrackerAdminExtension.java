package com.github.masonm;

import com.github.tomakehurst.wiremock.admin.Router;
import com.github.tomakehurst.wiremock.admin.model.PathParams;
import com.github.tomakehurst.wiremock.core.Admin;
import com.github.tomakehurst.wiremock.extension.AdminApiExtension;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.http.RequestMethod.DELETE;
import static com.github.tomakehurst.wiremock.http.RequestMethod.GET;
import static com.github.tomakehurst.wiremock.http.RequestMethod.POST;
import static com.github.tomakehurst.wiremock.http.ResponseDefinition.ok;
import static com.github.tomakehurst.wiremock.http.ResponseDefinition.okForJson;

public class StubMappingTrackerAdminExtension implements AdminApiExtension {
    @Override
    public String getName() {
        return getClass().getName();
    }

    /**
     * Adds the routes under /mappings_tracker/, using anonymous classes for the AdminTask
     *
     * @param router The Wiremock-provided router
     */
    @Override
    public void contributeAdminApiRoutes(Router router) {
        // Can't use "/mappings/" as the base route because that'll match "/mappings/{id}"
        router.add(POST, "/mappings_tracker/reset",
            (Admin admin, Request request, PathParams pathParams) -> {
                StubMappingTracker.INSTANCE.clear();
                return ok();
            }
        );

        router.add(GET, "/mappings_tracker/matched",
            (Admin admin, Request request, PathParams pathParams) ->
                okForJson(getAllMappings(admin)
                    .filter(StubMappingTracker.INSTANCE::isMatched)
                    .toArray()
                )
        );

        router.add(GET, "/mappings_tracker/unmatched",
            (Admin admin, Request request, PathParams pathParams) ->
                okForJson(getAllMappings(admin)
                    .filter(StubMappingTracker.INSTANCE::isNotMatched)
                    .toArray()
                )
        );

        router.add(DELETE, "/mappings_tracker/unmatched",
            (Admin admin, Request request, PathParams pathParams) -> {
                getAllMappings(admin)
                    .filter(StubMappingTracker.INSTANCE::isNotMatched)
                    .forEach(admin::removeStubMapping);
                return ok();
            }
        );
    }

    /**
     * Returns all loaded stub mappings as a stream
     *
     * @param admin Wiremock-provided Admin object
     * @return Stream of StubMappings
     */
    private Stream<StubMapping> getAllMappings(Admin admin) {
        return admin
            .listAllStubMappings()
            .getMappings()
            .stream();
    }
}
