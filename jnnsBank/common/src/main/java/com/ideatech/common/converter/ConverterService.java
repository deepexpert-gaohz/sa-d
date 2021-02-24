/*
 * Project Name: standard-code-base
 * File Name: ConverterService.java
 * Class Name: ConverterService
 *
 * Copyright 2014 Hengtian Software Inc
 *
 * 
 *
 * http://www.hengtiansoft.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ideatech.common.converter;

import com.ideatech.common.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.springframework.beans.BeansException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class Name: ConverterService
 * Description: provides a conversion utility in converting between PO and DTO back and forth.
 * 
 * @author SC
 * 
 */
@SuppressWarnings("rawtypes")
public final class ConverterService {

    private static final Map<String, BeanCopier> CACHED_COPIER_MAP = new ConcurrentHashMap<String, BeanCopier>();
    private static final Map<String, ObjectConverter> CACHED_CUSTOM_CONVERTER_MAP = new ConcurrentHashMap<String, ObjectConverter>();
    private static final String PO = "Po";
    private static final String DTO = "Dto";

    private ConverterService() {

    }

    /** Overloaded methods */

    public static <T, F> F convert(T source, Class<F> targetClass) {
        return convert(source, targetClass, new DeepConverter(), null);
    }

    public static <T, F> F convert(T source, F target) {
        return convert(source, target, new DeepConverter(), null);
    }

    public static <T, F> F convert(T source, Class<F> targetClass, Class<? extends ObjectConverter> customConverterClass) {
        return convert(source, targetClass, new DeepConverter(), customConverterClass);
    }

    public static <T, F> List<F> convertToList(Collection<T> sourceList, Class<F> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        List<F> targetList = new ArrayList<F>(sourceList.size());
        for (T src : sourceList) {
            F target = convert(src, targetClass);
            targetList.add(target);
        }
        return targetList;
    }

    /** Private methods */

    @SuppressWarnings("unchecked")
    private static <T, F> void copy(T source, F target, Converter converter,
            Class<? extends ObjectConverter> customConverterClass) {
        BeanCopier beanCopier = getBeanCopierInstance(source, target.getClass(), converter);
        beanCopier.copy(source, target, converter);
        ObjectConverter customConverter = getCustomConverterInstance(customConverterClass);
        if (customConverter != null) {
            if (target.getClass().getName().endsWith(PO) || target.getClass().getName().endsWith(DTO)) {
                customConverter.convertFromDomain(source, target);
            } else if (source.getClass().getName().endsWith(PO) || source.getClass().getName().endsWith(DTO)) {
                customConverter.convertToDomain(source, target);
            }
        }
    }

    private static <T, F> BeanCopier getBeanCopierInstance(T source, Class<F> targetClass, Converter converter) {
        String key = source.getClass().getName() + "#" + targetClass.getName();
        BeanCopier beanCopier = CACHED_COPIER_MAP.get(key);
        if (beanCopier == null) {
            synchronized (CACHED_COPIER_MAP) {
                beanCopier = CACHED_COPIER_MAP.get(key);
                if (beanCopier == null) {
                    beanCopier = TypeAwareBeanCopier.instantiate(source.getClass(), targetClass, converter != null);
                    CACHED_COPIER_MAP.put(key, beanCopier);
                }
            }
        }
        return beanCopier;
    }

    private static <T, F> ObjectConverter getCustomConverterInstance(
            Class<? extends ObjectConverter> customConverterClass) {
        if (customConverterClass == null) {
            return null;
        }
        String key = customConverterClass.getName();
        ObjectConverter converter = CACHED_CUSTOM_CONVERTER_MAP.get(key);
        if (converter == null) {
            synchronized (CACHED_CUSTOM_CONVERTER_MAP) {
                try {
                    converter = ApplicationContextUtil.getBean(customConverterClass);
                } catch (BeansException e) {
                }
                if (converter == null) {
                    try {
                        converter = customConverterClass.newInstance();
                        CACHED_CUSTOM_CONVERTER_MAP.put(key, converter);
                    } catch (InstantiationException e) {
                        return null;
                    } catch (IllegalAccessException e) {
                        return null;
                    }
                }
            }
        }
        return converter;
    }

    /**
     * Description: this method will be removed.
     * 
     * @param source
     * @param targetClass
     * @param converter
     * @param customConverterClass
     * @return
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    private static <T, F> F convert(T source, Class<F> targetClass, Converter converter,
            Class<? extends ObjectConverter> customConverterClass) {
        if (source == null || targetClass == null) {
            return null;
        }
        if (source.getClass().equals(targetClass)) {
            return (F) source;
        }

        try {
            F target = targetClass.newInstance();
            copy(source, target, converter, customConverterClass);
            return target;
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static <T, F> F convert(T source, F target, Converter converter,
            Class<? extends ObjectConverter> customConverterClass) {
        if (source == null || target == null) {
            return null;
        }

        copy(source, target, converter, customConverterClass);
        return target;
    }

}
