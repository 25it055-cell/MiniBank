package minibank.util;

import minibank.model.annotation.MaxLength;
import minibank.model.annotation.Positive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationValidator {

    public static String[] validate(Object obj) {
        if (obj == null) {
            return new String[]{"Object to validate cannot be null"};
        }

        List<String> errors = new ArrayList<>();
        Class<?> currentClass = obj.getClass();

        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object val = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)) {
                        Positive positive = field.getAnnotation(Positive.class);
                        if (val instanceof Number num) {
                            if (num.doubleValue() <= 0) {
                                errors.add(field.getName() + " " + positive.message());
                            }
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {
                        MaxLength maxLength = field.getAnnotation(MaxLength.class);
                        if (val instanceof String str) {
                            if (str != null && str.length() > maxLength.value()) {
                                errors.add(field.getName() + " length exceeds maximum allowed (" + maxLength.value() + ")");
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    errors.add("Unable to access field: " + field.getName());
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
