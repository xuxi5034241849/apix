package org.xuxi.apix.build;

import org.springframework.http.HttpMethod;

/**
 * api 选项内容构造器
 */
public class OperationBuilder {
    private HttpMethod method = HttpMethod.GET;
    private String summary;


    public OperationBuilder setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public OperationBuilder setSummary(String summary) {
        this.summary = summary;
        return this;
    }
}