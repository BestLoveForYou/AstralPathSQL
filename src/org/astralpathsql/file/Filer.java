package org.astralpathsql.file;

import org.astralpathsql.properties.ProRead;
import org.astralpathsql.node.BalancedBinaryTree;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Filer {
    public static void createInFirst() {
        try {
            File file = new File("." + File.separator + "apsql" + File.separator + "save.txt");			// 定义文件路径
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                System.out.println(file.createNewFile()); 					// 创建新的文件
            }
            file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "info.properties");			// 定义文件路径
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                System.out.println(file.createNewFile()); 					// 创建新的文件
                ProRead.write();
            }
            file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "table.txt");			// 定义文件路径
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                System.out.println(file.createNewFile()); 					// 创建新的文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ByteArrayOutputStream readSQL() {
        try {
            File file = new File("." + File.separator + "apsql" + File.separator + "save.txt");			// 定义文件路径
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                System.out.println(file.createNewFile()); 					// 创建新的文件
            }

            FileInputStream input = new FileInputStream(file);					// 文件输入流
            FileChannel channel = input.getChannel(); 							// 获取文件通道
            ByteBuffer buffer = ByteBuffer.allocate(100); 						// 开辟缓冲大小
            ByteArrayOutputStream bos = new ByteArrayOutputStream(); 			// 内存输出流
            int count = 0; 													// 保存读取个数
            while ((count = channel.read(buffer)) != -1) {						// 缓冲区读取
                buffer.flip(); 												// 重置缓冲区
                while (buffer.hasRemaining()) { 								// 是否还有数据
                    bos.write(buffer.get()); 									// 内容写入内存流
                }
                buffer.clear();												// 清空缓冲区
            }
            channel.close();													// 关闭通道
            input.close();
            return bos;
        } catch (IOException e) {
            return null;
        }
    }
    public static ByteArrayOutputStream readTable() {
        try {
            File file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "table.txt");			// 定义文件路径
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                System.out.println(file.createNewFile()); 					// 创建新的文件
            }

            FileInputStream input = new FileInputStream(file);					// 文件输入流
            FileChannel channel = input.getChannel(); 							// 获取文件通道
            ByteBuffer buffer = ByteBuffer.allocate(100); 						// 开辟缓冲大小
            ByteArrayOutputStream bos = new ByteArrayOutputStream(); 			// 内存输出流
            int count = 0; 													// 保存读取个数
            while ((count = channel.read(buffer)) != -1) {						// 缓冲区读取
                buffer.flip(); 												// 重置缓冲区
                while (buffer.hasRemaining()) { 								// 是否还有数据
                    bos.write(buffer.get()); 									// 内容写入内存流
                }
                buffer.clear();												// 清空缓冲区
            }
            channel.close();													// 关闭通道
            input.close();
            return bos;
        } catch (IOException e) {
            return null;
        }
    }
    public static Integer writeSQL(BalancedBinaryTree tree) throws Exception{
        try {
            String a = tree.forW();
            File file = new File("." + File.separator + "apsql" + File.separator + "save.txt");
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                System.out.println(file.createNewFile()); 					// 创建新的文件
            }

            PrintWriter pu = new PrintWriter(new FileOutputStream(file));
            pu.print(a);
            pu.close();
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }
    public static void addIPFile(String ip) {
        try {
            File file = new File("." + File.separator + "apsql" + File.separator + "ip.txt");
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                System.out.println(file.createNewFile()); 					// 创建新的文件
            }
            PrintWriter pu = new PrintWriter(new FileOutputStream(file,true));
            pu.println(ip);
            pu.close();
        } catch (Exception e) {

        }
    }
}
