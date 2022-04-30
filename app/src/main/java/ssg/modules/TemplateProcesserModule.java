package ssg.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ssg.filereader.FileReader;
import ssg.filereader.FileReaderImplementation;
import ssg.templatehandler.TemplateProcesser;
import ssg.templatehandler.TemplateProcesserImplementation;


/**
 * Module for TemplateProcesser.
 */
@SuppressWarnings({"PMD.LawOfDemeter"})
public class TemplateProcesserModule extends AbstractModule {

    /**
     * Configure dependency injection for TemplateHandler.
     */
    @Override
    protected void configure() {
        bind(TemplateProcesser.class).to(TemplateProcesserImplementation.class);
        bind(FileReader.class)
                .annotatedWith(Names.named("FileReader"))
                .to(FileReaderImplementation.class);
    }


}
