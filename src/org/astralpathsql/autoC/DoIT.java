package org.astralpathsql.autoC;

import org.astralpathsql.autoC.core.DELETE;
import org.astralpathsql.autoC.core.INSERT;
import org.astralpathsql.autoC.core.SELECT;
import org.astralpathsql.autoC.core.UPDATE;
import org.astralpathsql.autoC.form.Table;
import org.astralpathsql.autoC.form.TreeSearch;
import org.astralpathsql.been.COREINFORMATION;
import org.astralpathsql.file.Add;
import org.astralpathsql.file.Filer;
import org.astralpathsql.node.BalancedBinaryTree;
import org.astralpathsql.properties.ProRead;
import org.astralpathsql.server.MainServer;

import java.io.*;
import java.util.Date;
import java.util.List;

import static org.astralpathsql.file.Filer.*;
import static org.astralpathsql.server.MainServer.*;
import static org.astralpathsql.server.MainServer.now_Connect;

public class DoIT {
    /**
     * 这是实现模块,包括所有数据库功能
     */
    public static String doit(String readMessage, String Jurisdiction, String ip) throws IOException{
        writePool.submit(() -> {
            try {
                Filer.writeLog(readMessage,ip);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        String[] sp = readMessage.split(" ");
        String writeMessage = "-1";
        try {
            if (sp[0].equals("use")) {
                MainServer.save();
                MainServer.ta.clear();
                File file = new File("." + File.separator + "apsql" + File.separator + "db" + File.separator + sp[1] + File.separator + "save.ap");			// 定义文件路径
                if (!file.exists()) {
                    return writeMessage="数据库不存在";
                }
                database = file;
                table = new File("." + File.separator + "apsql" + File.separator + "db" + File.separator + sp[1] + File.separator + "table.txt");			// 定义文件路径

                tree = new BalancedBinaryTree<COREINFORMATION>();


                Table.read();//生成表
                TreeSearch.load();
                tree = Add.addin(tree);//二叉树加载
                writeMessage = "1";
            }
            if (sp[0].equals("set")) {
                if (sp[1].equals("table")) {
                    String prop = sp[3];
                    Table t = ta.get(sp[2]);//Get table name
                    if (prop.equals("1")) {
                        if (t.getTable().contains("int id")) {
                            t.setProp(prop);
                            MainServer.save();
                            MainServer.ta.clear();
                            tree = new BalancedBinaryTree<COREINFORMATION>();
                            Table.read();//生成表
                            TreeSearch.load();
                            tree = Add.addin(tree);//二叉树加载
                            writeMessage = "1";
                        }
                    } else {
                        t.setProp(prop);
                        System.out.println("请重新启动！");
                        writeMessage = "命令已执行！重新启动后生效";
                    }

                }
            }
            if (sp[0].equals("show")) {
                if (sp[1].equals("database")) {
                    List<String> a = Filer.readDB();
                    writeMessage = "+---Database-------§|";
                    for (int x = 0 ; x < a.size(); x ++) {
                        writeMessage = writeMessage + a.get(x) + "§|";
                    }
                    writeMessage = writeMessage + "§+------------------";
                }

            }
            if (sp[0].equals("banip")) {
                BanIPFile(sp[1]);
                writeMessage = "已完成！封禁IP:" + sp[1];
            }
            if (sp[0].equals("save")) {
                writePool.submit(() -> {

                            try {
                                ProRead.write();
                                Table.write();
                                Filer.writeSQL();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        );
                writeMessage = "1";

            }
            if (sp[0].equals("insert")) {
                writeMessage = INSERT.in(sp);
            }
            /**
             *
             *查看表
             */
            if (sp[0].equals("desc")) {
                try {
                    Table t  = ta.get(sp[1]);
                    writeMessage = "-" + t.getName();
                    for (int x = 0 ; x < t.getName().length(); x ++) {
                        writeMessage = writeMessage + "-";
                    }
                    writeMessage = writeMessage + "§";
                    String a[] = t.getTable().split(",");
                    for (int x = 0 ; x < a.length; x ++) {
                        writeMessage = writeMessage + a[x] + "§";
                    }
                    for (int x = 0 ; x < t.getName().length(); x ++) {
                        writeMessage = writeMessage + "-";
                    }
                    writeMessage = writeMessage + "§prop:" + t.getProp();
                } catch (Exception e) {
                    writeMessage = ta.toString();
                }
            }
            if (sp[0].equals("create")) {
                if (sp[1].contains("table")) {
                    Table t = new Table();
                    String a =readMessage;
                    a = a.replaceAll("create table ","");
                    a = a.replaceAll("\\)","");
                    a = a.replaceAll(";","");
                    String result2 [] = a.split(",");
                    String b = null;//属性
                    for (int x = 0 ; x <result2.length ; x ++) {
                        if (x==0) {
                            String r[] = a.split("\\(");
                            t.setName(r[0]);
                            result2[0] = result2[0].replaceAll(r[0] + "\\(","");
                            String avc[] =  result2[x].split(" ");//扭转
                            b = avc[1] + " " + avc[0];
                            continue;
                        }
                        String avc[] =  result2[x].split(" ");//扭转
                        String i = avc[1] + " " + avc[0];
                        b = b + "," +  i;
                    }
                    t.setTable(b);
                    MainServer.ta.put(t.getName().replaceAll("name:",""),t);
                    if (readMessage.contains("id int")) {
                        writeMessage = "1§注意:此表可以使用二叉树引擎,使用set table " + t.getName() + " 1 将表的引擎改为二叉树引擎！";
                    } else {
                        writeMessage = "1";
                    }
                }
                if (sp[1].equals("database")) {
                    createDB(sp[2]);
                    writeMessage = "1";
                }
            }
            /**
             * 删除表
             */
            if (sp[0].equals("drop")) {
                if(sp[1].equals("table")) {
                    ta.remove(sp[2]);
                    writeMessage = "1";
                }
                if (sp[1].equals("database")) {
                    removeDB(sp[2]);
                    writeMessage = "1";
                }
            }
            /**
             * 搜索功能实现
             */
            if (sp[0].equals("select")) {
                writeMessage = SELECT.select(sp);
                return writeMessage;
            }
            /**
             * @Date 2022-07-14
             * 删除属性
             */
            if (sp[0].equals("delete")) {
                writeMessage = DELETE.delete(sp);
            }
            /**
             * 更新数据功能
             * 已知Bug: 2022-07-15:数据不全 已修复
             */
            if (sp[0].equals("update")) {
                writeMessage = UPDATE.update(sp);
            }
            if (sp[0].equals("status")) {
                writeMessage = "版本:" + version + "\n历史连接数:" + all_Connect + "\n目前连接数:" + now_Connect + "\n权限:" + Jurisdiction
                        + "\n连接IP:" + ip ;
            }
            if (sp[0].equals("user")) {
                if (sp[1].equals("add")) {
                    if(sp[2].contains("DBA")) {
                        writeMessage = "DBA permission cannot be created!";
                    } else if (Jurisdiction.equals("ADMIN")|Jurisdiction.equals("DBA")) {
                        if(sp[2].contains(":")) {
                            Filer.addUser(sp[2]);
                            writeMessage = "用户更新-时间:" + new Date();
                        } else {
                            writeMessage = "null";
                        }
                    } else {
                        writeMessage = "权限不足!";
                    }
                }
                if (sp[1].equals("remove")) {
                    if (Jurisdiction.equals("ADMIN")|Jurisdiction.equals("DBA")) {
                        Filer.removeUser(sp[2]);
                        writeMessage = "用户更新-时间:" + new Date();
                    } else {
                        writeMessage = "权限不足!";
                    }

                }
                if (sp[1].equals("all")) {
                    if (Jurisdiction.equals("ADMIN")|Jurisdiction.equals("DBA")) {
                        writeMessage = Filer.getUser();
                    } else {
                        writeMessage = "权限不足!";
                    }
                }
                USER = Filer.readUser();
            }
            if ("exit".equals(readMessage)) { 						// 结束指令
                writeMessage = "[INFO]Connected closed...";			// 结束消息
            }
            if ("stop".equals(readMessage)) { 						// 结束指令
                if (Jurisdiction.equals("ADMIN")) {
                    writeMessage = "[INFO]Connected closed...";			// 结束消息
                    System.out.println("[INFO]Server closed");
                    stopserver();
                } else {
                    writeMessage = "权限不足!";
                }
                if (Jurisdiction.equals("DBA")) {
                    writeMessage = "[INFO]Connected closed...";			// 结束消息
                    System.out.println("[INFO]Server closed");
                    stopserver();
                } else {
                    writeMessage = "权限不足!";
                }
            }
            if ("restart".equals(readMessage)) { 						// 结束指令
                writeMessage = "[INFO]Connected closed...";			// 结束消息
                System.out.println("[INFO]Server closed");
                restart();
                stopserver();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
        return writeMessage;
    }
    public static void restart() {
            try {
                Runtime rt = Runtime.getRuntime();
//            Process pr = rt.exec("cmd /c dir");
//            Process pr = rt.exec("D:/APP/Evernote/Evernote.exe");//open evernote program
                Process pr = rt.exec("java -jar .//AstralPathSQL.jar server.port=" + PORT) ;//open tim program
                BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream(),"GBK"));
                String line = null;
                while ((line = input.readLine())!=null){
                    System.out.println(line);
                }
                int exitValue = pr.waitFor();
                System.out.println("Exited with error code "+exitValue);
            } catch (IOException e) {
                System.out.println(e.toString());
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    }
}
