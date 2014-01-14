package controllers;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import controllers.de.htwg.upfaz.backgammon.controller.Persister;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public final class BackGammonHibernateModule extends AbstractModule {
    private static final SessionFactory sessionFactory;

    static {
        final AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.configure("persistence.xml");
        sessionFactory =  cfg.buildSessionFactory();
    }

    @Override
    protected void configure() {
        bind(Persister.class).to(HibernatePersister.class).in(Singleton.class);
    }

    @Provides
    SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
