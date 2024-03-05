package com.hrt.example.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Name FileUtil
 * @Description FileUtil
 * @Author HRT
 * @Date 2024/3/5 9:43
 * @Version 1.0.0
 **/
public class FileUtil {

    // 遍历目录
    public static List<String> traverseDirectory(File directory) {
        List<String> fileAbsolutePathList = new ArrayList<>();
        // 遍历文件夹中的文件和子文件夹
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是文件夹，递归调用本方法
                    fileAbsolutePathList.addAll(traverseDirectory(file));
                } else {
                    fileAbsolutePathList.add(file.getAbsolutePath());
                }
            }
        }
        return fileAbsolutePathList;
    }
}
