package ssg.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ssg.Generated;
import ssg.dependencymanager.DependencyManager;
import ssg.dependencymanager.DependencyManagerImplementation;
import ssg.filemodifiedmanager.FileModifiedManager;
import ssg.filemodifiedmanager.FileModifiedManagerImplementation;
import ssg.filereader.FileReader;
import ssg.filereader.FileReaderImplementation;
import ssg.filewriter.FileWriter;
import ssg.filewriter.FileWriterImplementation;
import ssg.parsertoml.ParserToml;
import ssg.parsertoml.ParserTomlImplementation;


/**
 * Module class for DependencyManager.
 */
@Generated
@SuppressWarnings("PMD.LawOfDemeter")
public class DependencyManagerModule extends AbstractModule {

    /**
     * Configure dependency injection for BuildPage.
     */
    @Override
    protected void configure() {
        bind(DependencyManager.class).to(DependencyManagerImplementation.class);
        bind(FileModifiedManager.class)
                .annotatedWith(Names.named("FileModifiedManager"))
                .to(FileModifiedManagerImplementation.class);
        bind(FileReader.class)
                .annotatedWith(Names.named("FileReader"))
                .to(FileReaderImplementation.class);
        bind(ParserToml.class)
                .annotatedWith(Names.named("ParserToml"))
                .to(ParserTomlImplementation.class);
        bind(FileWriter.class)
                .annotatedWith(Names.named("FileWriter"))
                .to(FileWriterImplementation.class);

    }

}
