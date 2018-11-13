package org.xuxi.apix.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.xuxi.apix.annotations.ApiGroup;
import org.xuxi.apix.annotations.ApiModelProperty;
import org.xuxi.apix.annotations.ApiParam;
import org.xuxi.apix.context.OperationContext;
import org.xuxi.apix.wrapper.VaildWrapper;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

//@Component
//@Order(Ordered.OperationParamsReader)
public class OperationParamsReader implements OperationBuilderPlugin {

    private List<VaildWrapper> vaildWrapper;

    @Autowired
    public OperationParamsReader(List<VaildWrapper> vaildWrapper) {
        this.vaildWrapper = vaildWrapper;
    }

    @Override
    public void apply(OperationContext context) {


        /**
         * 加入文档逻辑：
         * 1、如果方法参数上有 {@link org.springframework.web.bind.annotation.RequestParam} 并且为基本数据类型或String类型
         * 2、如果 Mapping 提交方式为 POST, 方法参数上有 {@link org.springframework.web.bind.annotation.RequestBody}
         */
        // @RequestBody 处理 只处理POST的
        requestBodyHandel(context);


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

            // 只获取带有 @RequestBody 的参数对象
            context.getParameterAnnotation(RequestBody.class).stream().forEach(methodParameter -> {

                Annotation validAnnotation = methodParameter.getParameterAnnotation(Valid.class);
                Annotation validatedAnnotation = methodParameter.getParameterAnnotation(Validated.class);
                Annotation requestBodyAnnotation = methodParameter.getParameterAnnotation(RequestBody.class);
                Annotation apiGroupAnnotation = methodParameter.getParameterAnnotation(ApiGroup.class);

                // 默认false,然后获取 RequestBody的 required值覆盖
                Boolean required = Boolean.FALSE;
                if (requestBodyAnnotation != null) {
                    required = ((RequestBody) requestBodyAnnotation).required();
                }

                // 递归获取字段内容
                List<Map<String, Object>> fieldList = getParamsBody(methodParameter.getParameterType(), required, validAnnotation, validatedAnnotation, apiGroupAnnotation, 0);

                Map<String, Object> map = newHashMap();
                map.put("fields", fieldList);
                map.put("required", required);


                context.operationBuilder().setParamsBody(map);

            });
        }
    }

    /**
     * 遍历 request body对象信息
     *
     * @param paramsBodyType
     * @param required
     * @param validAnnotation
     * @param validatedAnnotation
     * @param count
     * @return
     */
    private List<Map<String, Object>> getParamsBody(Class<?> paramsBodyType, Boolean required, Annotation validAnnotation, Annotation validatedAnnotation, Annotation apiGroupAnnotation, int count) {
        Field[] fields = paramsBodyType.getDeclaredFields();

        if (count++ > 10) { // 禁止无线递归
            return null;
        }
        int finalCount = count; //计数器，防止无限递归


        // 遍历当前类型的所有成员属性
        List<Map<String, Object>> fieldList = newArrayList();
        Arrays.stream(fields).filter(field -> {

            // 获取 字段组 并集操作
            Set<Class<?>> group = newHashSet();
            // 获取 ApiModelProperty group组
            Annotation annotation = field.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                group.addAll(Arrays.asList(((ApiModelProperty) annotation).groups()));
            }

            // 如果这个字段，存在包装器的内容，那么我就返回这个分组类型
            vaildWrapper.stream().forEach(wrapper -> {
                Class<?>[] result = wrapper.getGroup(field);
                if (result != null) {
                    group.addAll(Arrays.asList(result));
                }
            });

            // 获取 Validated 或 ApiGroup 并集操作
            Set<Class<?>> groupSet = newHashSet();
            if (validatedAnnotation != null) {
                groupSet.addAll(Arrays.asList(((Validated) validatedAnnotation).value()));

            }
            if (apiGroupAnnotation != null) {
                groupSet.addAll(Arrays.asList(((ApiGroup) apiGroupAnnotation).value()));
            }

            if (marchClassArray(group.toArray(), groupSet.toArray())) {
                return true;
            }


            // 只要有Valid ，慢不管 group
            if (validAnnotation != null && group.size() > 0) {
                return true;
            }


            // 如果有 validAnnotation || validatedAnnotation ，就参加计算
            if (validAnnotation != null || validatedAnnotation != null) {
                long num = vaildWrapper.stream().filter(vaild ->
                        field.getAnnotation(vaild.getType()) != null)
                        .count();
                return num > 0;
            }


            return false;

        }).forEach(field -> {

            Class<?> c = field.getType();

            // 只验证八大基础类型&String
            if (!c.equals(String.class)
                    && !c.equals(Integer.class)
                    && !c.equals(Long.class)
                    && !c.equals(Short.class)
                    && !c.equals(Character.class)
                    && !c.equals(Byte.class)
                    && !c.equals(Boolean.class)
                    && !c.equals(Double.class)
                    && !c.equals(Float.class)) {


                // 获取子字段信息
                List<Map<String, Object>> subList = getParamsBody(c, false  // 这里的false 临时写的，需要验证
                        , validAnnotation, validatedAnnotation, apiGroupAnnotation, finalCount);

                Map<String, Object> map = newHashMap();
                map.put("subFields", subList);
                map.put("type", c.getName());
                map.put("describe", field.getName());

                fieldList.add(map);
            } else {

                Map<String, Object> map = newHashMap();
                map.put("field", field.getName());
                map.put("type", c.getName());
                map.put("describe", field.getName());
                map.put("required", Boolean.FALSE);

                // 获取描述信息
                getFieldDescribe(field, map);

                // 检测是否必填
                detectionRequired(required, validAnnotation, validatedAnnotation, field, map);

                fieldList.add(map);
            }

        });

        return fieldList;
    }



    /**
     * 获取字段描述信息
     *
     * @param field 当前字段
     * @param map   结果集
     */
    private void getFieldDescribe(Field field, Map<String, Object> map) {
        Annotation annotation = AnnotationUtils.getAnnotation(field, ApiModelProperty.class);
        if (annotation != null) {
            if (!StringUtils.isEmpty(((ApiModelProperty) annotation).describe())) {
                map.put("describe", ((ApiModelProperty) annotation).describe());
            }
        }
    }




    /**
     * 检查字段是否必填
     *
     * @param required            父级如果是false，那子字段默认不必填
     * @param validAnnotation     @Valid
     * @param validatedAnnotation @vaildGroup
     * @param field               当前遍历的字段
     * @param map                 结果集
     */
    private void detectionRequired(boolean required, Annotation validAnnotation, Annotation validatedAnnotation,
                                   Field field, Map<String, Object> map) {
        if (required) {

            // 检查 {@Valid}
            if (validAnnotation != null && validatedAnnotation == null) {

                // 当前字段可能只有 ApiModelProperty，则为 false 选填

                long count = vaildWrapper.stream().filter(vaild -> {
                    Annotation annotation = field.getAnnotation(vaild.getType());
                    return annotation != null;
                }).count();

                if (count > 0) {
                    map.put("required", Boolean.TRUE);
                }
            }

            // 检查 {@Validated}
            if (validAnnotation == null && validatedAnnotation != null) {
                Class<?>[] vaildGroup = ((Validated) validatedAnnotation).value();

                // 遍历 遍历 VaildWrapper 包装的子类，以此检查
                map.put("required", decideGroupRequired(field, vaildGroup));
            }
        }
    }

    /**
     * 遍历 VaildWrapper 包装的子类，查询field是否有其中之一，如果没有返回false，如果存在 返回true
     *
     * @param field
     * @param vaildGroup
     */
    private Boolean decideGroupRequired(Field field, Class<?>[] vaildGroup) {
        long count = vaildWrapper.stream().filter(vaild -> {
            Class<?>[] group = vaild.getGroup(field);
            if (group == null) {
                return false;
            }

            return marchClassArray(vaildGroup, group);
        }).count();

        return count > 0;
    }


    /**
     * 检查两个数组有没有相同的对象
     *
     * @param aClass
     * @param bClass
     * @return
     */
    private boolean marchClassArray(Class<?>[] aClass, Class<?>[] bClass) {

        long index = Arrays.stream(aClass).filter(a -> {
            long count = Arrays.stream(bClass).filter(b -> a.equals(b)).count();
            return count > 0;
        }).count();

        return index > 0;
    }


    /**
     * 检查两个数组有没有相同的对象
     *
     * @param aObject
     * @param bObject
     * @return
     */
    private boolean marchClassArray(Object[] aObject, Object[] bObject) {

        long index = Arrays.stream(aObject).filter(a -> {
            long count = Arrays.stream(bObject).filter(b -> a.equals(b)).count();
            return count > 0;
        }).count();

        return index > 0;
    }


}
