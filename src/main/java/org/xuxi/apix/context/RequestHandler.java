package org.xuxi.apix.context;

import com.google.common.base.Optional;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.annotation.Annotation;
import java.util.Set;

public class RequestHandler {


    private final RequestMappingInfo requestMapping;

    private final HandlerMethod handlerMethod;


    public RequestHandler(RequestMappingInfo requestMapping, HandlerMethod handlerMethod) {
        this.requestMapping = requestMapping;
        this.handlerMethod = handlerMethod;
    }

    /**
     * 获取API名称
     *
     * @return
     */
    public String getName() {
        return handlerMethod.getMethod().getName();
    }


    /**
     * 查询注解是否存在
     *
     * @param annotation
     * @return
     */
    public boolean isAnnotatedWith(Class<? extends Annotation> annotation) {
        return null != AnnotationUtils.findAnnotation(handlerMethod.getMethod(), annotation);
    }

    /**
     * 查询注解信息
     * @param annotation
     * @param <T>
     * @return
     */
    public <T extends Annotation> Optional<T> findAnnotation(Class<T> annotation) {
        return Optional.fromNullable(AnnotationUtils.findAnnotation(handlerMethod.getMethod(), annotation));
    }

    /**
     * 获取Mapping bean类型
     *
     * @return
     */
    public Class<?> declaringClass() {
        return handlerMethod.getBeanType();
    }


    /**
     * 获取API组名称
     *
     * @return
     */
    public String groupName() {
        return controllerNameAsGroup(handlerMethod);
    }

    private static String controllerNameAsGroup(HandlerMethod handlerMethod) {
        Class<?> controllerClass = handlerMethod.getBeanType();
        return splitCamelCase(controllerClass.getSimpleName(), "-")
                .replace("/", "")
                .toLowerCase();
    }

    /**
     * 获取方法
     * @return
     */
    public Set<RequestMethod> supportedMethods() {
        return requestMapping.getMethodsCondition().getMethods();
    }

    /**
     * 获取请求条件
     *
     * @return
     */
    public PatternsRequestCondition getPatternsCondition() {
        return requestMapping.getPatternsCondition();
    }


    private static String splitCamelCase(String s, String separator) {
        if (s == null || s.equals("")) {
            return "";
        }
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                separator
        );
    }


}
