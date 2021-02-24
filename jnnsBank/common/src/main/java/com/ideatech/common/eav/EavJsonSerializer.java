package com.ideatech.common.eav;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ideatech.common.util.ReflectionUtil;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author liangding
 * @create 2018-07-23 下午7:29
 **/
public class EavJsonSerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();

        List<Field> fieldsIncludingSuperClasses = ReflectionUtil.getFieldsIncludingSuperClasses(o.getClass());
        for (Field fieldsIncludingSuperClass : fieldsIncludingSuperClasses) {
            Method getter = ReflectionUtil.getGetter(o.getClass(), fieldsIncludingSuperClass);
            Object field = ReflectionUtils.invokeMethod(getter, o);

            if (fieldsIncludingSuperClass.isAnnotationPresent(ExtendedAttribute.class) && field instanceof Map) {
                Map map = (Map) field;
                for (Object o1 : map.keySet()) {
                    jsonGenerator.writeFieldName(fieldsIncludingSuperClass.getName() + "[" + o1 + "]");
                    Object value = map.get(o1);
                    jsonGenerator.writeObject(value);
                }
            } else {
                jsonGenerator.writeFieldName(fieldsIncludingSuperClass.getName());
                jsonGenerator.writeObject(field);
            }
        }
        jsonGenerator.writeEndObject();
    }
}
