package com.crittercism.integrations;

public class PluginException extends Throwable {
    private static final String EMPTY_STRING = "";
    private static final long serialVersionUID = -1947260712494608235L;
    private String exceptionName;

    public PluginException(String msg, String stack) {
        this(EMPTY_STRING, msg, stack);
    }

    public PluginException(String name, String msg, String stack) {
        super(msg);
        this.exceptionName = null;
        setExceptionName(checkString(name), checkString(msg));
        setStackTrace(createStackTraceArrayFromStack(checkStringStack(stack)));
    }

    public PluginException(String name, String msg, String[] classStackElems, String[] methodStackElems, String[] fileStackElems, int[] lineNumberStackElems) {
        super(msg);
        this.exceptionName = null;
        setExceptionName(checkString(name), checkString(msg));
        setStackTrace(createStackTraceArrayFromStack(classStackElems, methodStackElems, fileStackElems, lineNumberStackElems));
    }

    public String getExceptionName() {
        return this.exceptionName;
    }

    public void setExceptionName(String name, String msg) {
        if (name.length() > 0) {
            this.exceptionName = name;
        } else {
            this.exceptionName = "JavaScript Exception";
        }
    }

    private StackTraceElement[] createStackTraceArrayFromStack(String[] stack) {
        int i;
        StackTraceElement[] stackTraceElementArr;
        int i2 = 0;
        if (stack.length < 2 || stack[0] == null || stack[1] == null || !stack[0].equals(stack[1])) {
            i = 0;
            stackTraceElementArr = null;
        } else {
            i = 1;
            stackTraceElementArr = new StackTraceElement[(stack.length - 1)];
        }
        if (i == 0) {
            stackTraceElementArr = new StackTraceElement[stack.length];
        }
        while (i2 < stack.length) {
            if (i2 != 0 || i == 0) {
                int i3;
                if (i != 0) {
                    i3 = i2 - 1;
                } else {
                    i3 = i2;
                }
                stackTraceElementArr[i3] = new StackTraceElement(EMPTY_STRING, stack[i2], EMPTY_STRING, -1);
            }
            i2++;
        }
        return stackTraceElementArr;
    }

    private StackTraceElement[] createStackTraceArrayFromStack(String[] classStackElems, String[] methodStackElems, String[] fileStackElems, int[] lineNumberStackElems) {
        int length = classStackElems.length;
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[length];
        for (int i = 0; i < length; i++) {
            int i2 = lineNumberStackElems[i];
            if (i2 <= 0) {
                i2 = -1;
            }
            stackTraceElementArr[i] = new StackTraceElement(checkString(classStackElems[i]), checkString(methodStackElems[i]), checkString(fileStackElems[i]), i2);
        }
        return stackTraceElementArr;
    }

    private static String checkString(String s) {
        if (s == null) {
            return EMPTY_STRING;
        }
        return s;
    }

    private static String[] checkStringStack(String stack) {
        if (stack == null) {
            return new String[0];
        }
        return stack.split("\\r?\\n");
    }

    public String toString() {
        String localizedMessage = getLocalizedMessage();
        String exceptionName = getExceptionName();
        return localizedMessage == null ? exceptionName : exceptionName + ": " + localizedMessage;
    }
}
