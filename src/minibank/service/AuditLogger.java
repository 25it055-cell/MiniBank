package minibank.service;

import minibank.model.annotation.Audit;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Reflective audit logger (Practical 14 Part B).
 * Inspects methods for @Audit annotation and records operations.
 */
public class AuditLogger {
    private static final List<String> auditTrail = new ArrayList<>();

    public static synchronized void logIfAudited(Class<?> serviceClass, String methodName, Object[] args) {
        try {
            for (Method m : serviceClass.getDeclaredMethods()) {
                if (m.getName().equals(methodName) && m.isAnnotationPresent(Audit.class)) {
                    Audit audit = m.getAnnotation(Audit.class);
                    String action = audit.action().isEmpty() ? methodName : audit.action();
                    String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
                    StringBuilder params = new StringBuilder();
                    if (args != null) {
                        for (int i = 0; i < args.length; i++) {
                            params.append(args[i]);
                            if (i < args.length - 1) params.append(", ");
                        }
                    }
                    String record = String.format("[%s] AUDIT LOG: Action '%s' invoked on %s(%s)",
                            time, action, methodName, params);
                    auditTrail.add(record);
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("Audit logging failure: " + e.getMessage());
        }
    }

    public static synchronized List<String> getAuditTrail() {
        return new ArrayList<>(auditTrail);
    }
}
