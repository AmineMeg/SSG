package ssg.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ssg.Generated;
import ssg.modules.BuildPageModule;
import ssg.modules.BuildSiteModule;
import ssg.modules.TemplateHandlerModule;
import ssg.modules.TemplateProcesserModule;

/**
 * IOC Container.
 */
@Generated
public final class Container {
    /**
     * Private constructor to avoid instantiation.
     */
    private Container() {

    }

    /**
     * IOC Container injector.
     */
    public static final Injector container = Guice.createInjector(new BuildPageModule(),
            new BuildSiteModule(), new TemplateHandlerModule(),
            new TemplateProcesserModule());
}
