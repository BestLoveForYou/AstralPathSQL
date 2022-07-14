package org.astralpathsql.been;

public class News {
    private long nid ;
    private String title ;
    public News(long nid,String title) {
        this.nid = nid ;
        this.title = title ;
    }
    public String toString() {
        return "id:" + this.nid + "|title:" + this.title ;
    }

}
