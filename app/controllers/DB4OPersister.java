package controllers;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import controllers.de.htwg.upfaz.backgammon.controller.GameMap;
import controllers.de.htwg.upfaz.backgammon.controller.Persister;

import java.util.List;
import java.util.UUID;

public final class DB4OPersister
        implements Persister {

    private ObjectContainer db;

    @Inject
    public DB4OPersister(@Named("dbfile") final String file) {
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), file);
    }

    @Override
    public UUID createGame(final GameMap map) {
        final UUID uuid = UUID.randomUUID();
        map.set_id(uuid);
        map.setRevision("0");
        db.store(map);
        return uuid;
    }

    @Override
    public void saveGame(final GameMap map) {
        map.setRevision(Integer.toString(Integer.parseInt(map.getRevision()) + 1));
        db.store(map);
    }

    @Override
    public GameMap loadGame(final UUID id, final int rev) {
        List<GameMap> maps = db.query(new Predicate<GameMap>() {
            @Override
            public boolean match(GameMap map) {
                if (rev == -1) {
                    return map.get_id().equals(id);
                } else {
                    return map.get_id().equals(id) && Integer.parseInt(map.getRevision()) == rev;
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
