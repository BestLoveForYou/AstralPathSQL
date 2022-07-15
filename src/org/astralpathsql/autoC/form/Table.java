package org.astralpathsql.autoC.form;

import org.astralpathsql.autoC.ClassInstanceFactory;
import org.astralpathsql.file.Filer;
import org.astralpathsql.server.MainServer;
import org.astralpathsql.node.BalancedBinaryTree;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Map;

public class Table {


    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String table;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public static void read() {
        ByteArrayOutputStream bos = Filer.readTable();
        String n = "";
        n = new String(bos.toByteArray());
        if (n.isEmpty()) {
            n = "name:Test|table:char 请勿在无其他表时删除§";
        }
        String res[] = n.split("§");

        try {
            for (int x = 0; x < res.length; x ++) {
                String value = res[x];
                Table e = ClassInstanceFactory.create(Table.class, value) ;	// 工具类自动设置
                MainServer.ta.put(e.getName(),e);
            }
        } catch (Exception e) {
            System.out.println("警告!");
        }
    }
    public static Integer write() throws Exception{
        try {

            File file = new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "table.txt");
            if (!file.getParentFile().exists()) { 							// 父路径不存在
                file.getParentFile().mkdirs(); 							// 创建父路径
            }
            if (file.exists()) {											// 文件存在

            } else { 													// 文件不存在
                System.out.println(file.createNewFile()); 					// 创建新的文件
            }

            PrintWriter pu = new PrintWriter(new FileOutputStream(file));
            for(Map.Entry<String, Table> entry : MainServer.ta.entrySet()){
                String mapKey = entry.getKey();
                String mapValue = entry.getValue().toString();
                pu.print(mapValue + "§");

            }
            pu.close();
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }
    @Override
    public String toString(){
        return "name:" + this.name + "|table:" + this.table;
    }
}
