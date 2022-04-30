package ssg.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import ssg.Generated;
import ssg.buildpage.BuildPage;
import ssg.buildpage.BuildPageImplementation;
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
import ssg.parsertoml.ParserToml;
import ssg.parsertoml.ParserTomlImplementation;
import ssg.templatehandler.TemplateHandler;
import ssg.templatehandler.TemplateHandlerImplementation;
import ssg.templatehandler.TemplateProcesser;
import ssg.templatehandler.TemplateProcesserImplementation;

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
        bind(BuildPage.class).to(BuildPageImplementation.class);
        bind(FileReader.class)
                .annotatedWith(Names.named("FileReader"))
                .to(FileReaderImplementation.class);
        bind(FileSplitter.class)
                .annotatedWith(Names.named("FileSplitter"))
                .to(FileSplitterImplementation.class);
        bind(ParserToml.class)
             .annotatedWith(Names.named("ParserToml"))
             .to(ParserTomlImplementation.class);
        bind(MarkdownToHtmlConverter.class)
                .annotatedWith(Names.named("MarkdownToHtmlConverter"))
                .to(MarkdownToHtmlConverterImplementation.class);
        bind(TemplateHandler.class)
                .annotatedWith(Names.named("TemplateHandler"))
                .to(TemplateHandlerImplementation.class);
        bind(TemplateProcesser.class)
                .annotatedWith(Names.named("TemplateProcesser"))
                .to(TemplateProcesserImplementation.class);
        bind(FileWriter.class)
                .annotatedWith(Names.named("FileWriter"))
                .to(FileWriterImplementation.class);
        bind(HtmlValidator.class)
                .annotatedWith(Names.named("HtmlValidator"))
                .to(HtmlValidatorW3Capi.class);
    }
}
