package baltika.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "DATAS")
public class Datas {

    @Id @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Column(name = "N_OB")
    private int n_ob;

    @Column(name = "DD_MM_YYYY")
    private Date sqlDate;

    @Column(name = "HH_MI")
    private String hh_mi;

    @Column(name = "VAL")
    private String val;

    @Column(name = "PARTOFSN")
    private double partOfSN;

    public Datas() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getN_ob() {
        return n_ob;
    }

    public void setN_ob(int n_ob) {
        this.n_ob = n_ob;
    }

    public Date getSqlDate() {
        return sqlDate;
    }

    public void setSqlDate(Date sqlDate) {
        this.sqlDate = sqlDate;
    }

    public String getHh_mi() {
        return hh_mi;
    }

    public void setHh_mi(String hh_mi) {
        this.hh_mi = hh_mi;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public double getPartOfSN() {
        return partOfSN;
    }

    public void setPartOfSN(double partOfSN) {
        this.partOfSN = partOfSN;
    }
}
