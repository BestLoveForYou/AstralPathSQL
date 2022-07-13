package org.astralpathsql.autoC;

import org.astralpathsql.been.Emp;
import org.astralpathsql.file.Filer;

import java.util.Map;

import static org.astralpathsql.server.MainServer.*;
import static org.astralpathsql.server.MainServer.now_Connect;

public class DoIT {
    public static String doit(String readMessage) {
        String sp[] = readMessage.split(" ");
        String writeMessage = "无效指令!";
        try {
            if (sp[0].equals("getall")) {
                String a = tree.forW();
                if (a.isEmpty()) {

                } else {
                    writeMessage = a;
                }
            }
            if (sp[0].equals("save")) {
                writeMessage = String.valueOf(Filer.writeSQL(tree));
            }
            if (sp[0].equals("insert")) {
                if (sp[1].contains("§")) {
                    String res[] = sp[1].split("§");
                    int x;
                    for (x = 0; x < res.length; x++) {
                        Emp emp = ClassInstanceFactory.create(Emp.class, res[x]);    // 工具类自动设置
                        tree.add(emp);
                    }
                    writeMessage = String.valueOf(x);
                } else {
                    Emp emp = ClassInstanceFactory.create(Emp.class, sp[1]) ;	// 工具类自动设置
                    tree.add(emp);
                    writeMessage = "1";
                }
            }
            if (sp[0].equals("select")) {
                String sl = sp[1];
                Emp sc = new Emp();
                if (sl.equals("id")) {
                    sc.setId(Integer.parseInt(sp[2]));
                    sc = (Emp) tree.getId(sc);
                    writeMessage = sc.toString();
                }
                if (sl.equals("counts")) {
                    writeMessage = String.valueOf(tree.size());
                }
            }
            if (sp[0].equals("delete")) {
                String sl = sp[1];
                Emp sc = new Emp();
                if (sl.equals("id")) {
                    sc.setId(Integer.parseInt(sp[2]));
                    tree.remove(sc);
                }
                writeMessage = "1";
            }
            if (sp[0].equals("update")) {
                String sl = sp[1];
                Emp sc = new Emp();
                if (sl.equals("id")) {
                    sc.setId(Integer.parseInt(sp[2]));
                    sc = (Emp) tree.getId(sc);
                }
                String value = sc.toString();
                value = value + "|" + sp[3];
                Emp emp = ClassInstanceFactory.create(Emp.class, value) ;	// 工具类自动设置
                sc.setId(Integer.parseInt(sp[2]));
                tree.remove(sc);
                tree.add(emp);
                writeMessage = "1";
            }
            /*
                以下是缓存功能
             */
            if (sp[0].equals("cache")) {
                if (sp[1].equals("setTime")) {
                    cache.setDelaySeconds(Long.parseLong(sp[2]));
                }
                if (sp[1].equals("getTime")) {
                    cache.getDelaySeconds(Long.parseLong(sp[2]));
                }
                if (sp[1].equals("insert")) {
                    if (sp[2].contains("§")) {
                        String res[] = sp[1].split("§");
                        int x;
                        for (x = 0; x < res.length; x++) {
                            Emp emp = ClassInstanceFactory.create(Emp.class, res[x]);    // 工具类自动设置
                            cache.put(emp.getId(),emp);
                        }
                        writeMessage = String.valueOf(x);
                    } else {
                        Emp emp = ClassInstanceFactory.create(Emp.class, sp[2]) ;	// 工具类自动设置
                        cache.put(cache.getCacheObjects().size(),emp);
                        writeMessage = "1";
                    }
                }
                if (sp[1].equals("get")) {
                    writeMessage = " ";
                    for (int x = 0; x <  cache.getCacheObjects().size(); x++) {
                        writeMessage = writeMessage + "|" + cache.get(x);
                    }
                }
            }
            if (sp[0].equals("status")) {
                writeMessage = "版本:" + version + "\n历史连接数:" + all_Connect + "\n目前连接数:" + now_Connect;
            }
            if ("exit".equals(readMessage)) { 						// 结束指令
                writeMessage = "[INFO]Connected closed...";			// 结束消息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writeMessage;
    }
}
