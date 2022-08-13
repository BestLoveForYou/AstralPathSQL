package org.astralpathsql.autoC.core;

import org.astralpathsql.server.MainServer;

import static org.astralpathsql.server.MainServer.*;
import static org.astralpathsql.server.MainServer.lock;

public class DELETE {
    public static String delete(String sp[]) {
        String writeMessage = "-1";
        long stamp = MainServer.lock.readLock();
        long write = MainServer.lock.tryConvertToWriteLock(stamp);
        Boolean flag = true;
        while (flag)
        {
            if (write != 0) {
                stamp = write;
                String table = sp[2];
                try {
                    if (sp[3].equals("where")) {
                        if (sp[5].equals("=")) {
                            sp[4] = sp[4] + "'" + sp[6] + "'";
                        }
                        if (Mtree.containsKey(table)) {
                            Mtree.get(table).deleteBy(table,sp[4]);
                        } else {
                            tree.deleteBy(table,sp[4]);
                        }
                        writeMessage = "1";
                    }
                } catch (Exception e) {
                    if (Mtree.containsKey(table)) {
                        Mtree.get(table).clear(table);
                    } else {
                        tree.clear(table);
                    }
                    writeMessage = "1";
                }
                flag = false;
            } else {
                lock.unlockRead(stamp);
                write = lock.writeLock();
            }
        }
        lock.unlock(stamp);
        return writeMessage;
    }
}
