package ru.clevertec.util;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class JsonConverter {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static String toJson(Object obj) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                jsonBuilder
                        .append("\"")
                        .append(field.getName())
                        .append("\":")
                        .append(parseValue(field.get(obj)))
                        .append(",");

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (jsonBuilder.charAt(jsonBuilder.length() - 1) == ',') {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }

        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    private static String parseValue(Object obj) {
        if (obj == null)
            return "null";

        if (obj.getClass().isPrimitive() || obj instanceof Number || obj instanceof Boolean)
            return obj.toString();

        if (obj instanceof Character || obj instanceof String || obj instanceof UUID) {
            return "\"" + obj + "\"";
        }

        if (obj instanceof LocalDate) {
            return "\"" + obj + "\"";
        }

        if (obj instanceof OffsetDateTime) {
            return "\"" +  ((OffsetDateTime) obj).format(FORMATTER) + "\"";
        }

        if (obj instanceof ZonedDateTime) {
            return "\"" + obj + "\"";
        }

        if (obj instanceof Collection<?> collection) {
            StringBuilder result = new StringBuilder("[");
            for (Object object : collection) {
                result.append(parseValue(object));
                result.append(",");
            }

            if (result.charAt(result.length() - 1) == ',') {
                result.deleteCharAt(result.length() - 1);
            }

            result.append("]");
            return result.toString();
        }

        if (obj instanceof Map<?, ?> map) {
            StringBuilder result = new StringBuilder("{");
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result
                        .append("\"")
                        .append(entry.getKey())
                        .append("\":")
                        .append(parseValue(entry.getValue()))
                        .append(",");
            }

            if (result.charAt(result.length() - 1) == ',') {
                result.deleteCharAt(result.length() - 1);
            }

            result.append("}");
            return result.toString();
        }

        return toJson(obj);
    }
}
