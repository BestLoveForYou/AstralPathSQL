package org.astralpathsql.autoC.core;

import org.astralpathsql.been.COREINFORMATION;

import static org.astralpathsql.file.Add.add;
import static org.astralpathsql.server.MainServer.*;

public class UPDATE {
    public static String update(String sp[]) {
        String writeMessage = "-1";
        if (sp[2].equals("set")) {
            sp[5] = sp[5] + "'" + sp[7] + "'";
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
        return writeMessage;
    }
}
