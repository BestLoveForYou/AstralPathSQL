package org.astralpathsql.server;

import org.astralpathsql.autoC.DoIT;
import org.astralpathsql.file.Filer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static org.astralpathsql.server.MainServer.*;

public class NIOThread implements Runnable {					// 客户端处理线程
        private SocketChannel clientChannel; 								// 客户端通道
        private boolean flag = true; 										// 循环标记
        public NIOThread(SocketChannel clientChannel) throws Exception {
            this.clientChannel = clientChannel;							// 保存客户端通道
            Filer.addIPFile(String.valueOf(clientChannel.getRemoteAddress()));
        }
        @Override
        public void run() {												// 线程任务
            ByteBuffer buffer = ByteBuffer.allocate(10000000); 					// 创建缓冲区
            try {
                while (this.flag) { 										// 不断与客户端交互
                    // 由于可能重复使用一个Buffer，所以使用之前需要将其做出清空处理
                    buffer.clear();
                    int readCount = this.clientChannel.read(buffer); 		// 接收客户端发送数据
                    String readMessage = new String(buffer.array(),
                            0, readCount).trim(); 						// 数据变为字符串
                    String writeMessage = DoIT.doit(readMessage);
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
                e.printStackTrace();
            }
        }
}
