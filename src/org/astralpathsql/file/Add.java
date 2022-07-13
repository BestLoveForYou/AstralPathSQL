package org.astralpathsql.file;

import org.astralpathsql.autoC.ClassInstanceFactory;
import org.astralpathsql.been.Emp;
import org.astralpathsql.tree.BalancedBinaryTree;

import java.io.ByteArrayOutputStream;

public class Add {
    public static BalancedBinaryTree addin(BalancedBinaryTree t) {
        ByteArrayOutputStream bos = Filer.readSQL();
        String n = new String(bos.toByteArray());
        if (n.isEmpty()) {
            n = "id:0|username:test|password:null|money:0.0|age:0|hiredate:2022.07.10|other:测试数据!千万不要在无其他数据时把这个删除!§";
        }
        String res[] = n.split("§");

        try {
            for (int x = 0; x < res.length; x ++) {
                String value = res[x];
                Emp emp = ClassInstanceFactory.create(Emp.class, value) ;	// 工具类自动设置
                t.add(emp);
            }
            return t;
        } catch (Exception e) {
            System.out.println("警告!");
        }
        return t;
    }
}
