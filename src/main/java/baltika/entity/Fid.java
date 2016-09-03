package baltika.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "FID")
public class Fid implements Serializable{

    @Id
    @Column(name = "SYB_RNK")
    private int syb_rnk;

    @Id
    @Column(name = "N_FID")
    private int n_fid;

    public int getN_fid() {
        return n_fid;
    }

    public void setN_fid(int n_fid) {
        this.n_fid = n_fid;
    }

    public int getSyb_rnk() {
        return syb_rnk;
    }

    public void setSyb_rnk(int syb_rnk) {
        this.syb_rnk = syb_rnk;
    }
}
