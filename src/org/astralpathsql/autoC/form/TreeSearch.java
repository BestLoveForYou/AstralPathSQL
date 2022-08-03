package org.astralpathsql.autoC.form;

import org.astralpathsql.been.COREINFORMATION;
import org.astralpathsql.node.BalancedBinaryTree;

import java.util.Map;

import static org.astralpathsql.server.MainServer.Mtree;
import static org.astralpathsql.server.MainServer.ta;

public class TreeSearch {
    public static void load() {
        Mtree.clear();
        for(String key:ta.keySet()){
            Table t = ta.get(key);
            if(t.getProp().equals("1")) {
                Mtree.put(t.getName(),new BalancedBinaryTree<COREINFORMATION>());
            }
        }
    }
}
