package ru.clevertec.util;

import java.lang.reflect.Field;
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

    //{"id":"8cd1a26f-2fb0-48db-9da9-3d55cd962538","dateStart":"2023-11-07T12:34:56.000+0000","groups":{"A":[{"id":"ab649309-170a-4061-8b9a-79a7d94721c7","name":"Team A1","country":"Country A","players":[{"id":"ca0abcb0-c6e9-40cd-88b9-10b00975653c","name":"Player A1","surname":"Surname A1","dateBirth":"1990-01-01","number":7},{"id":"ea7902cc-b84c-4a88-816f-e13e790bbc57","name":"Player A2","surname":"Surname A2","dateBirth":"1992-05-15","number":10}]},{"id":"baf89272-073f-49c4-9059-64a196532986","name":"Team A2","country":"Country A","players":[{"id":"4a28f7f8-d915-4a9a-b834-e274b5658345","name":"Player A3","surname":"Surname A3","dateBirth":"1988-11-30","number":5},{"id":"d82498de-359b-4524-9420-6ec5426b17b7","name":"Player A4","surname":"Surname A4","dateBirth":"1993-07-20","number":9}]}],"B":[{"id":"1ac11052-e13a-4a54-92c0-1f9c933bc2f8","name":"Team B1","country":"Country B","players":[{"id":"8e3fd4ee-2d7a-4744-98a2-61835ad26b74","name":"Player B1","surname":"Surname B1","dateBirth":"1991-04-10","number":8},{"id":"66b0d93a-0b08-4ddf-b0a6-3b43c784b3f7","name":"Player B2","surname":"Surname B2","dateBirth":"1989-08-25","number":11}]},{"id":"f6f5cf8c-b64c-48e8-8fdf-fdedc7816ae6","name":"Team B2","country":"Country B","players":[{"id":"d7050647-3157-44cb-ab04-92b68290f217","name":"Player B3","surname":"Surname B3","dateBirth":"1994-02-28","number":6},{"id":"139f3bac-9924-4814-a80e-bd633e9316c2","name":"Player B4","surname":"Surname B4","dateBirth":"1995-09-05","number":12}]}]}}


    public static Object fromJson(String json, Class<?> clazz) {
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
            // System.out.println(map);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, Object> parseToMap(String json) {
        Map<String, Object> map = new HashMap<>();

        Pattern pattern = Pattern.compile(
                "(\"\\w+\" ?: ?\"?[\\w. \\-,]+\"?)"
        );
        //TODO вложенность
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            String[] keyVal = matcher.group().split(" ?: ?");
            String key = keyVal[0].substring(1, keyVal[0].length()-1);
            String value = keyVal[1];
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            map.put(key, value);
        }
        return map;
    }

    private static void setFieldValue(Object obj, Field field, Object value) throws IllegalAccessException {
        if (value == null) {
            field.set(obj, null);
        } else {
            Class<?> fieldType = field.getType();
            switch (fieldType.getSimpleName()) {
                case "String":
                    field.set(obj, value.toString());
                    break;
                case "Integer":
                case "int":
                    field.set(obj, Integer.parseInt(value.toString()));
                    break;
                case "UUID":
                    field.set(obj, UUID.fromString(value.toString()));
                    break;
                case "LocalDate":
                    field.set(obj, LocalDate.parse(value.toString()));
                    break;
                case "OffsetDateTime":
                    field.set(obj, OffsetDateTime.parse(value.toString()));
                    break;
                case "ZonedDateTime":
                    field.set(obj, ZonedDateTime.parse(value.toString()));
                    break;
                    //TODO еще классы
                default:
                    throw new IllegalArgumentException("Unsupported field type: " + fieldType.getSimpleName());
            }
        }
    }
}
