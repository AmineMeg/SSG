package ssg.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ssg.buildsite.BuildSite;
import ssg.buildsite.BuildSiteImplementation;
import ssg.filereader.FileReader;
import ssg.filereader.FileReaderImplementation;
import ssg.filewriter.FileWriter;
import ssg.filewriter.FileWriterImplementation;
import ssg.parsertoml.ParserToml;
import ssg.parsertoml.ParserTomlImplementation;

/**
 * Module class for ParserToml.
 */
@SuppressWarnings("PMD.LawOfDemeter")
public class ParserTomlModule extends AbstractModule {

    /**
     * Configure dependency injection for BuildPage.
     */
    @Override
    protected void configure() {
        bind(ParserToml.class).to(ParserTomlImplementation.class);
        bind(FileReader.class)
            .annotatedWith(Names.named("FileReader"))
            .to(FileReaderImplementation.class);
        bind(FileWriter.class)
            .annotatedWith(Names.named("FileWriter"))
            .to(FileWriterImplementation.class);
    }
}