package org.xuxi.apix.operation;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.xuxi.apix.context.OperationContext;


/**
 * 处理 RequestMapping 的 requestBody
 * 处理非基础类型&String
 *
 * @see @ReqyestParam 包含必填验证
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


            /**
             * 处理这几种情况
             * 所有参与计算的对象，必须被 @RequestBody标注
             *
             *
             * 1、@Valid ：
             *      ApiModelProperty (选填)
             *      Notnull（必填）
             *      其他不显示
             *
             * 2、@Validate(g1) ：
             *      ApiModelProperty/ApiModelProperty(g1)/(空)/*ApiModelProperty(g2)*，Notnull(g1)（必填）
             *      ApiModelProperty(g1)，Notnull/ Notnull(g2)/(空)(选填)
             *
             *      ApiModelProperty，Notnull （不显示）
             *      ApiModelProperty(g2)，Notnull(g2) （不显示）
             *
             * 3、@ApiGroup(g1) ：
             *      ApiModelProperty(g1) (选填)
             *      ApiModelProperty(g1)，NotNull (选填)
             *
             *      NotNull (不显示)
             *      NotNull(g1) (不显示) todo 这个比较特殊
             *      ApiModelProperty (不显示)
             *      ApiModelProperty(g2) (不显示)
             *
             * 4、@Validate(g1) ,@ApiGroup(g1):
             *      ApiModelProperty/(空)/*ApiModelProperty(g2)*，Notnull(g1)（必填）
             *      ApiModelProperty(g1)，Notnull/(空)/ Notnull(g2)(选填)
             *
             *      ApiModelProperty，Notnull （不显示）
             *      ApiModelProperty(g2)（不显示）
             *      Notnull(g2) （不显示）
             *      ApiModelProperty(g2)，Notnull(g2) （不显示）
             *
             * 5、@Validate(g1) ,@ApiGroup(g2):
             *      ApiModelProperty(g1)（选填）
             *      ApiModelProperty(g2)（选填）
             *      Notnull(g1)（必填）
             *      *Notnull(g2)*（不显示）
             *      ApiModelProperty，Notnull(g1)（必填）
             *      *ApiModelProperty，Notnull(g2)*（不显示）
             *      ApiModelProperty(g1)，Notnull（选填）
             *      ApiModelProperty(g2)，Notnull（选填）
             *      ApiModelProperty(g1)，Notnull(g1) （必填）
             *      ApiModelProperty(g2)，Notnull(g2) （选填）
             *      ApiModelProperty(g1)，Notnull(g2) （选填）
             *      ApiModelProperty(g2)，Notnull(g1) （必填）
             *
             *      ApiModelProperty (不显示)
             *      Notnull (不显示)
             *
             *
             *      xuchao思路：">":优先级      "-->":决定
             *          1、Validate>ApiGroup
             *
             *          2、Validate-->Notnull
             *                     -->ApiModelProperty
             *
             *              ApiGroup-->ApiModelProperty
             *
             *
             */


        }


    }

}
