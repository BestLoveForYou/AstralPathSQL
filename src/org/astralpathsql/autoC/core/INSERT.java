package org.astralpathsql.autoC.core;

import org.astralpathsql.autoC.form.Table;
import org.astralpathsql.been.COREINFORMATION;
import org.astralpathsql.server.MainServer;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.astralpathsql.server.MainServer.*;
import static org.astralpathsql.server.MainServer.lock;

public class INSERT {
    public static String in(String sp[]) {
        String writeMessage = "-1";
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
                            if (y == res[1].split(",").length) {
                                break;
                            }
                        } else {
                            y++;
                            x--;
                        }
                    } catch (Exception e) {
                    }
                }


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
                long stamp = MainServer.lock.readLock();
                long write = MainServer.lock.tryConvertToWriteLock(stamp);
                Boolean flag = true;
                while (flag)
                {
                    if (write != 0) {
                        stamp = write;
                        if (Mtree.containsKey(t.getName())) {
                            String a = c.getINFO();
                            String id = a.substring(a.indexOf("id"));
                            id = id.replaceFirst("id'","");
                            id = id.substring(0,id.indexOf("'"));
                            System.out.println(id);
                            c.setId(Integer.valueOf(id));
                        } else {
                            c.setId(tree.size());
                        }
                        if (Mtree.containsKey(t.getName())) {
                            Mtree.get(t.getName()).add(c);
                        } else {
                            tree.add(c);
                        }
                        flag = false;
                    } else {
                        lock.unlockRead(stamp);
                        write = lock.writeLock();
                    }
                }
                lock.unlock(stamp);
                writeMessage = "1";
            }
        }
        return writeMessage;
    }
}
