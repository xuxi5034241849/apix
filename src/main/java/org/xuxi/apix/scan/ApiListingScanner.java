package org.xuxi.apix.scan;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xuxi.apix.build.ApiListingBuilder;
import org.xuxi.apix.context.RequestMappingContext;
import org.xuxi.apix.schema.ApiListing;
import org.xuxi.apix.module.ResourceGroup;
import org.xuxi.apix.schema.ApiDescription;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

@Component
public class ApiListingScanner {


    private ApiDescriptionReader apiDescriptionReader;

    @Autowired
    public ApiListingScanner(ApiDescriptionReader apiDescriptionReader) {
        this.apiDescriptionReader = apiDescriptionReader;
    }

    Set<ApiDescription> apiDescriptions = newHashSet();

    public Multimap<String, ApiListing> scan(Map<ResourceGroup, List<RequestMappingContext>> resourceGroupListMap) {

        Multimap<String, ApiListing> apiListingMap = LinkedListMultimap.create();

        for (ResourceGroup resourceGroup : resourceGroupListMap.keySet()) {
            for (RequestMappingContext each : resourceGroupListMap.get(resourceGroup)) {

                apiDescriptions.addAll(apiDescriptionReader.read(each));
            }
            List<ApiDescription> sortedApis = newArrayList(apiDescriptions);

            ApiListingBuilder apiListingBuilder = ApiListingBuilder.getBuild()
                    .apis(sortedApis);

            ApiListing apiListing = apiListingBuilder.build();

            apiListingMap.put(resourceGroup.getGroupName(), apiListingBuilder.build());

        }

        return apiListingMap;
    }
}
