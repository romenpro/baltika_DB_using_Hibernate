import baltika.entity.Datas;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

public class DataTest {
    private SessionFactory factory;
    private Configuration configuration;
    private StandardServiceRegistryBuilder builder;
    private Session session;

    @Before
    public void before() {
        configuration = new Configuration().configure();
        builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        factory = configuration.buildSessionFactory(builder.build());
        session = factory.openSession();
    }

    @Test
    public void insertData(){
        Datas data = new Datas();
        data.setN_ob(123);
        data.setPartOfSN(.987);
        data.setVal("465");
        data.setHh_mi("13:00");
        data.setSqlDate(new Date(1472755977597L));
        Integer id = (Integer) session.save(data);
        Datas data1 = (Datas) session.get(Datas.class, id);
        assertEquals(123, data1.getN_ob());
        assertEquals(.987, data1.getPartOfSN(), 0.0);
        assertEquals("465", data1.getVal());
        assertEquals("13:00", data1.getHh_mi());
        assertEquals(new Date(1472755977597L), data1.getSqlDate());
    }


    @After
    public void after() {
        session.close();
        factory.close();
    }
}