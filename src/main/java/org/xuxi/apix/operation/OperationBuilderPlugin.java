package org.xuxi.apix.operation;


import org.xuxi.apix.context.OperationContext;


/**
 * 读取内容应用器
 */
public interface OperationBuilderPlugin {

    void apply(OperationContext context);
}
