package org.astralpathsql.been;

public class Student implements Comparable<Student>{
    private Integer id;
    private String cls;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }
    @Override
    public int compareTo(Student stu) {
        return this.id - stu.id;
    }
    @Override
    public String toString() {
        return "id:" + this.id + "|cls:" + this.cls;
    }
}
