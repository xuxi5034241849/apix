package org.xuxi.apix.operation;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.xuxi.apix.context.OperationContext;


/**
 * 处理 RequestMapping 的 requestBody
 * 处理非基础类型&String
 * <p>
 * 处理这几种情况
 * 所有参与计算的对象，必须被 @RequestBody标注
 * <p>
 * <p>
 * 1、@Valid ：
 * ApiModelProperty (选填)
 * Notnull（必填）
 * <p>
 * 2、@Validate(g1) ：
 * ApiModelProperty/ApiModelProperty(g1)/(空)/*ApiModelProperty(g2)*，Notnull(g1)（必填）
 * ApiModelProperty(g1)，Notnull/ Notnull(g2)/(空)(选填)
 * <p>
 * <p>
 * 3、@ApiGroup(g1) ：
 * ApiModelProperty(g1)，NotNull/(空) (选填)
 * <p>
 * <p>
 * 4、@Validate(g1) ,@ApiGroup(g1):
 * ApiModelProperty/(空)/*ApiModelProperty(g2)*，Notnull(g1)（必填）
 * ApiModelProperty(g1)，Notnull/(空)/ Notnull(g2)(选填)
 * <p>
 * <p>
 * 5、@Validate(g1) ,@ApiGroup(g2):
 * ApiModelProperty(g1)（选填）
 * ApiModelProperty(g2)（选填）
 * Notnull(g1)（必填）
 * ApiModelProperty，Notnull(g1)（必填）*4.1
 * ApiModelProperty(g1)，Notnull（选填）*4.2
 * ApiModelProperty(g2)，Notnull（选填）
 * ApiModelProperty(g1)，Notnull(g1) （必填）
 * ApiModelProperty(g2)，Notnull(g2) （选填）
 * ApiModelProperty(g1)，Notnull(g2) （选填）*4.2
 * ApiModelProperty(g2)，Notnull(g1) （必填）*5.7
 * <p>
 * 思路：">":优先级      "-->":决定
 * 0、vaild
 * 1、Validate>ApiGroup
 * 2、Validate-->Notnull （1、匹配成功，必填，且后续操作忽略）
 *            -->ApiModelProperty （2、验证匹配成功，选填）
 *
 * ApiGroup-->ApiModelProperty （3、Validate完全匹配失败，尝试匹配，如果成功：选填）
 *
 * @see {@ReqyestParam} 包含必填验证
 */
@Component
@Order(Ordered.OperationRequestBodyReader)
public class OperationRequestBodyReader implements OperationBuilderPlugin {


    @Override
    public void apply(OperationContext context) {

        // 处理requestBody 请求参数
        requestBodyHandel(context);
    }


    /**
     * 处理requestBody 请求参数
     * <p>
     * 目前只处理POST的
     *
     * @param context
     */
    private void requestBodyHandel(OperationContext context) {

        // 处理POST请求
        if (HttpMethod.POST.equals(context.httpMethod())) {


        }


    }


    /**
     * 决定 required 属性
     */
    private void decisionRequired(){


        
    }



}
