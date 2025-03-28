package org.xtracat.storage;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.xtracat.datatypes.Label;
import org.xtracat.datatypes.MusicGenre;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class CustomProblemHandler extends DeserializationProblemHandler {

    public interface TypeConverter<T> {
        T convert(String value) throws Exception;
    }

    private static final Map<Class<?>, TypeConverter<?>> converters = new HashMap<>();

    static {
        converters.put(Long.class, Long::valueOf);
        converters.put(long.class, converters.get(Long.class));
        converters.put(Integer.class, Integer::valueOf);
        converters.put(int.class, converters.get(Integer.class));
        converters.put(Double.class, Double::valueOf);
        converters.put(double.class, converters.get(Double.class));
        converters.put(Float.class, Float::valueOf);
        converters.put(float.class, converters.get(Float.class));
        converters.put(Boolean.class, Boolean::valueOf);
        converters.put(boolean.class, converters.get(Boolean.class));
        converters.put(ZonedDateTime.class,ZonedDateTime::parse);
        converters.put(MusicGenre.class,value -> MusicGenre.valueOf(value.toUpperCase()));
    }

    @Override
    public Object handleWeirdStringValue(DeserializationContext ctxt,
                                         Class<?> targetType,
                                         String valueToConvert,
                                         String failureMsg) throws IOException {

        TypeConverter<?> converter = converters.get(targetType);
        if (converter != null) {
            try {
                return converter.convert(valueToConvert);
            } catch (Exception e) {
//                System.out.println("sdk");
//                //System.out.println(valueToConvert);
//                if(targetType == long.class){
//                    System.out.println("bebeeb");
//                    return (long)1000;
//                }
//                if(targetType == double.class){
//                    return (double)-1000;
//                }
//                if (targetType == int.class) {
//                    return (int)1000;
//                }
                if(targetType == MusicGenre.class){
                    return MusicGenre.INVALID;
                }
                return null;
            }
        }


        return super.handleWeirdStringValue(ctxt, targetType, valueToConvert, failureMsg);

    }
}