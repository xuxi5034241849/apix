package org.xuxi.apix.doc;

import org.xuxi.apix.schema.ApiDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiDocument {

    private String path;

    private String methodName;

    private List<Map<String,String>> operations;

    public ApiDocument(ApiDescription apiDescription) {

        this.path = apiDescription.getPath();
        this.methodName = apiDescription.getMethodName();

        if(apiDescription.getOperations().size() > 0 ){
            operations = new ArrayList<>();
            apiDescription.getOperations().forEach(operation ->{
                Map<String, String> map = new HashMap<>();
                map.put("method", operation.getMethod().toString());
                map.put("summary", operation.getSummary());
                this.operations.add(map);
            });
        }
    }


    public String getPath() {
        return path;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<Map<String, String>> getOperations() {
        return operations;
    }
}
