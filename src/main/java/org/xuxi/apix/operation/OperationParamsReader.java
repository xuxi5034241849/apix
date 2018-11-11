package org.xuxi.apix.operation;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.xuxi.apix.annotations.ApiModelProperty;
import org.xuxi.apix.annotations.ApiParam;
import org.xuxi.apix.context.OperationContext;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Component
@Order(Ordered.OperationParamsReader)
public class OperationParamsReader implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext context) {


        /**
         * 加入文档逻辑：
         * 1、如果方法参数上有 {@link org.springframework.web.bind.annotation.RequestParam} 并且为基本数据类型或String类型
         * 2、如果 Mapping 提交方式为 POST, 方法参数上有 {@link org.springframework.web.bind.annotation.RequestBody}
         */

        // @RequestParam 处理
        requestParamHandel(context);

        // @RequestBody 处理 只处理POST的
        requestBodyHandel(context);


    }

    /**
     * {@RequestParam} 处理
     * <p>
     * 1、获取mapping 基本类型或 {String}类型
     *
     * @param context
     */
    private void requestParamHandel(OperationContext context) {
        Map<String, Object> params = newHashMap();

        context.getParameter().stream().forEach(methodParameter -> {

            methodParameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());

            Map<String, String> map = newHashMap();
            map.put("type", methodParameter.getParameterType().getName());
            map.put("describe", methodParameter.getParameterName());
            map.put("required", Boolean.FALSE.toString());


            Annotation apiParamAnnotation = methodParameter.getParameterAnnotation(ApiParam.class);
            if (apiParamAnnotation != null) {
                if (!StringUtils.isEmpty(((ApiParam) apiParamAnnotation).describe())) {
                    map.put("describe", ((ApiParam) apiParamAnnotation).describe());
                }
            }

            Annotation requestParamAnnotation = methodParameter.getParameterAnnotation(RequestParam.class);
            if (requestParamAnnotation != null) {
                map.put("required", Boolean.toString(((RequestParam) requestParamAnnotation).required()));
            }

            // 获取类型
            params.put(methodParameter.getParameterName(), map);
        });

        context.operationBuilder().setParams(params);
    }


    /**
     * {@RequestBody} 处理
     * <p>
     * 目前只处理POST的
     *
     * @param context
     */
    private void requestBodyHandel(OperationContext context) {

        if (HttpMethod.POST.equals(context.httpMethod())) {

            context.getParameterAnnotation(RequestBody.class).stream().forEach(methodParameter -> {

                Class<?> paramsBodyType = methodParameter.getParameterType();
                Annotation validAnnotation = methodParameter.getParameterAnnotation(Valid.class);
                Annotation validatedAnnotation = methodParameter.getParameterAnnotation(Validated.class);
                Annotation requestBodyAnnotation = methodParameter.getParameterAnnotation(RequestBody.class);


                Map<String, Object> fieldMap = getParamsBody(paramsBodyType, validAnnotation, validatedAnnotation, requestBodyAnnotation, 0);

                Map<String, Object> map = newHashMap();
                map.put("field", fieldMap);

                if (requestBodyAnnotation != null) {
                    map.put("required", Boolean.toString(((RequestBody) requestBodyAnnotation).required()));
                }


                context.operationBuilder().setParamsBody(map);

            });
        }


    }

    private Map<String, Object> getParamsBody(Class<?> paramsBodyType, Annotation validAnnotation, Annotation validatedAnnotation, Annotation requestBodyAnnotation, int count) {
        Field[] fields = paramsBodyType.getDeclaredFields();

        if (count++ > 10) { // 禁止无线递归
            return null;
        }

        Map<String, Object> fieldMap = newHashMap();
        int finalCount = count;
        Arrays.stream(fields).forEach(field -> {
            Class<?> c = field.getType();
            if (!c.equals(String.class)
                    && !c.equals(Integer.class)
                    && !c.equals(Long.class)
                    && !c.equals(Short.class)
                    && !c.equals(Character.class)
                    && !c.equals(Byte.class)
                    && !c.equals(Boolean.class)
                    && !c.equals(Double.class)
                    && !c.equals(Float.class)) {

                Map<String, Object> subMap = getParamsBody(c, validAnnotation, validatedAnnotation, requestBodyAnnotation, finalCount);
                fieldMap.put(field.getName(), subMap);
            } else {

                Map<String, String> map = newHashMap();
                map.put("type", c.getName());
                map.put("describe", field.getName());
                map.put("required", Boolean.FALSE.toString());


                Annotation annotation = AnnotationUtils.getAnnotation(field, ApiModelProperty.class);
                if (annotation != null) {
                    if (!StringUtils.isEmpty(((ApiModelProperty) annotation).describe())) {
                        map.put("describe", ((ApiModelProperty) annotation).describe());
                    }
                }

                boolean parentRequired = Boolean.FALSE;
                if (requestBodyAnnotation != null) {
                    parentRequired = ((RequestBody) requestBodyAnnotation).required();
                }

                if (validAnnotation != null && validatedAnnotation == null && parentRequired) {
                    map.put("required", Boolean.TRUE.toString());
                }

                if (validAnnotation == null && validatedAnnotation != null && parentRequired) {
                    Class<?>[] vaildGroup = ((Validated) validatedAnnotation).value();

                    Annotation notBlankAnnotation = AnnotationUtils.getAnnotation(field, NotBlank.class);
                    if (notBlankAnnotation != null) {
                        long index = Arrays.stream(((NotBlank) notBlankAnnotation).groups()).filter(group -> {
                            long gcount = Arrays.stream(vaildGroup).filter(g -> g.equals(group)).count();
                            return gcount > 0;
                        }).count();

                        map.put("required", Boolean.toString(index > 0));
                    }
                }

                fieldMap.put(field.getName(), map);
            }
        });

        return fieldMap;
    }

}
