package controllers;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import controllers.de.htwg.upfaz.backgammon.controller.GameMap;
import controllers.de.htwg.upfaz.backgammon.persist.Persister;

import java.util.List;
import java.util.UUID;

public final class DB4OPersister
        implements Persister {

    private ObjectContainer db;

    public DB4OPersister() {
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "/Users/mogli/gameDB.db4o");
    }

    @Override
    public UUID createGame(final GameMap map) {
        final UUID uuid = UUID.randomUUID();
        map.setUuid(uuid);
        map.setRevision("0");
        db.store(map);
        return uuid;
    }

    @Override
    public void saveGame(final GameMap map) {
        map.setRevision(Integer.toString(Integer.parseInt(map.getRevision())+1));
        db.store(map);
    }

    @Override
    public GameMap loadGame(final UUID id, final int rev) {
        List<GameMap> maps = db.query(new Predicate<GameMap>() {
            public boolean match(GameMap map) {
                if (rev == -1) {
                    return map.getUuid().equals(id);
                } else {
                    return map.getUuid().equals(id) && Integer.parseInt(map.getRevision()) == rev;
                }
            }
        });

        if (maps.size() == 0) {
            return null;
        } else {
            int lastRev = 0;
            int index = 0;

            for (int i = 0; i < maps.size(); i++) {
                int newRev = Integer.parseInt(maps.get(i).getRevision());
                if (newRev > lastRev) {
                    lastRev = newRev;
                    index = i;
                }
            }

            return maps.get(index);
        }
    }

    @Override
    public void closeDB() {
        db.close();
    }
}
