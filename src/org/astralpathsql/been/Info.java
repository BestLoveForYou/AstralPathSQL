package org.astralpathsql.been;

public class Info {
    private long id ;
    private String title ;
    private String body;

    public long getId() {
        return id;
    }

    public Info(long id,String title,String body) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String toString() {
        return "id:" + this.id + "|title:" + this.title + "|body:" + this.body;
    }
}
