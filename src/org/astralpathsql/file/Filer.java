package org.astralpathsql.file;

import org.astralpathsql.autoC.Hash;
import org.astralpathsql.autoC.form.Table;
import org.astralpathsql.been.COREINFORMATION;
import org.astralpathsql.properties.ProRead;
import org.astralpathsql.node.BalancedBinaryTree;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.astralpathsql.autoC.Hash.decode;
import static org.astralpathsql.autoC.Hash.encode;
import static org.astralpathsql.server.MainServer.*;
import static org.astralpathsql.server.MainServer.ta;

public class Filer {
    public static void createInFirst() {
        try {
            File file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "info.properties");			// 定义文件路径
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                file.createNewFile(); 					// 创建新的文件
                ProRead.write();
            }
            file = new File("." + File.separator + "apsql" + File.separator + "db" + File.separator + "default" + File.separator + "table.txt");			// 定义文件路径
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                file.createNewFile(); 					// 创建新的文件
            }
            table = file;
            file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "user.txt");			// 定义文件路径
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                file.createNewFile(); 					// 创建新的文件
                String a = encode("root:123456:ADMIN§");
                PrintWriter pu = new PrintWriter(new FileOutputStream(file), Boolean.parseBoolean("utf-8"));
                pu.print(a);
                pu.close();
            }
            file = new File("." + File.separator + "apsql" + File.separator + "db" + File.separator + "default" + File.separator + "save.ap");			// 定义文件路径
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                file.createNewFile(); 					// 创建新的文件
                PrintWriter pu = new PrintWriter(new FileOutputStream(file), Boolean.parseBoolean("utf-8"));
                pu.close();
            }
            database = file;//设置数据库
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            file = new File("." + File.separator + "apsql" + File.separator + "log" + File.separator + s.format(new Date()) + File.separator + "log.txt");			// 定义文件路径
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                file.createNewFile(); 					// 创建新的文件
                PrintWriter pu = new PrintWriter(new FileOutputStream(file), Boolean.parseBoolean("utf-8"));
                pu.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static ByteArrayOutputStream readSQL() {
        try {
            File file = database;
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                file.createNewFile();					// 创建新的文件
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
            File file = table;
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                file.createNewFile(); 					// 创建新的文件
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

    public static boolean addUser(String c) {
        try {
            File file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "user.txt");			// 定义文件路径
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
            String a = decode(new String(bos.toByteArray()));
            PrintWriter pu = new PrintWriter(new FileOutputStream(file),Boolean.parseBoolean("utf-8"));
            pu.print(encode(a + c + "§"));
            pu.close();
            return false;
        } catch (IOException e) {
            return true;
        }
    }
    public static String getUser() {
        try {
            File file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "user.txt");			// 定义文件路径
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
            String a = decode(new String(bos.toByteArray()));

            return a;
        } catch (IOException e) {
            return null;
        }
    }
    //删除用户
    public static String removeUser(String a) {
        try {
            String b = getUser();
            System.out.println(b);
            b = b.replaceAll(a + "§","");
            if (b.isEmpty()) {
                b = "root:123456:ADMIN§";
            }
            b = encode(b);
            File file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "user.txt");
            PrintWriter pu = new PrintWriter(new FileOutputStream(file), Boolean.parseBoolean("utf-8"));
            pu.print(b);
            pu.close();
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }
    public static Integer writeSQL() throws Exception{
        try {
            String a = tree.forW();

            File file = database;
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                file.createNewFile(); 					// 创建新的文件
            }

            PrintWriter pu = new PrintWriter(new FileOutputStream(file), Boolean.parseBoolean("utf-8"));
            pu.print(a);
            for (int x =0; x < Mtree.size(); x ++) {
                for(String key:ta.keySet()){
                    try {
                        if (!Mtree.get(key).forW().isEmpty()) {
                            pu.print(Mtree.get(key).forW());
                        }
                    } catch (Exception exception) {

                    }
                }
            }
            pu.close();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
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
                file.createNewFile(); 					// 创建新的文件
            }
            PrintWriter pu = new PrintWriter(new FileOutputStream(file,true), Boolean.parseBoolean("utf-8"));
            pu.println(ip);
            pu.close();
        } catch (Exception e) {

        }
    }
    public static void BanIPFile(String ip) {
        try {
            File file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "banip.txt");
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                file.createNewFile(); 					// 创建新的文件
            }
            PrintWriter pu = new PrintWriter(new FileOutputStream(file,true), Boolean.parseBoolean("utf-8"));
            pu.println(ip);
            pu.close();
        } catch (Exception e) {

        }
    }
    public static void checkIP() {
        try {
            File file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "banip.txt");
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在
                file.createNewFile();
            } else { 													// 文件不存在
                file.createNewFile(); 					// 创建新的文件
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
            banip = new String(bos.toByteArray());
        } catch (Exception e) {
        }
    }
    public static String readUser() throws IOException {
        File file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "user.txt");			// 定义文件路径

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
        return new String(bos.toByteArray());
    }
    public static void createDB(String db) throws IOException {
        File file = new File("." + File.separator + "apsql" + File.separator + "db" + File.separator + db + File.separator + "save.ap");			// 定义文件路径
        if (!file.getParentFile().exists()) { 							// 父路径不存在
            file.getParentFile().mkdirs(); 							// 创建父路径
        }
        if (file.exists()) {											// 文件存在

        } else { 													// 文件不存在
            file.createNewFile(); 					// 创建新的文件
        }
        file = new File("." + File.separator + "apsql" + File.separator + "db" + File.separator + db + File.separator + "table.txt");			// 定义文件路径
        file.createNewFile();
    }
    public static void removeDB(String db) {
        File file = new File("." + File.separator + "apsql" + File.separator + "db" + File.separator + db + File.separator + "save.ap");			// 定义文件路径
        file.delete();
        file = new File("." + File.separator + "apsql" + File.separator + "db" + File.separator + db + File.separator + "table.txt");			// 定义文件路径
        file.delete();
    }
    public static List<String> readDB() {
        File f[] = new File("." + File.separator + "apsql" + File.separator + "db" + File.separator).listFiles();
        List<String> a = new ArrayList<>();
        for(int x = 0 ; x < f.length ; x ++) {
            a.add(f[x].getName());
        }
        return a;
    }
    public static void writeLog(String w,String ip) throws FileNotFoundException {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        File file = new File("." + File.separator + "apsql" + File.separator + "log" + File.separator + s.format(new Date()) + File.separator + "log.txt");
        PrintWriter pu = new PrintWriter(new FileOutputStream(file,true), Boolean.parseBoolean("utf-8"));
        s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        pu.println(ip + "|" + s.format(new Date()) + "|" + w);
        pu.close();
    }
}