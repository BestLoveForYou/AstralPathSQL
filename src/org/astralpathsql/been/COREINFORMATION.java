package org.astralpathsql.been;


import java.text.SimpleDateFormat;
import java.util.Date;

public class COREINFORMATION implements Comparable<COREINFORMATION>{
    private Integer id;
    private String INFO;
    private Date hiredate;

    private String table;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public COREINFORMATION() {
    }
    public COREINFORMATION(Integer id) {
        this.id = id;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getINFO() {
        return INFO;
    }

    public void setINFO(String INFO) {
        this.INFO = INFO;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    @Override
    public int compareTo(COREINFORMATION emp) {
        return this.id - emp.id;
    }
    @Override
    public String toString() {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        return "id:" + this.id + "|hiredate:" + s.format(this.hiredate) + "|INFO:" + this.INFO + "|table:" + this.table;
    }
}


