package org.astralpathsql.been;

import java.text.SimpleDateFormat;

public class IP {
    public String ip;
    public boolean i;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isI() {
        return i;
    }

    public void setI(boolean i) {
        this.i = i;
    }


    @Override
    public String toString() {
        return "ip:" + this.ip  + "|i:" + this.i;
    }
}
