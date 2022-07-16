package org.astralpathsql.been;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.astralpathsql.autoC.Hash.decode;

public class User {
    String username;
    String password;
    String jurisdiction;
    public static String gJurisdiction(String a, String user) {
        String re[] = a.split("§");
        String J = null;
        for(int x = 0; x < re.length ; x ++ ) {
            if (re[x].contains(user)) {
                String usera[] = re[x].split(":");
                J = usera[2];
            }
        }
        return J;
    }
    public static String checkUser(String c) {
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
            String a =new String(bos.toByteArray());
            a = decode(a);
            String res[] = a.split("§");
            for (int x = 0 ;x <res.length; x ++) {
                String r[] = res[x].split(":");
                String ra[] = c.split(":");
                if (r[0].equals(ra[0])) {
                    if (r[1].equals(ra[1])) {
                        return r[2];
                    }
                }
            }
            return "false";
        } catch (IOException e) {
            return "false";
        }
    }
}
