package baltika;

import baltika.entity.Datas;
import baltika.table.MyTableModel;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class OracleConnection {
    private static final String DB_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
    private SessionFactory factory;
    private Configuration configuration;
    private StandardServiceRegistryBuilder builder;
    private static final String USER = "cnt";
    private static final String PASS = "cnt";
    private String dbName = "DATAS";
    private Connection conn;
    private int id;
    private int n_ob;
    private Date spinnerDate;
    private double partOfSN;
    private MyTableModel factTableModel;

    private ArrayList<BaltikaObject> baltikaObjects;

    public OracleConnection() {
        try {
            configuration = new Configuration().configure();
            builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            factory = configuration.buildSessionFactory(builder.build());
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void createArrayLists() {
        baltikaObjects = new ArrayList<BaltikaObject>();
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            List employees = session.createQuery("SELECT DISTINCT n_ob, txt FROM Obekt" +
                    " WHERE syb_rnk IN (SELECT syb_rnk FROM Fid) AND syb_rnk <> 0" +
                    " AND n_ob IN (SELECT n_ob FROM Gr_integr) ORDER BY n_ob").list();
            for (Iterator iterator = employees.iterator(); iterator.hasNext(); ) {
                Object[] row = (Object[]) iterator.next();
                int n_ob = new Integer(row[0].toString());
                String txt = row[1].toString();
                baltikaObjects.add(new BaltikaObject(n_ob, txt));
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void createTable() throws SQLException {
        String createString =
                "create table " + dbName +
                        " (ID integer NOT NULL,  " +
                        "N_OB integer NOT NULL, " +
                        "DD_MM_YYYY DATE NOT NULL, " +
                        "HH_MI varchar(5) NOT NULL, " +
                        "VAL varchar(20) NOT NULL, " +
                        "PARTOFSN number not NULL, " +
                        "PRIMARY KEY (ID))";
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            stmt.execute(createString);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void deleteTable() throws SQLException {
        String createString =
                "drop table " + dbName;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            stmt.execute(createString);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void insertIntoTable() {
        String stringJComboBox = UI.getStringJComboBox();
        for (BaltikaObject arrayList : baltikaObjects) {
            if (arrayList.toString().equals(stringJComboBox)) {
                n_ob = arrayList.getN_ob();
            }
        }
        spinnerDate = UI.getSpinnerDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(spinnerDate);
        partOfSN = UI.getPartOfSN();
        factTableModel = UI.getFactTableModel();
        java.sql.Date sqlDate = new java.sql.Date(spinnerDate.getTime());
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (int j = 1; j < factTableModel.getColumnCount(); j++) {
                calendar.set(Calendar.DATE, j);
                spinnerDate = calendar.getTime();
                sqlDate.setTime(spinnerDate.getTime());
                for (int i = 0; i < factTableModel.getRowCount(); i++) {
                    Datas data = new Datas();
                    data.setN_ob(n_ob);
                    data.setSqlDate(sqlDate);
                    data.setHh_mi((String) factTableModel.getValueAt(i, 0));
                    data.setVal((String) factTableModel.getValueAt(i, j));
                    data.setPartOfSN(partOfSN);
                    session.save(data);
                    session.flush();
                    session.clear();
                }
            }
            tx.commit();
            DialogFrame.showSuccessMessage("Выполнено");
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void readFromTable() {
        String stringJComboBox = UI.getStringJComboBox();
        for (BaltikaObject arrayList : baltikaObjects) {
            if (arrayList.toString().equals(stringJComboBox)) {
                n_ob = arrayList.getN_ob();
            }
        }
        spinnerDate = UI.getSpinnerDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(spinnerDate);
        partOfSN = UI.getPartOfSN();
        factTableModel = UI.getFactTableModel();
        java.sql.Date sqlDate = new java.sql.Date(spinnerDate.getTime());

        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String hql = "FROM Datas WHERE n_ob = :n_ob AND " +
                    "sqlDate = :dd_mm_yyyy ORDER BY id";
            Query query = session.createQuery(hql);
            for (int j = 1; j < factTableModel.getColumnCount(); j++) {
                int i = 0;
                calendar.set(Calendar.DATE, j);
                spinnerDate = calendar.getTime();
                sqlDate.setTime(spinnerDate.getTime());
                query.setParameter("n_ob", n_ob);
                query.setParameter("dd_mm_yyyy", sqlDate);
                List data = query.list();
                for (Iterator iterator = data.iterator(); iterator.hasNext(); ) {
                    Datas dat = (Datas) iterator.next();
                    String val = dat.getVal();
                    double partOfSN = dat.getPartOfSN();
                    factTableModel.setValueAt(val, i, j);
                    UI.setPartOfSN(partOfSN);
                    i++;
                }
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void setBaltikaObjects() {
        baltikaObjects = new ArrayList<BaltikaObject>();
        baltikaObjects.add(new BaltikaObject(1016101, "Балтика Хабаровск"));
        baltikaObjects.add(new BaltikaObject(1312101, "Балтика Ростов"));
        baltikaObjects.add(new BaltikaObject(1413101, "Балтика СПб"));
        baltikaObjects.add(new BaltikaObject(1883101, "Балтика Самара"));
        baltikaObjects.add(new BaltikaObject(2042101, "Балтика Тула"));
        baltikaObjects.add(new BaltikaObject(4162001, "Челябинск 1"));
        baltikaObjects.add(new BaltikaObject(4230001, "Балтика-Воронеж"));
        baltikaObjects.add(new BaltikaObject(4383101, "Балтика Ярославль"));
        baltikaObjects.add(new BaltikaObject(4388101, "Балтика Вена"));
        baltikaObjects.add(new BaltikaObject(4392101, "Пикра-Балтика"));
        baltikaObjects.add(new BaltikaObject(9408001, "Балтика Новосибирск"));
    }

    public ArrayList<BaltikaObject> getBaltikaObjects() {
//        setBaltikaObjects();
        return baltikaObjects;
    }

}
