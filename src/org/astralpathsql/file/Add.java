package org.astralpathsql.file;

import org.astralpathsql.autoC.ClassInstanceFactory;
import org.astralpathsql.been.COREINFORMATION;
import org.astralpathsql.node.BalancedBinaryTree;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ConcurrentNavigableMap;

import static org.astralpathsql.server.MainServer.Mtree;

public class Add {
    public static BalancedBinaryTree addin(BalancedBinaryTree t) {
        ByteArrayOutputStream bos = Filer.readSQL();
        String n = new String(bos.toByteArray());
        if (n.isEmpty()) {
            n = "id:0|hiredate:2022.07.10|INFO:null|table:Test";
        }
        String res[] = n.split("§");
        try {
            for (int x = 0; x < res.length; x ++) {
                String value = res[x];
                COREINFORMATION emp = ClassInstanceFactory.create(COREINFORMATION.class, value) ;	// 工具类自动设置
                if (Mtree.containsKey(emp.getTable())) {
                    Mtree.get(emp.getTable()).add(emp);
                } else {
                    t.add(emp);
                }
            }
            return t;
        } catch (Exception e) {
            System.out.println("警告!");
        }
        return t;
    }
    public static COREINFORMATION add(String n) {
        String res[] = n.split("§");
        try {
            for (int x = 0; x < res.length; x ++) {
                String value = res[x];
                COREINFORMATION emp = ClassInstanceFactory.create(COREINFORMATION.class, value) ;	// 工具类自动设置
                return emp;
            }
        } catch (Exception e) {
            System.out.println("警告!");
        }
        return null;
    }
}
