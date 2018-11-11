package org.xuxi.apix.operation;

import com.google.common.base.Optional;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.xuxi.apix.annotations.ApiOperation;
import org.xuxi.apix.context.OperationContext;

@Component
public class OperationSummaryReader implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext context) {
        Optional<ApiOperation> apiOperationAnnotation = context.findAnnotation(ApiOperation.class);
        if (apiOperationAnnotation.isPresent() && StringUtils.hasText(apiOperationAnnotation.get().value())) {
            context.operationBuilder().setSummary(apiOperationAnnotation.get().value());
        }
    }

}
