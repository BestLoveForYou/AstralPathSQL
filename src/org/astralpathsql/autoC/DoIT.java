package org.astralpathsql.autoC;

import org.astralpathsql.autoC.form.Table;
import org.astralpathsql.autoC.form.TreeSearch;
import org.astralpathsql.been.COREINFORMATION;
import org.astralpathsql.client.InputUtil;
import org.astralpathsql.file.Add;
import org.astralpathsql.file.Filer;
import org.astralpathsql.node.BalancedBinaryTree;
import org.astralpathsql.server.MainServer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.astralpathsql.file.Add.add;
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
                            System.out.println("请重新启动！");
                            writeMessage = "命令已执行！重新启动后生效";
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
                writeMessage = String.valueOf(Filer.writeSQL());
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
                String sl = sp[1];
                if (Mtree.containsKey(sp[3])) {
                    if (sl.equals("*")) {
                        try {
                            if (sp[2].equals("from")) {
                                if (sp[4].equals("where")) {
                                    try {
                                        if (sp[6].equals("like")) {
                                            String a = Mtree.get(sp[3]).forI(sp[5]);
                                            String b[] = a.split("§");
                                            writeMessage = "";
                                            for (int x=0;x < b.length; x ++) {
                                                COREINFORMATION emp = ClassInstanceFactory.create(COREINFORMATION.class, b[x]) ;	// 工具类自动设置
                                                String c = emp.getINFO();
                                                String d = c.substring(c.indexOf(sp[5]));
                                                d = d.substring(0,d.indexOf(";"));
                                                d = d.replaceAll(sp[5],"");
                                                if (d.contains(sp[7])) {
                                                    writeMessage = writeMessage + c + "§";
                                                }
                                            }
                                            return writeMessage;
                                        }
                                    } catch (Exception e) {

                                    }
                                    if (sp[5].contains("id=")) {
                                        String value = "id:" +sp[5].split("=")[1] + "|hiredate:2022-07-27|INFO:null|table:null§";
                                        COREINFORMATION c = ClassInstanceFactory.create(COREINFORMATION.class, value) ;	// 工具类自动设置
                                        COREINFORMATION resu = (COREINFORMATION) Mtree.get(sp[3]).getId(c);
                                        writeMessage = resu.toString();
                                        return writeMessage;
                                    } else if (sp[5].contains("=")){
                                        sp[5] = sp[5].replaceAll("=","'") + "'";
                                        String a = Mtree.get(sp[3]).forI(sp[5]);
                                        writeMessage = a;
                                        return writeMessage;
                                    }
                                }

                            } else {
                                return Mtree.get(sp[3]).forW();
                            }
                        } catch (Exception e) {
                            String a = Mtree.get(sp[3]).forW();
                            writeMessage = a;
                            return a;
                        }
                    }

                    if (sl.equals("counts")) {
                        if (sp[2].equals("from")) {
                            writeMessage = String.valueOf(Mtree.get(sp[3]).size());
                            return writeMessage;
                        }
                    } else if(!sl.equals("*")){
                        try {
                            if (sl.contains(",")) {
                                try {
                                    if (sp[4].equals("where")) {
                                        try {
                                            if (sp[6].equals("like")) {
                                                String a = Mtree.get(sp[3]).forI(sp[5]);
                                                String b[] = a.split("§");
                                                writeMessage = "---" + sp[1] + "-------§";
                                                for (int x=0;x < b.length; x ++) {
                                                    COREINFORMATION emp = ClassInstanceFactory.create(COREINFORMATION.class, b[x]) ;	// 工具类自动设置
                                                    String c = emp.getINFO();
                                                    String d = c.substring(c.indexOf(sp[5]));
                                                    d = d.substring(0,d.indexOf(";"));
                                                    d = d.replaceAll(sp[5],"");
                                                    if (d.contains(sp[7])) {
                                                        String r[] = sp[1].split(",");
                                                        for (int y = 0 ;y < r.length ; y ++) {
                                                            d = c.substring(c.indexOf(r[y]));
                                                            d = d.substring(0,d.indexOf(";"));
                                                            d = d.replaceAll(r[y] + "'","");
                                                            d = d.replaceAll("'","");
                                                            writeMessage = writeMessage + d + "|";
                                                        }
                                                        writeMessage = writeMessage + "§";
                                                    }
                                                }
                                                return writeMessage;
                                            }
                                        }catch (Exception e) {}
                                        if (sp[5].contains("id=")) {
                                            String value = "id:" +sp[5].split("=")[1] + "|hiredate:2022-07-27|INFO:null|table:null§";
                                            COREINFORMATION c = ClassInstanceFactory.create(COREINFORMATION.class, value) ;	// 工具类自动设置
                                            String i[] = sl.split(",");
                                            writeMessage = "";
                                            for (int x =0 ; x < i.length; x ++) {
                                                String a= Mtree.get(sp[3]).getId(c,i[x]);
                                                if (!a.equals("")) {
                                                    writeMessage = writeMessage + a;
                                                }
                                            }
                                            writeMessage = "+--" + sl + "--+\n" + writeMessage;
                                            return writeMessage;
                                        } else {
                                            String i[] = sl.split(",");
                                            writeMessage = "";
                                            sp[5] = sp[5].replaceAll("=","'") + "'";

                                            for (int x =0 ; x < i.length; x ++) {
                                                String a=  Mtree.get(sp[3]).select(i[x],sp[3],sp[5]);
                                                if (!a.equals("")) {
                                                    writeMessage = writeMessage + a;
                                                }
                                            }
                                            writeMessage = "+--" + sl + "--+\n" + writeMessage;
                                            return writeMessage;
                                        }

                                    }
                                } catch (Exception e) {
                                    String i[] = sl.split(",");
                                    writeMessage = "";
                                    for (int x =0 ; x < i.length; x ++) {
                                        String a= Mtree.get(sp[3]).select(i[x],sp[3]);
                                        if (!a.equals("")) {
                                            writeMessage = writeMessage + a;
                                        }
                                    }
                                    writeMessage = "+--" + sl + "--+\n" + writeMessage;
                                    return writeMessage;
                                }

                            } else {
                                if (sp[4].equals("where")) {
                                    try {
                                        if (sp[6].equals("like")) {
                                            String a = Mtree.get(sp[3]).forI(sp[5]);
                                            String b[] = a.split("§");
                                            writeMessage = "+--" + sp[1] + "------+§";
                                            for (int x=0;x < b.length; x ++) {
                                                COREINFORMATION emp = ClassInstanceFactory.create(COREINFORMATION.class, b[x]) ;	// 工具类自动设置
                                                String c = emp.getINFO();
                                                String d = c.substring(c.indexOf(sp[5]));
                                                d = d.substring(0,d.indexOf(";"));
                                                d = d.replaceAll(sp[5],"");
                                                if (d.contains(sp[7])) {
                                                    d = c.substring(c.indexOf(sp[1]));
                                                    d = d.substring(0,d.indexOf(";"));
                                                    d = d.replaceAll(sp[1] + "'","");
                                                    d = d.replaceAll("'","");
                                                    writeMessage = writeMessage + d + "§";
                                                }
                                            }
                                            return writeMessage;
                                        }
                                    }catch (Exception e) {}

                                    if (sp[5].contains("id=")) {
                                        String value = "id:" +sp[5].split("=")[1] + "|hiredate:2022-07-27|INFO:null|table:null§";
                                        COREINFORMATION c = ClassInstanceFactory.create(COREINFORMATION.class, value) ;	// 工具类自动设置
                                        writeMessage = Mtree.get(sp[3]).getId(c,sp[1]);
                                    } else {
                                        sp[5] = sp[5].replaceAll("=","'") + "'";
                                        writeMessage = Mtree.get(sp[3]).select(sp[1],sp[3],sp[5]);
                                    }
                                } else {
                                    writeMessage = Mtree.get(sp[3]).select(sp[1],sp[3]);
                                }
                                writeMessage = "+--" + sl + "--+\n" + writeMessage;
                                return writeMessage;
                            }

                        } catch (Exception e) {
                            writeMessage = Mtree.get(sp[3]).select(sp[1],sp[3]);
                            writeMessage = "+--" + sl + "--+\n" + writeMessage;
                            return writeMessage;
                        }

                    }
                } else {
                    if (sl.equals("*")) {
                        if (sp[2].equals("from")) {
                            try {
                                if (sp[4].equals("where")) {
                                    sp[5] = sp[5].replaceAll("=","'") + "'";
                                    String a = tree.forTa(sp[3],sp[5]);
                                    writeMessage = a;
                                    return  writeMessage;
                                }
                            } catch (Exception e) {

                            }
                        }
                    } else
                    if (sl.equals("counts")) {
                        if (sp[2].equals("from")) {
                            writeMessage = String.valueOf(tree.num(sp[3]));
                            return writeMessage;
                        }
                    } else if(!sp.equals("*")){
                        try {
                            if (sl.contains(",")) {
                                try {
                                    if (sp[4].equals("where")) {
                                            String i[] = sl.split(",");
                                            writeMessage = "";
                                            sp[5] = sp[5].replaceAll("=","'") + "'";

                                            for (int x =0 ; x < i.length; x ++) {
                                                String a=  tree.select(i[x],sp[3],sp[5]);
                                                if (!a.equals("")) {
                                                    writeMessage = writeMessage + a;
                                                }
                                            }
                                            writeMessage = "+--" + sl + "--+\n" + writeMessage;
                                            return writeMessage;

                                    }
                                } catch (Exception e) {
                                    String i[] = sl.split(",");
                                    writeMessage = "";
                                    for (int x =0 ; x < i.length; x ++) {
                                        String a= tree.select(i[x],sp[3]);
                                        if (!a.equals("")) {
                                            writeMessage = writeMessage + a;
                                        }
                                    }
                                    writeMessage = "+--" + sl + "--+\n" + writeMessage;
                                    return writeMessage;
                                }

                            } else {
                                if (sp[4].equals("where")) {
                                        sp[5] = sp[5].replaceAll("=","'") + "'";
                                        writeMessage = tree.select(sp[1],sp[3],sp[5]);
                                } else {
                                    writeMessage = tree.select(sp[1],sp[3]);
                                }
                                writeMessage = "+--" + sl + "--+\n" + writeMessage;
                                return writeMessage;
                            }

                        } catch (Exception e) {
                            writeMessage = tree.select(sp[1],sp[3]);
                            writeMessage = "+--" + sl + "--+\n" + writeMessage;
                            return writeMessage;
                        }
                    }

                    String a = tree.forT(sp[3]);
                    writeMessage = a;
                }

            }
            /**
             * @Date 2022-07-14
             * 删除属性
             */
            if (sp[0].equals("delete")) {
                if (sp[3].equals("where")) {
                    String table = sp[2];
                    if (sp[4].contains("=")) {
                        sp[4] = sp[4].replaceAll("=","'");
                        sp[4] = sp[4] + "'";
                    }
                    if (Mtree.containsKey(table)) {
                        Mtree.get(table).deleteBy(table,sp[4]);
                    } else {
                        tree.deleteBy(table,sp[4]);
                    }

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
                    sp[5] = sp[5].replaceAll("\"","");
                    sp[3] = sp[3].replaceAll("\"","");
                    String a;
                    if (!Mtree.containsKey(sp[1])) {
                        a = tree.forT(sp[1],sp[5]);
                    } else {
                        a = Mtree.get(sp[1]).forT(sp[1],sp[5]);
                    }
                    String table = sp[1];
                    String work = sp[3];
                    String ca[] = work.split("=");
                    work = work.replaceAll("=","'") + "';";
                    String res[] = a.split("\\|");
                    int ind = res[2].lastIndexOf(ca[0]);
                    String handle = res[2].substring(0,ind);
                    int i = res[2].indexOf(work.split("'")[0]);
                    String w2 = res[2].substring(i);
                    w2 = w2.replaceFirst("'","");
                    w2 = w2.replaceAll(work.split("'")[0],"");
                    w2 = w2.substring(0,w2.indexOf("'"));
                    String end = res[2].substring(ind + work.split("'")[0].length() + w2.length() + 1);
                    if (handle.contains("'")) {
                        handle = res[0] + "|" + res[1] + "|" + handle + work.replaceFirst("';","") + end + "|table:" + table + "§";
                    } else {
                        handle = res[0] + "|" + res[1] + "|" + handle + work.replaceFirst("';","") + end + "|table:" + table + "§";
                    }
                    System.out.println(handle);
                    COREINFORMATION c = add(handle);
                    if (Mtree.containsKey(table)) {
                        Mtree.get(sp[1]).deleteBy(sp[1],sp[5]);
                        Mtree.get(sp[1]).add(c);
                    } else {
                        tree.deleteBy(sp[1],sp[5]);
                        tree.add(c);
                    }

                    writeMessage = "1";
                }
            }
            /*
                以下是缓存功能
             */
            if (sp[0].equals("cache")) {
                if (sp[1].equals("insert")) {
                    if (sp[2].equals("into")) {

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
