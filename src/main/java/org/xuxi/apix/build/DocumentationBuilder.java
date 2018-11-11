package org.xuxi.apix.build;

import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import org.xuxi.apix.schema.ApiListing;
import org.xuxi.apix.schema.Documentation;

public class DocumentationBuilder {

    private Multimap<String, ApiListing> apiListings;

    public DocumentationBuilder setApiListings(Multimap<String, ApiListing> apiListings) {
        this.apiListings = apiListings;
        return this;
    }

    public static DocumentationBuilder getBuild(){
        return new DocumentationBuilder();
    }

    public Documentation build() {
        return new Documentation(
                apiListings);
    }



}
