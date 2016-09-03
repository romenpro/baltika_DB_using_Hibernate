package baltika.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GR_INTEGR")
public class Gr_integr implements Serializable{

    @Id
    @Column(name = "N_OB")
    private int n_ob;

    @Id
    @Column(name = "N_GR_INTEGR")
    private int n_gr_integr;

    public int getN_ob() {
        return n_ob;
    }

    public void setN_ob(int n_ob) {
        this.n_ob = n_ob;
    }

    public int getN_gr_integr() {
        return n_gr_integr;
    }

    public void setN_gr_integr(int n_gr_integr) {
        this.n_gr_integr = n_gr_integr;
    }
}
