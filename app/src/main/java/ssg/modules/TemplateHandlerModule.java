package ssg.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ssg.Generated;
import ssg.buildsite.BuildSite;
import ssg.buildsite.BuildSiteImplementation;
import ssg.filereader.FileReader;
import ssg.filereader.FileReaderImplementation;
import ssg.templatehandler.TemplateHandler;
import ssg.templatehandler.TemplateHandlerImplementation;
import ssg.templatehandler.TemplateProcesser;
import ssg.templatehandler.TemplateProcesserImplementation;

/**
 * Module class for TemplateHandler.
 */
@SuppressWarnings("PMD.LawOfDemeter")
@Generated
public class TemplateHandlerModule extends AbstractModule {

    /**
     * Configure dependency injection for TemplateHandler.
     */
    @Override
    protected void configure() {
        bind(TemplateHandler.class).to(TemplateHandlerImplementation.class);
        bind(FileReader.class)
                .annotatedWith(Names.named("FileReader"))
                .to(FileReaderImplementation.class);
        bind(TemplateProcesser.class)
                .annotatedWith(Names.named("TemplateProcesser"))
                .to(TemplateProcesserImplementation.class);
    }
}
