package controllers;

import com.google.inject.Inject;
import controllers.de.htwg.upfaz.backgammon.controller.GameMap;
import controllers.de.htwg.upfaz.backgammon.persist.Persister;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import java.util.UUID;

public final class HibernatePersister implements Persister {
    @Inject
    private SessionFactory factory;

    @Override
    public UUID createGame(GameMap map) {
        Session session = factory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(map);
        tx.commit();
        return map.getUuid();
    }

    @Override
    public void saveGame(GameMap map) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void closeDB() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public GameMap loadGame(UUID id, int rev) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
