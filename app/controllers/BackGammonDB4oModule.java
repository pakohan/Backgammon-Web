package controllers;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import controllers.de.htwg.upfaz.backgammon.controller.Persister;

public final class BackGammonDB4oModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("dbfile")).toInstance("/Users/mogli/gameDB.db4o");

        bind(Persister.class).to(DB4OPersister.class).in(Singleton.class);
    }
}
