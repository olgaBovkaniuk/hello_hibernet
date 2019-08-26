package by.pvt.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static volatile HibernateUtil hibernateUtil;

    private SessionFactory sessionFactory;
    private SessionFactory testSessionFactory;

    public static synchronized HibernateUtil getInstance() {
        if (hibernateUtil == null) {
            hibernateUtil = new HibernateUtil();
        }
        return hibernateUtil;
    }

    private HibernateUtil() {
        sessionFactory =
                new MetadataSources(
                        new StandardServiceRegistryBuilder()
                        .configure()
                        .build()
                ).buildMetadata().buildSessionFactory();

        testSessionFactory =
                new MetadataSources(
                        new StandardServiceRegistryBuilder()
                                .configure("hibernate.cfg.test.xml")
                                .build()
                ).buildMetadata().buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public Session getTestSession() {
        return testSessionFactory.openSession();
    }
}
