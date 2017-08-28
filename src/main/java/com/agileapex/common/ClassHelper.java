package com.agileapex.common;

public class ClassHelper {

    public String getCallerClassName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace != null && stackTrace.length > 3 && stackTrace[2] != null) {
            return stackTrace[3].toString();
        }
        return "";
    }
}
