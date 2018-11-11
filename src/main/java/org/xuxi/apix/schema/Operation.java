package org.xuxi.apix.schema;

import org.springframework.http.HttpMethod;


/**
 * mapping 属性 schema 映射
 */
public class Operation {
    private final HttpMethod method;
    private final String summary;


    public Operation(HttpMethod method, String summary) {
        this.method = method;
        this.summary = summary;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getSummary() {
        return summary;
    }
}