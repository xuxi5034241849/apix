package org.xuxi.apix.build;

import org.xuxi.apix.schema.ApiListing;
import org.xuxi.apix.schema.ApiDescription;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class ApiListingBuilder {

    private List<ApiDescription> apis = newArrayList();


    public static ApiListingBuilder getBuild() {
        return new ApiListingBuilder();
    }

    public ApiListingBuilder apis(List<ApiDescription> apis) {
        if (apis != null) {
            this.apis = apis;
        }
        return this;
    }



    public ApiListing build() {
        return new ApiListing(apis);
    }
}
