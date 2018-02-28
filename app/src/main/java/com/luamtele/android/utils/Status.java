package com.luamtele.android.utils;

public  class Status {
    static private String status = "offline";
    static private int userId = 0;
    public static String getStatus() {
        return status;
    }

    public static void setStatus(String statuss) {
        status = statuss;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userIdd) {
        userId = userIdd;
    }
}
