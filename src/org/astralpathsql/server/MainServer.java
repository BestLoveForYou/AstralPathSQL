package org.astralpathsql.server;

import org.astralpathsql.autoC.DoIT;
import org.astralpathsql.autoC.form.Table;
import org.astralpathsql.been.Cache;
import org.astralpathsql.been.COREINFORMATION;
import org.astralpathsql.client.InputUtil;
import org.astralpathsql.file.Add;
import org.astralpathsql.file.Filer;
import org.astralpathsql.properties.ProRead;
import org.astralpathsql.node.BalancedBinaryTree;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.astralpathsql.print.ColorTest.getFormatLogString;

public class MainServer {
    public static String version = "1.102.20220714";
    public static BalancedBinaryTree<COREINFORMATION> tree = new BalancedBinaryTree<COREINFORMATION>();
    public static Integer now_Connect = 0; //目前连接数
    public static Integer all_Connect = 0;//历史连接数
    public static Selector selector = null;
    public static ServerSocketChannel serverSocketChannel = null;
    public static Set<SelectionKey> selectedKeys = null; 	// 获取全部连接通道
    public static Iterator<SelectionKey> selectionIter = null;
    public static Cache<Integer, COREINFORMATION> cache = new Cache<>();
    public static Properties prop = null;
    public static Map<String,Table> ta = new HashMap<>();
    public static boolean flag = true;
    public static int PORT = 9999;
    /**
     * @author Saturn
     * AstralPathSQL System
     *
     */
    public static void main(String[] args) throws Exception {
        System.out.println(getFormatLogString("版本:" + version + "\n" + System.getProperty("os.name"),35,4));
        try {
            long start = System.currentTimeMillis();
            System.out.println(getFormatLogString("初始化中",34,1));
            Filer.createInFirst();//对象保存文件
            Table.read();//生成表
            System.out.println(getFormatLogString("数据,数据表 加载完成",34,1));
            prop = ProRead.read();//加载配置文件
            PORT = Integer.parseInt(prop.getProperty("port"));
            System.out.println(getFormatLogString("平衡二叉树加载中",34,1));
            tree = Add.addin(tree);//二叉树加载
            System.out.println(getFormatLogString("成功!",32,1));
            System.out.println(getFormatLogString("线程池加载中",34,1));
            ExecutorService executorService = Executors.newFixedThreadPool(19999999);
            System.out.println(getFormatLogString("成功!",32,1));
            all_Connect = Integer.valueOf(prop.getProperty("all_connect"));
            executorService.submit(() -> {
                System.out.println(getFormatLogString("定时任务已启动成功!",32,1));
                while (flag) {
                    try {
                        Thread.sleep(10000);
                        Filer.writeSQL(tree);
                        ProRead.write();
                        Table.write();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            executorService.submit(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                System.out.println(getFormatLogString("服务端命令集启动成功!",32,1));
                while (true) {
                    String msg = InputUtil.getString(">");	// 提示信息
                    String out = DoIT.doit(msg);
                    if (msg.equals("getall")) {
                        String res[] = out.split("§");
                        for (int x = 0;x < res.length ;x ++) {
                            System.out.println(res[x]);
                        }
                    } else {
                        System.out.println(out);
                    }
                    if ("exit".equals(msg)) { 						// 结束指令
                        System.out.println("[INFO]Server closed");			// 结束消息
                        executorService.shutdown();									// 关闭线程池
                        serverSocketChannel.close();									// 关闭服务端通道
                        selector.close();
                        System.exit(0);
                    }
                }
            });
            // 打开一个服务端的Socket的连接通道
            getFormatLogString("打开服务端连接通道",34,1);
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false); 					// 设置非阻塞模式

            serverSocketChannel.bind(new InetSocketAddress(PORT)); 			// 服务绑定端口
            // 打开一个选择器，随后所有的Channel都要注册到此选择器之中

            selector = Selector.open();
            // 将当前的ServerSocketChannel统一注册到Selector之中，接受统一的管理
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            long end = System.currentTimeMillis();
            System.out.println(getFormatLogString("全部启动完成! 共耗时:" + (end - start) + "ms",32,1));
            System.out.println(getFormatLogString("服务端启动程序成功，该程序在 " + PORT + " 端口上监听，等待客户端连接... ...",35,4));
            // 所有的连接处理都需要被Selector所管理，也就是说只要有新的用户连接，那么就通过Selector处理
            int keySelect = 0; 											// 接收连接状态
            while ((keySelect = selector.select()) > 0) { 					// 持续等待连接
                selectedKeys = selector.selectedKeys();
                selectionIter = selectedKeys.iterator();
                while (selectionIter.hasNext()) {
                    SelectionKey selectionKey = selectionIter.next(); 		// 获取每一个通道
                    if (selectionKey.isAcceptable()) { 					// 模式为接收连接模式
                        SocketChannel clientChannel = serverSocketChannel.accept(); // 等待接收
                        if (clientChannel != null) { 						// 已经有了连接
                            now_Connect ++;
                            all_Connect ++;
                            executorService.submit(new NIOThread(clientChannel));
                        }
                    }
                    selectionIter.remove(); 								// 移除掉此通道
                }
            }
            executorService.shutdown();									// 关闭线程池
            serverSocketChannel.close();									// 关闭服务端通道

        } catch (Exception e) {

        }

    }
}
