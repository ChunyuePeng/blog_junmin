package org.example.junmin.config;

public class UserContext {

    private static final ThreadLocal<Long> USER = new ThreadLocal<>();

    public static void setUser(Long username) {
        USER.set(username);
    }

    public static Long getUser() {
        return USER.get();
    }

    public static void clear() {
        USER.remove();
    }
}
