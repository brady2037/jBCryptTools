package com.chinamobile.mcloud.sdk.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final int CHOICE_GENERATE = 0;
    private static final int CHOICE_VERIFY = 1;

    private static final String BG_GREEN = "\33[42m";
    private static final String BG_RED = "\33[41m";
    private static final String TEXT_WHITE = "\u001B[37m";
    private static final String TEXT_GREEN = "\u001B[32m";
    private static final String TEXT_RED = "\u001B[31m";

    public static void main(String[] args) {
        int choice;
        System.out.println("\t jBCrypt生成校验工具");
        System.out.println("—————————————— 操作选项 ——————————————");
        System.out.println("0.  生成哈希值");
        System.out.println("1.  校验哈希值");
        System.out.print("请输入数字：");
        Scanner scChoice = new Scanner(System.in);
        try {
            choice = scChoice.nextInt();
            readFile(choice);
        } catch (Exception e) {
            System.out.println("请输入正确的选择数字");
        }
    }

    private static void readFile(int choice) {
        String hashed = null;
        if (choice == CHOICE_VERIFY) {
            System.out.print("请输入hashed值：");
            Scanner scHashed = new Scanner(System.in);
            hashed = scHashed.nextLine();
        }

        System.out.print("请输入文件完整路径：");
        Scanner scPath = new Scanner(System.in);

        String path = scPath.nextLine();  //读取字符串型输入
        FileInputStream fis = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                System.out.println("正在读取文件...");
                StringBuilder sb = new StringBuilder();
                fis = new FileInputStream(file); // 内容是：abc
                int temp;
                //当temp等于-1时，表示已经到了文件结尾，停止读取
                while ((temp = fis.read()) != -1) {
                    sb.append((char) temp);
                }
                String contentStr = sb.toString();
//                long fileSize = contentStr.length();
//                System.out.println("读取大小:" + fileSize);
                if (choice == CHOICE_GENERATE) {
                    hashed = BCrypt.hashpw(contentStr, BCrypt.gensalt(12));
                    System.out.println("hashed值：\n");
                    System.out.println(hashed + "\n");
                } else if (choice == CHOICE_VERIFY) {
                    try {
                        boolean result = BCrypt.checkpw(contentStr, hashed);
                        if (result) {
                            System.out.println(TEXT_GREEN + "[OK] " + BG_GREEN + TEXT_WHITE + "校验通过");
                        } else {
//                        System.out.println("[FAILED]  校验失败，文件可能被篡改！");
                            System.out.println(TEXT_RED + "[FAILED] " + BG_RED + TEXT_WHITE + "校验失败，文件可能被篡改！");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("请输入正确的hashed值");
                    }
                } else {
                    System.out.println("无效选项");
                }
            } else {
                System.out.println("文件不存在请检查文件路径");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件读取异常");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
