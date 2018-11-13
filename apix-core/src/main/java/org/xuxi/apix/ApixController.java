package org.xuxi.apix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xuxi.apix.annotations.Api;
import org.xuxi.apix.doc.DocDocument;

@Api("演示API管理")
@RestController
public class ApixController {

    @Autowired
    private DocumentationCache documentationCache;

    @GetMapping("apix/doc")
    @ResponseBody
    public DocDocument api() {

        return new DocDocument(documentationCache.getDocumentationLookup());
    }


}
