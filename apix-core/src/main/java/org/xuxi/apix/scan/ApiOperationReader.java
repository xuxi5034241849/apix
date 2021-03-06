package org.xuxi.apix.scan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xuxi.apix.context.OperationContext;
import org.xuxi.apix.context.RequestMappingContext;
import org.xuxi.apix.operation.OperationBuilderPlugin;
import org.xuxi.apix.schema.Operation;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

@Component
public class ApiOperationReader {


    private OperationBuilderPlugin[] operationBuilderPlugins;

    @Autowired
    public ApiOperationReader(OperationBuilderPlugin[] operationBuilderPlugins) {
        this.operationBuilderPlugins = operationBuilderPlugins;
    }

    /**
     * 读取 API 选项的描述信息
     *
     * @param context
     */
    public List<Operation> read(RequestMappingContext context) {

        Set<RequestMethod> requestMethods = context.getMethodsCondition();

        List<Operation> operations = newArrayList();

        for (RequestMethod httpRequestMethod : requestMethods) {

            OperationContext operationContext = new OperationContext(httpRequestMethod, context);

            // 遍历应用方法
            Arrays.stream(operationBuilderPlugins).forEach(plugin -> {
                plugin.apply(operationContext);
            });

            operations.add(operationContext.operationBuilder().build());
        }

        return operations;
    }


}
