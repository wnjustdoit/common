package com.caiya.common.db.support;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * BeanUtil
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/23
 **/
public final class BeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);


    public static Map<String, String> beanToMap(Object bean) {
        try {
            return BeanUtils.describe(bean);
        } catch (Exception e) {
            logger.error("convert bean to map failed, bean: {}", ToStringBuilder.reflectionToString(bean));
        }
        return Collections.emptyMap();
    }

    public static <T> T mapToBean(Class<T> beanType, Map<String, Object> map) {
        try {
            T bean = beanType.getDeclaredConstructor().newInstance();
            BeanUtils.populate(bean, map);
            return bean;
        } catch (Exception e) {
            logger.error("convert map to bean failed, map: {}", map, e);
        }
        return null;
    }

    public static <T> void mapToBean(T bean, Map<String, Object> map) {
        try {
            BeanUtils.populate(bean, map);
        } catch (Exception e) {
            logger.error("convert map to bean failed, map: {}", map);
        }
    }

    private BeanUtil() {
    }
}
