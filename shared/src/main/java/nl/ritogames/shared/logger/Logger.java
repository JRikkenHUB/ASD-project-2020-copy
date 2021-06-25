package nl.ritogames.shared.logger;

public class Logger {
    static boolean active = false;
    
    public static void logMethodCall(Object object) {
        for (StackTraceElement s : Thread.currentThread().getStackTrace()) {
            if (s.getClassName().contains("junit")) {
                System.out.println(object.getClass().toString() + ", function: " + Thread.currentThread().getStackTrace()[2].getMethodName());
                return;
            }
        }
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        Logger.active = active;
    }
}
