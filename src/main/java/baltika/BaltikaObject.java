package baltika;

public class BaltikaObject {

    private int n_ob;
    private String txt;

    public BaltikaObject(int n_ob, String txt) {
        this.n_ob = n_ob;
        this.txt = txt;
    }

    public int getN_ob() {
        return n_ob;
    }

    public String getTxt() {
        return txt;
    }

    @Override
    public String toString() {
        return txt;
    }
}