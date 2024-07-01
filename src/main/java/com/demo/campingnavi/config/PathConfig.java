package com.demo.campingnavi.config;

import java.io.File;

public class PathConfig {
    public static String path = System.getProperty("user.dir")+ File.separator;

    public static String realPath(String file) {
        return path + file;
    }

    public static String intelliJPath = "FoodNavi\\src\\main\\resources\\static\\assets\\memberImages\\";

    public static String eclipsePath = "src\\main\\resources\\static\\assets\\memberImages\\";

    public static String intelliJPath_QNA = "FoodNavi\\src\\main\\resources\\static\\assets\\qnaImages\\";

    public static String eclipsePath_QNA = "src\\main\\resources\\static\\assets\\qnaImages\\";

    public static String intelliJPath_REPLY = "FoodNavi\\src\\main\\resources\\static\\assets\\replyImages\\";

    public static String eclipsePath_REPLY = "src\\main\\resources\\static\\assets\\replyImages\\";

    public static String existsPath = realPath(intelliJPath.substring(0, intelliJPath.length() - 1));

    public static boolean isExistsPath() {
        File file = new File(existsPath);
        boolean exists = file.exists();

        return  exists;
    }
}
