package org.astralpathsql.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputUtil {
    private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(
            new InputStreamReader(System.in));                        // 键盘缓冲输入流

    private InputUtil() {
    }

    public static String getString(String prompt) throws IOException {    // 键盘接收数据
        boolean flag = true;                                            // 输入标记
        String str = null;                                            // 接收输入字符串
        while (flag) {
            System.out.print(prompt);                                    // 提示信息
            str = KEYBOARD_INPUT.readLine();                            // 读取数据
            if (str == null || "".equals(str)) {                        // 保证不为null
                System.out.println("数据输入错误，请重新输入！！！");
            } else {
                flag = false;
            }
        }
        return str;
    }

    public static String getString(String prompt,int a) throws IOException {    // 键盘接收数据
        boolean flag = true;                                            // 输入标记
        String str = null;                                            // 接收输入字符串
        while (flag) {
            System.out.print(prompt);                                    // 提示信息
            str = KEYBOARD_INPUT.readLine();                            // 读取数据
            flag = false;
        }
        return str;
    }
}
