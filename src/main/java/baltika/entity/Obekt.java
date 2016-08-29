package baltika.entity;

import javax.persistence.*;

@Entity
@Table(name = "OBEKT")
public class Obekt {

    @Id
    @Column(name = "N_OB")
    private int n_ob;

    @Id
    @Column(name = "SYB_RNK")
    private int syb_rnk;

    @Column(name = "TXT")
    private String txt;

    public int getN_ob() {
        return n_ob;
    }

    public void setN_ob(int n_ob) {
        this.n_ob = n_ob;
    }

    public int getSyb_rnk() {
        return syb_rnk;
    }

    public void setSyb_rnk(int syb_rnk) {
        this.syb_rnk = syb_rnk;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
