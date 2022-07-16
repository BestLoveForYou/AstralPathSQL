package org.astralpathsql.autoC;

import org.astralpathsql.autoC.form.Table;
import org.astralpathsql.been.COREINFORMATION;
import org.astralpathsql.file.Filer;
import org.astralpathsql.server.MainServer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.astralpathsql.file.Add.add;
import static org.astralpathsql.server.MainServer.*;
import static org.astralpathsql.server.MainServer.now_Connect;

public class DoIT {
    /**
     * 这是实现模块,包括所有数据库功能
     * @param readMessage
     * @return
     */
    public static String doit(String readMessage,String Jurisdiction,String ip) {

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
                if (sp[1].equals("into")) {
                    /**
                     * @Date 2022-07-14
                     * 添加功能完成!!!!!!和sql语句一模一样,但不能使用大写
                     * @Date 2022-07-16  数据不全时更新失败bug已解决
                     *
                     */
                    if (sp[3].equals("values")) {
                        String ch = sp[2];
                        //数据表
                        String res[] = ch.split("\\(");
                        sp[4] = sp[4].replaceAll("\\(", "");
                        sp[4] = sp[4].replaceAll("\\)", "");
                        res[1] = res[1].replaceAll("\\)", "");//去除(和)
                        String tabler[] = res[1].split(",");//为输入中的数据格式
                        Table t = ta.get(res[0]);//获取表格中的数据格式
                        String res2[] = t.getTable().split(",");
                        COREINFORMATION c = new COREINFORMATION();
                        String value[] = sp[4].split(",");
                        String add = "";//这是要处理的数据,将要保存
                        String tab = "";
                        int y = 0;
                        int x;
                        for (x = 0; x < res2.length; x++) {
                            String prof[] = res2[y].split(" ");
                            try {
                                if (prof[1].equals(tabler[x])) {//检验是否匹配 prof[0]为格式,prof[1[为数据名称
                                    add = add + prof[1] + value[x] + ";";
                                    tab = tab + tabler[x] + ",";
                                    if (y == res.length) {
                                        break;
                                    }
                                } else {
                                    y++;
                                    x--;
                                }
                            } catch (Exception e) {
                            }
                        }
                        c.setId(tree.size());
                        c.setTable(res[0]);
                        String r[] = t.getTable().split(",");
                        String tabe = "";
                        for (int xa = 0; xa < r.length; xa++) {
                            tabe = tabe + r[xa].split(" ")[1] + ",";
                        }
                        if (!tabe.equals(tab)) {
                            String tabl[] = tabe.replaceAll(tab,"").split(",");
                            String a = "";
                            for (int xb = 0; xb < tabl.length; xb ++) {
                                a = a + tabl[xb]  + "'null';";
                            }
                            c.setINFO(a + add);
                        } else {
                            c.setINFO(add);
                        }
                        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                        c.setHiredate(new Date());
                        tree.add(c);
                        writeMessage = "1";
                    }
                }

            }
            /**
             *
             *查看表
             */
            if (sp[0].equals("desc")) {
                try {
                    writeMessage = String.valueOf(ta.get(sp[1]));
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
            }
            /**
             * 搜索功能实现
             */
            if (sp[0].equals("select")) {
                String sl = sp[1];
                if (sl.equals("*")) {
                    if (sp[2].equals("from")) {
                        String a = tree.forT(sp[3]);
                        writeMessage = a;
                        if (sp[4].equals("where")) {
                            sp[5] = sp[5].replaceAll("=","'") + "'";
                            a = tree.forT(sp[3],sp[5]);
                            writeMessage = a;
                        }
                    }
                } else
                if (sl.equals("counts")) {
                    if (sp[2].equals("from")) {
                        writeMessage = String.valueOf(tree.num(sp[3]));
                    }
                } else {
                    writeMessage = tree.select(sp[1],sp[3]);
                    writeMessage = "+--" + sl + "--+\n" + writeMessage;
                }
            }
            /**
             * @Date 2022-07-14
             * 删除属性
             */
            if (sp[0].equals("delete")) {
                if (sp[3].equals("where")) {
                    String sl = sp[1];
                    COREINFORMATION sc = new COREINFORMATION();
                    String table = sp[2];
                    if (sp[4].contains("=")) {
                        sp[4] = sp[4].replaceAll("=","'");
                        sp[4] = sp[4] + "'";
                    }
                    tree.deleteBy(table,sp[4]);
                    writeMessage = "1";
                }
            }
            /**
             * 更新数据功能
             * 已知Bug: 2022-07-15:数据不全 已修复
             */
            if (sp[0].equals("update")) {
                if (sp[2].equals("set")) {
                    sp[5] = sp[5].replaceAll("=","'") + "'";
                    String a = tree.forT(sp[1],sp[5]);
                    System.out.println(a);
                    String table = sp[1];
                    String work = sp[3];
                    String ca[] = work.split("=");
                    work = work.replaceAll("=","'") + "';";
                    tree.deleteBy(sp[1],sp[5]);
                    String res[] = a.split("\\|");
                    int ind = res[2].lastIndexOf(ca[0]);
                    String handle = res[2].substring(0,ind);
                    String end = res[2].substring(ind + work.length() + 2);
                    System.out.println(end);
                    System.out.println(handle + work.replaceFirst(";","") + end);
                    handle = res[0] + "|" + res[1] + "|" + handle + work.replaceFirst(";","") + end + "|table:" + table + "§";
                    System.out.println(handle);
                    COREINFORMATION c = add(handle);
                    System.out.println(c.toString());
                    tree.add(c);
                    writeMessage = "1";
                }
            }
            /*
                以下是缓存功能
             */
            if (sp[0].equals("cache")) {
                if (sp[1].equals("insert")) {
                    if (sp[2].equals("into")) {
                        /**
                         * @Date 2022-07-14
                         * 添加功能完成!!!!!!和sql语句一模一样,但不能使用大写
                         * @Date 2022-07-16  数据不全时更新失败bug已解决
                         *
                         */
                        if (sp[4].equals("values")) {
                            String ch = sp[3];
                            //数据表
                            String res[] = ch.split("\\(");
                            sp[5] = sp[5].replaceAll("\\(", "");
                            sp[5] = sp[5].replaceAll("\\)", "");
                            res[1] = res[1].replaceAll("\\)", "");//去除(和)
                            String tabler[] = res[1].split(",");//为输入中的数据格式
                            Table t = ta.get(res[0]);//获取表格中的数据格式
                            String res2[] = t.getTable().split(",");
                            COREINFORMATION c = new COREINFORMATION();
                            String value[] = sp[5].split(",");
                            String add = "";//这是要处理的数据,将要保存
                            String tab = "";
                            int y = 0;
                            int x;
                            for (x = 0; x < res2.length; x++) {
                                String prof[] = res2[y].split(" ");
                                try {
                                    if (prof[1].equals(tabler[x])) {//检验是否匹配 prof[0]为格式,prof[1[为数据名称
                                        add = add + prof[1] + value[x] + ";";
                                        tab = tab + tabler[x] + ",";
                                        if (y == res.length) {
                                            break;
                                        }
                                    } else {
                                        y++;
                                        x--;
                                    }
                                } catch (Exception e) {
                                }
                            }
                            c.setId(tree.size());
                            c.setTable(res[0]);
                            String r[] = t.getTable().split(",");
                            String tabe = "";
                            for (int xa = 0; xa < r.length; xa++) {
                                tabe = tabe + r[xa].split(" ")[1] + ",";
                            }
                            String tabl[] = tabe.replaceAll(tab,"").split(",");
                            String a = "";
                            for (int xb = 0; xb < tabl.length; xb ++) {
                                a = a + tabl[xb]  + "'null';";
                            }
                            c.setINFO(a + add);
                            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                            c.setHiredate(new Date());
                            tree.add(c);
                            writeMessage = "1";
                        }
                    }

                }
                if (sp[1].equals("getTime")) {
                    writeMessage = String.valueOf(cache.getDelaySeconds(Long.parseLong(sp[2])));
                }
                if (sp[1].equals("insert")) {
                    if (sp[2].contains("§")) {
                        String res[] = sp[2].split("§");
                        int x;
                        for (x = 0; x < res.length; x++) {
                            COREINFORMATION emp = ClassInstanceFactory.create(COREINFORMATION.class, res[x]);    // 工具类自动设置
                            cache.put(emp.getId(),emp);
                        }
                        writeMessage = String.valueOf(x);
                    } else {
                        COREINFORMATION emp = ClassInstanceFactory.create(COREINFORMATION.class, sp[2]) ;	// 工具类自动设置
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
                if (sp[1].equals("submit")) {
                    int x;
                    for (x = 0; x < cache.getCacheObjects().size(); x++) {
                         tree.add(cache.get(x));
                    }
                    writeMessage = String.valueOf(x);
                }
            }
            if (sp[0].equals("status")) {
                writeMessage = "版本:" + version + "\n历史连接数:" + all_Connect + "\n目前连接数:" + now_Connect + "\n权限:" + Jurisdiction
                        + "\n连接IP:" + ip ;
            }
            if (sp[0].equals("user")) {
                if (sp[1].equals("add")) {
                    if(sp[2].contains("DBA")) {
                        writeMessage = "DBA permission cannot be created!";
                    } else if (Jurisdiction.equals("ADMIN")) {
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
                    if (Jurisdiction.equals("admin")) {
                        Filer.removeUser(sp[2]);
                        writeMessage = "用户更新-时间:" + new Date();
                    } else {
                        writeMessage = "权限不足!";
                    }

                }
                if (sp[1].equals("all")) {
                    if (Jurisdiction.equals("ADMIN")) {
                        writeMessage = Filer.getUser();
                    } else {
                        writeMessage = "权限不足!";
                    }
                }
            }
            if ("exit".equals(readMessage)) { 						// 结束指令
                writeMessage = "[INFO]Connected closed...";			// 结束消息
            }
        } catch (Exception e) {
        }
        System.gc();
        return writeMessage;
    }
}
