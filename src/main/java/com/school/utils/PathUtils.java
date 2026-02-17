package com.school.utils;

import org.springframework.boot.system.ApplicationHome;

import java.io.File;

public class PathUtils {
    public static String getUploadPath() {
        ApplicationHome home = new ApplicationHome(PathUtils.class);
        File baseDir = home.getDir();
        if (baseDir.getAbsolutePath().contains("target") || baseDir.getAbsolutePath().contains("classes")) {
            while (baseDir.getName().equals("classes") || baseDir.getName().equals("target")) {
                baseDir = baseDir.getParentFile();
            }
        }

        File uploadDir = new File(baseDir, "upload");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        return uploadDir.getAbsolutePath() + File.separator;
    }
}
