package org.acq.lz.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    public static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    //===============================序列化================//
    @SneakyThrows
    public static String toString(Object obj) {
        if (obj == null) return null;
        if (obj.getClass() == String.class)  return Convert.toStr(obj);
        return MAPPER.writeValueAsString(obj);
    }
    //==========================反序列化===============//
    //对象反序列化
    @SneakyThrows(value={IOException.class})
    public static <T> T toBean(String json, Class<T> tClass) {
        if(StrUtil.isEmpty(json)) return null;
        return MAPPER.readValue(json, tClass);
    }
    //集合反序列化
    @SneakyThrows(value={IOException.class})
    public static <E> List<E> toList(String json, Class<E> eClass) {
        if(StrUtil.isEmpty(json)) return null;
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, Bean.class);
        return MAPPER.readValue(json,javaType);
    }
    //Map集合反序列化
    @SneakyThrows(value={IOException.class})
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        if(StrUtil.isEmpty(json)) return null;
        JavaType javaType = MAPPER.getTypeFactory()
                .constructParametricType(HashMap.class,String.class, Bean.class);
        return MAPPER.readValue(json, javaType);
    }
    //复杂对象反序列化
    @SneakyThrows(value={IOException.class})
    public static <T> T nativeRead(String json, TypeReference<T> type) {
        if(StrUtil.isEmpty(json)) return null;
        return MAPPER.readValue(json, type);
    }
}
