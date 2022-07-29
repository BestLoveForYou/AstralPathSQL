package org.astralpathsql.properties;

import org.astralpathsql.file.Filer;
import org.astralpathsql.server.MainServer;

import java.io.*;
import java.util.Properties;

import static org.astralpathsql.server.MainServer.*;

public class ProRead {
    public static void write() {
        try {
            Properties prop = new Properties();
            prop.setProperty("all_connect", String.valueOf(all_Connect));
            prop.setProperty("port", String.valueOf(PORT));

            prop.store(new FileOutputStream(
                    new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "info.properties"), Boolean.parseBoolean("utf-8")),
                    "Properties For SQL");
        } catch (Exception e) {

        }
    }
    public static Properties read() throws IOException {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "info.properties")));
            return prop;
        } catch (IOException e) {
            crNew();
            Properties prop = new Properties();
            prop.load(new FileInputStream(
                    new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "info.properties")));
            return prop;
        }

    }
    public static void crNew() {
        try {
            Properties prop = new Properties();
            prop.setProperty("all_connect", String.valueOf(all_Connect));
            prop.setProperty("port", String.valueOf(PORT));

            prop.store(new FileOutputStream(
                            new File("." + File.separator + "apsql" + File.separator + "config" + File.separator + "info.properties"), Boolean.parseBoolean("utf-8")),
                    "Properties For SQL");
        }catch (Exception e) {

        }
    }
}
