package org.astralpathsql.print;

public class ColorTest {
    /**
     * @param colour  颜色代号：背景颜色代号(41-46)；前景色代号(31-36)
     * @param type    样式代号：0无；1加粗；3斜体；4下划线
     * @param content 要打印的内容
     */
    public static String getFormatLogString(String content, int colour, int type) {
        if (System.getProperty("os.name").contains("Windows")) {
            return content;
        }
        boolean hasType = type != 1 && type != 3 && type != 4;
        if (hasType) {
            return String.format("\033[%dm%s\033[0m", colour, content);
        } else {
            return String.format("\033[%d;%dm%s\033[0m", colour, type, content);
        }
    }

    public static void main(String[] args) {
        System.out.println("控制台颜色测试：");
        for (int colour = 30; colour < 49;colour ++) {
            for (int type = 0; type < 5; type ++) {
                System.out.println(getFormatLogString("[INFO]", colour, type) + "|代码:color" + colour + "|type:" + type + "----getFormatLogString(\"[INFO]\"," + colour + ","  + type + ")");
            }

        }
        System.out.println(getFormatLogString("[ 红色 ]", 31, 0));
        System.out.println(getFormatLogString("[ 黄色 ]", 32, 0));
        System.out.println(getFormatLogString("[ 橙色 ]", 33, 0));
        System.out.println(getFormatLogString("[ 蓝色 ]", 34, 0));
        System.out.println(getFormatLogString("[ 紫色 ]", 35, 0));
        System.out.println(getFormatLogString("[ 绿色 ]", 36, 0));
    }
}
