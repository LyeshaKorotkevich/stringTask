package ru.clevertec.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonConverter {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static String toJson(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
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

    public static Object fromJson(String json, Class<?> clazz) {
        if (json == null) {
            return null;
        }
        try {
            Object obj = clazz.getDeclaredConstructor().newInstance();
            Map<String, Object> map = parseToMap(json);

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (map.containsKey(fieldName)) {
                    Object value = map.get(fieldName);
                    setFieldValue(obj, field, value);
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, Object> parseToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        Deque<Map<String, Object>> deque = new ArrayDeque<>();
        deque.push(map);

        Pattern pattern = Pattern.compile("\"(.*?)\"\\s*:\\s*(\\{.*?\\}|\".*?\"|\\d+|\\[.*?\\]|null|true|false)");
        Matcher matcher = pattern.matcher(json);

        while (matcher.find()) {
            String key = matcher.group(1);
            String valueString = matcher.group(2);
            Object value = parseValue(valueString);

            // FIXME не работает с Map
            Map<String, Object> currentMap = deque.peek();
            if (currentMap.containsKey(key)) {
                if (currentMap.get(key) instanceof Map) {
                    Map<String, Object> nestedMap = (Map<String, Object>) currentMap.get(key);
                    deque.push(nestedMap);
                } else {
                    Map<String, Object> nestedMap = new HashMap<>();
                    if (currentMap.get(key) instanceof List) {
                        ((List<Object>) currentMap.get(key)).add(nestedMap);
                    } else {
                        currentMap.put(key, nestedMap);
                    }
                    deque.push(nestedMap);
                }
            } else {
                currentMap.put(key, value);
            }

            if (value instanceof Map) {
                deque.push((Map<String, Object>) value);
            }
        }

        return map;
    }


    private static Object parseValue(String valueString) {

        if (valueString.startsWith("\"") && valueString.endsWith("\"")) {
            return valueString.substring(1, valueString.length() - 1);
        } else if (valueString.startsWith("{") && valueString.endsWith("}")) {
            return parseToMap(valueString);
        } else if (valueString.startsWith("[") && valueString.endsWith("]")) {
            return parseList(valueString);
        }
        return valueString.trim();
    }

    private static List<Object> parseList(String arrayString) {
        List<Object> list = new ArrayList<>();
        String content = arrayString.substring(1, arrayString.length() - 1);
        Matcher matcher = Pattern.compile("(\\{.*?\\}|\".*?\"|\\d+)").matcher(content);

        while (matcher.find()) {
            String elementString = matcher.group(1);
            list.add(parseValue(elementString.trim()));
        }

        return list;
    }

    private static void setFieldValue(Object obj, Field field, Object value) throws IllegalAccessException {
        if (value == null) {
            field.set(obj, null);
        }else {
            Class<?> fieldType = field.getType();
            if (fieldType.isAssignableFrom(List.class)) {
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> listElementType = (Class<?>) listType.getActualTypeArguments()[0];
                List<Object> listValue = new ArrayList<>();
                for (Object element : (List<?>) value) {
                    listValue.add(parseElement(listElementType, element));
                }
                field.set(obj, listValue);
            } else {
                field.set(obj, parseElement(fieldType, value));
            }
        }
    }

    private static Object parseElement(Class<?> elementType, Object elementValue) {
        if (elementValue == null) {
            return null;
        }

        if (elementValue instanceof Map) {
            Map<?, ?> elementMap = (Map<?, ?>) elementValue;
            Object instance = null;
            try {
                instance = elementType.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (instance != null) {
                for (Field field : elementType.getDeclaredFields()) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (elementMap.containsKey(fieldName)) {
                        Object fieldValue = elementMap.get(fieldName);
                        try {
                            setFieldValue(instance, field, fieldValue);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return instance;
        }

        return switch (elementType.getSimpleName()) {
            case "String" -> elementValue.toString();
            case "Byte", "byte" -> Byte.parseByte(elementValue.toString());
            case "Short", "short" -> Short.parseShort(elementValue.toString());
            case "Integer", "int" -> Integer.parseInt(elementValue.toString());
            case "Long", "long" -> Long.parseLong(elementValue.toString());
            case "Float", "float" -> Float.parseFloat(elementValue.toString());
            case "Double", "double" -> Double.parseDouble(elementValue.toString());
            case "Character", "char" -> elementValue.toString().charAt(0);
            case "Boolean", "bool" -> Boolean.parseBoolean(elementValue.toString());
            case "UUID" -> UUID.fromString(elementValue.toString());
            case "LocalDate" -> LocalDate.parse(elementValue.toString());
            case "OffsetDateTime" -> OffsetDateTime.parse(elementValue.toString());
            case "ZonedDateTime" -> ZonedDateTime.parse(elementValue.toString());
            default -> throw new IllegalArgumentException("Unsupported field type: " + elementType.getSimpleName());
        };
    }

}
