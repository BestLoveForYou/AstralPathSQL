package org.astralpathsql.been;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Emp implements Comparable<Emp>{
    private Integer id;
    private String username;
    private String password;
    private Double money;
    private Integer age ;
    private Date hiredate ;
    private String other;

    public Emp() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Double getMoney() {
        return money;
    }
    public void setMoney(Double salary) {
        this.money = salary;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    public Date getHiredate() {
        return hiredate;
    }
    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }
    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
    @Override
    public int compareTo(Emp emp) {
        return this.id - emp.id;
    }
    @Override
    public String toString() {
        SimpleDateFormat s = new SimpleDateFormat("yyyy.MM.dd");
        return "id:" + this.id + "|username:" + this.username + "|password:" + this.password + "|money:" + this.money + "|age:" + this.age + "|hiredate:" + s.format(this.hiredate) + "|other:" + this.other;
    }
}


