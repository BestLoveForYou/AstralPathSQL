package org.astralpathsql.autoC;

import org.astralpathsql.file.Filer;

import java.util.TimerTask;

import static org.astralpathsql.server.MainServer.tree;

public class SaveTask implements Runnable {
    @Override
    public void run() {
        System.out.println("[INFO]自动保存....");
        try {
            Filer.writeSQL(tree);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
