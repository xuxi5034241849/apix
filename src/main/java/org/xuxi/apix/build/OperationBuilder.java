package org.xuxi.apix.build;

import org.springframework.http.HttpMethod;
import org.xuxi.apix.schema.Operation;

import java.util.Map;

/**
 * api 选项内容构造器
 */
public class OperationBuilder {
    private HttpMethod method = HttpMethod.GET;
    private String summary;
    private Map<String, Object> params;
    private Map<String, Object> paramsBody;


    public OperationBuilder setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public OperationBuilder setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public OperationBuilder setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public OperationBuilder setParamsBody(Map<String, Object> paramsBody) {
        this.paramsBody = paramsBody;
        return this;
    }

    public Operation build(){
        return new Operation(method, summary, params, paramsBody);
    }
}