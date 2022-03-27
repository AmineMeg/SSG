package ssg.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ssg.Generated;
import ssg.buildpage.BuildPage;
import ssg.buildpage.BuildPageImplementation;
import ssg.staticfilehandler.InterfaceStaticFileHandler;
import ssg.staticfilehandler.StaticFileHandlerApache;

/**
 * Module class for BuildSite.
 */
@SuppressWarnings("PMD.LawOfDemeter")
@Generated
public class BuildSiteModule extends AbstractModule {
    /**
     * Configure dependency injection for BuildSite.
     */
    @Override
    protected void configure() {
        bind(BuildPage.class)
                .annotatedWith(Names.named("BuildPage"))
                .to(BuildPageImplementation.class);
        bind(InterfaceStaticFileHandler.class)
                .annotatedWith(Names.named("StaticFileHandler"))
                .to(StaticFileHandlerApache.class);
    }
}
