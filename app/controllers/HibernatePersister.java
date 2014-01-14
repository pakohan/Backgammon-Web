package controllers;

import com.google.inject.Inject;
import controllers.de.htwg.upfaz.backgammon.controller.GameMap;
import controllers.de.htwg.upfaz.backgammon.controller.Persister;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;
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
        return map.get_id();
    }

    @Override
    public void saveGame(GameMap map) {
        Session session = factory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.update(map);
        tx.commit();
    }

    @Override
    public void closeDB() {
        factory.getCurrentSession().close();
    }

    @Override
    public GameMap loadGame(UUID id, int rev) {
        Session session = factory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        List maps = session.createCriteria(GameMap.class).add(Restrictions.like("_id", id.toString())).list();
        tx.commit();
        if (maps.size() == 0) {
            return null;
        } else {
            return (GameMap) maps.get(0);
        }
    }
}
