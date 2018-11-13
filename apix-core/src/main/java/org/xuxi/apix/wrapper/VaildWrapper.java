package org.xuxi.apix.wrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 验证包装器
 */
public interface VaildWrapper {


    Class<? extends Annotation> getType();

    /**
     * 返回 Vaild group
     *
     * @param field 返回当前字段的Valid group
     * @return
     */
    Class<?>[] getGroup(Field field);


}
