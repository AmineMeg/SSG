package ssg.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ssg.Generated;
import ssg.modules.BuildPageModule;
import ssg.modules.BuildSiteModule;

/**
 * IOC Container.
 */
@Generated
public final class Container {

    /**
     * IOC Container injector.
     */
    public static final Injector container = Guice.createInjector(new BuildPageModule(),
            new BuildSiteModule());
}
