package org.astralpathsql.been;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.astralpathsql.autoC.Hash.decode;
import static org.astralpathsql.server.MainServer.USER;

public class User {
    String username;
    String password;
    String jurisdiction;
    public static String gJurisdiction(String a, String user) {
        String re[] = a.split("§");
        String J = null;
        for(int x = 0; x < re.length ; x ++ ) {
            if (re[x].contains(user)) {
                String usera[] = re[x].split(":");
                J = usera[2];
            }
        }
        return J;
    }
    public static String checkUser(String c) {
        try {

            String a = USER;
            a = decode(a);
            String res[] = a.split("§");
            for (int x = 0 ;x <res.length; x ++) {
                String r[] = res[x].split(":");
                String ra[] = c.split(":");
                if (r[0].equals(ra[0])) {
                    if (r[1].equals(ra[1])) {
                        return r[2];
                    }
                }
            }
            return "false";
        } catch (Exception e) {
            return "false";
        }
    }
}
