package org.astralpathsql.server;

import org.astralpathsql.autoC.DoIT;
import org.astralpathsql.autoC.Hash;
import org.astralpathsql.been.User;
import org.astralpathsql.file.Filer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.astralpathsql.file.Filer.checkIP;
import static org.astralpathsql.server.MainServer.*;

public class NIOThread implements Runnable {					// 客户端处理线程
        private SocketChannel clientChannel; 								// 客户端通道
        private boolean flag = true; 										// 循环标记
        public NIOThread(SocketChannel clientChannel) throws Exception {
            this.clientChannel = clientChannel;							// 保存客户端通道
            String ip = String.valueOf(clientChannel.getRemoteAddress());
            Filer.addIPFile(ip.replaceAll("/","").split(":")[0]);
        }
        @Override
        public void run() {												// 线程任务
            String ip = null;
            try {
                ip = String.valueOf(clientChannel.getRemoteAddress());
                ip = ip.replaceAll("/","").split(":")[0];
                if (banip.contains(ip)) {
                    this.clientChannel.close(); 								// 关闭通道
                    now_Connect --;
                    System.out.println("拦截了一次黑名单连接！IP来源:" + ip);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ByteBuffer buffer = ByteBuffer.allocate(10000000); 					// 创建缓冲区
            try {
                buffer.clear();
                int readCount1 = this.clientChannel.read(buffer); 		// 接收客户端发送数据
                String readMessage1 = new String(buffer.array(),
                        0, readCount1).trim(); 						// 数据变为字符串
                String ju = User.checkUser(readMessage1);
                if (!ju.equals("false")) {
                    buffer.clear(); 										// 清空缓冲区
                    buffer.put("Successful certification".getBytes()); 					// 缓冲区保存数据
                    buffer.flip(); 										// 重置缓冲区
                    this.clientChannel.write(buffer);						// 回应信息
                    String a = Filer.getUser();
                } else {
                    buffer.clear(); 										// 清空缓冲区
                    buffer.put("Authentication failed".getBytes()); 					// 缓冲区保存数据
                    buffer.flip(); 										// 重置缓冲区
                    this.clientChannel.write(buffer);						// 回应信息
                    this.flag = false;
                    now_Connect --;
                }

                while (this.flag) { 										// 不断与客户端交互
                    // 由于可能重复使用一个Buffer，所以使用之前需要将其做出清空处理
                    buffer.clear();
                    int readCount = this.clientChannel.read(buffer); 		// 接收客户端发送数据
                    String readMessage = new String(buffer.array(),
                            0, readCount).trim(); 						// 数据变为字符串
                    String writeMessage = DoIT.doit(readMessage,ju,ip);
                    save();
                    if ("exit".equals(readMessage)) { 						// 结束指令
                        writeMessage = "[INFO]Connected closed...";			// 结束消息
                        this.flag = false; 								// 修改标记
                        now_Connect --;
                    }
                    buffer.clear(); 										// 清空缓冲区
                    buffer.put(writeMessage.getBytes()); 					// 缓冲区保存数据
                    buffer.flip(); 										// 重置缓冲区
                    this.clientChannel.write(buffer);						// 回应信息
                }
                this.clientChannel.close(); 								// 关闭通道
            } catch (Exception e) {
                now_Connect --;
            }
        }
}
