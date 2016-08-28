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


}
