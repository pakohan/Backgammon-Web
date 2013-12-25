package controllers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import play.GlobalSettings;

public class BackGammonGlobal
        extends GlobalSettings {

    public final static Injector INJECTOR = createInjector();

    public static Injector createInjector() {
        return Guice.createInjector(new BackGammonCouchDBModule());
    }

    @Override
    public <A> A getControllerInstance(Class<A> controllerClass)
            throws Exception {

        return INJECTOR.getInstance(controllerClass);
    }
}
