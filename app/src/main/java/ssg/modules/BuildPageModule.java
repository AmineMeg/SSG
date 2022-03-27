package ssg.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ssg.Generated;
import ssg.filereader.FileReader;
import ssg.filereader.FileReaderImplementation;
import ssg.filesplitter.FileSplitter;
import ssg.filesplitter.FileSplitterImplementation;
import ssg.filewriter.FileWriter;
import ssg.filewriter.FileWriterImplementation;
import ssg.htmlvalidator.HtmlValidator;
import ssg.htmlvalidator.HtmlValidatorW3Capi;
import ssg.markdowntohtmlconverter.MarkdownToHtmlConverter;
import ssg.markdowntohtmlconverter.MarkdownToHtmlConverterImplementation;

/**
 * Module class for BuildPage.
 */
@SuppressWarnings("PMD.LawOfDemeter")
@Generated
public class BuildPageModule extends AbstractModule {

    /**
     * Configure dependency injection for BuildPage.
     */
    @Override
    protected void configure() {
        bind(FileReader.class)
                .annotatedWith(Names.named("FileReader"))
                .to(FileReaderImplementation.class);
        bind(FileSplitter.class)
                .annotatedWith(Names.named("FileSplitter"))
                .to(FileSplitterImplementation.class);
        // TODO: bind(ParserTOML.class)
        //  .annotatedWith(Names.named("ParserTOML"))
        //  .to(FileReaderImplementation.class);
        bind(MarkdownToHtmlConverter.class)
                .annotatedWith(Names.named("MarkdownToHtmlConverter"))
                .to(MarkdownToHtmlConverterImplementation.class);
        // TODO: bind(TemplateHandler.class)
        //  .annotatedWith(Names.named("TemplateHandler"))
        //  .to(FileReaderImplementation.class);
        bind(FileWriter.class)
                .annotatedWith(Names.named("FileWriter"))
                .to(FileWriterImplementation.class);
        bind(HtmlValidator.class)
                .annotatedWith(Names.named("HtmlValidator"))
                .to(HtmlValidatorW3Capi.class);
    }
}
