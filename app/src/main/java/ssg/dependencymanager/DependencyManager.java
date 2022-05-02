package ssg.dependencymanager;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.Generated;
import ssg.config.SiteStructureVariable;
import ssg.exceptions.BadDependencyTomlFormatException;
import ssg.filemodifiedmanager.FileModifiedManager;
import ssg.filemodifiedmanager.FileModifiedManagerImplementation;
import ssg.filereader.FileReader;
import ssg.filereader.FileReaderImplementation;
import ssg.filewriter.FileWriter;
import ssg.filewriter.FileWriterImplementation;
import ssg.parsertoml.ParserToml;
import ssg.parsertoml.ParserTomlImplementation;
import ssg.tomlvaluetypewrapper.TomlArrayWrapper;
import ssg.tomlvaluetypewrapper.TomlStringWrapper;
import ssg.tomlvaluetypewrapper.TomlValueTypeWrapper;


/**
 * Implementation of DependencyManager.
 */
@SuppressFBWarnings
@Generated
@SuppressWarnings({"PMD.LooseCoupling", "PMD.LawOfDemeter","PMD.AppendCharacterWithChar",
    "PMD.GuardLogStatement","PMD.ExcessiveImports", "PMD.TooManyMethods",
    "PMD.AvoidInstantiatingObjectsInLoops"})
public final class DependencyManager  {

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger(DependencyManager.class);

    /**
     * Logger.
     */
    private final String outputDirectory;

    /**
     * Template directory.
     */
    private final String templateDirectory;

    /**
     * FileModifiedManager dependency.
     */
    @Inject
    @Named("FileModifiedManager")
    private FileModifiedManager fileModifiedManager;

    /**
     * FileReader dependency.
     */
    @Inject
    @Named("FileReader")
    private FileReader fileReader;

    /**
     * ParserToml dependency.
     */
    @Inject
    @Named("ParserToml")
    private ParserToml parserToml;

    /**
     * FileWriter dependency.
     */
    @Inject
    @Named("FileWriter")
    private FileWriter fileWriter;

    /**
     * Map of each file to compute and their respective dependencies.
     */
    private final ConcurrentHashMap<String, ArrayList<String>> dependencies;

    /**
     * Map of each dependency and the last time it was modified.
     */
    private final ConcurrentHashMap<String, LocalDateTime> lastModifiedDependencies;

    /**
     * Instance of the class for design pattern singleton. Volatile to prevent double check lock.
     */
    private static volatile DependencyManager instance;

    /**
     * Default constructor, private for design pattern singleton.
     */
    private DependencyManager(String outputDirectory, String templateDirectory) {
        this.dependencies = new ConcurrentHashMap<>();
        this.lastModifiedDependencies = new ConcurrentHashMap<>();
        this.outputDirectory = outputDirectory;
        this.templateDirectory = templateDirectory;
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
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
        });
        injector.injectMembers(this);

        logger.info("constructor() : created instance with outputdir : "
                + "{} and templatedir : {}", outputDirectory, templateDirectory);

    }


    /**
     * Returns the instance of DependencyManager following singleton pattern.
     * This method is using lazy initialization and is thread safe.
     * with double-checked locking : https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
     *
     * @param outputDirectory outputDirectory specified at launch.
     * @param templateDirectory relative to inputDir specified at launch.
     * @return unique instance of DependencyManagerImplementation.
     */
    public static DependencyManager getInstance(String outputDirectory, String templateDirectory) {

        logger.info("DependencyManagerImplementation : getting instance of class");
        DependencyManager toReturn = instance;
        if (toReturn != null) {
            return toReturn;
        }

        synchronized (DependencyManager.class) {
            if (instance == null) {
                instance = new DependencyManager(outputDirectory, templateDirectory);
            }

            return instance;
        }
    }

    /**
     * Returns the instance of DependencyManager following singleton pattern.
     * This method is using lazy initialization and is thread safe.
     * with double-checked locking : https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
     *
     * @return unique instance of DependencyManagerImplementation.
     */

    public static DependencyManager getInstance() {

        logger.info("DependencyManagerImplementation : getting instance of class");
        DependencyManager toReturn = instance;
        if (toReturn != null) {
            return toReturn;
        }

        synchronized (DependencyManager.class) {
            if (instance == null) {
                logger.error("getInstance() : You can't use no parameters"
                        + " getInstance because singleton instance wasn't created yet");
            }

            return instance;
        }
    }


    /**
     * Adds dependencyName as a dependency for fileName.
     *
     * @param fileName       name of the file whose dependency we are listing.
     * @param dependencyName name of the file which is a dependency.
     */
    public void addDependency(String fileName, String dependencyName) {

        logger.info("addDependency() : "
                        + "attempt to add {} as a dependency for file {}",
                dependencyName, fileName);

        if (dependencies.containsKey(fileName)
                && !dependencies.get(fileName).contains(dependencyName)) {
            dependencies.get(fileName).add(dependencyName);
        } else {
            ArrayList<String> dependenciesList = new ArrayList<>();
            dependenciesList.add(dependencyName);
            dependencies.put(fileName, dependenciesList);
        }

        logger.info("addDependency() : "
                        + " {} successfully added as a dependency for file {}",
                dependencyName, fileName);
    }

    /**
     * Reads the dependency from the file were they were previously written if it exists.
     *
     * @throws IOException when reading files.
     * @throws BadDependencyTomlFormatException when converting parsed toml to class representation.
     *
     * @throws IOException when attempting to read dependency file.
     * @throws BadDependencyTomlFormatException unexpected toml format in dependency file.
     */
    public void readDependenciesFromFile() throws IOException, BadDependencyTomlFormatException {

        if (!this.dependencies.isEmpty()) {
            logger.info("readDependenciesFromFile() : dependencies were already read");
            return;
        }

        try {
            if (Files.exists(Path.of(outputDirectory
                    + SiteStructureVariable.DEPENDENCIES_FILE))) {
                logger.info("readDependenciesFromFile() : attempt to read dependency file");
                String dependencyRawContent = fileReader
                        .read(outputDirectory + SiteStructureVariable.DEPENDENCIES_FILE);
                logger.info("readDependenciesFromFile() : attempt to parse dependency buffer");
                Map<String, TomlValueTypeWrapper> dependenciesToml =
                        parserToml.parse(dependencyRawContent);
                logger.info("readDependenciesFromFile() : attempt to convert dependency from toml");
                convertTomlInDependency(dependenciesToml);
            } else {
                logger.info("readDependenciesFromFile() : attempted to read dependency file "
                                + "but didn't found pre-existing dependency file"
                                + " {} in directory {}",
                        SiteStructureVariable.DEPENDENCIES_FILE, outputDirectory);
            }
        } catch (IOException e) {
            logger.error("readDependenciesFromFile() : encountered exception "
                    + "when attempting to read dependency file", e);
            throw e;
        } catch (BadDependencyTomlFormatException e) {
            logger.error("readDependenciesFromFile() : "
                    + "error unexpected toml format in dependency file ");
            throw e;
        }


    }

    /**
     * Reads the dependency from the file were they were previously written if it exists.
     *
     * @param fileName the file that we will analyze to determine if it should be recomputed.
     * @return true if the file should be recomputed otherwise false.
     * @throws IOException when determining if file exists.
     */
    public boolean isRecomputeRequired(String fileName) throws IOException {

        logger.info("isRecomputeRequired() : attempt to determine "
                + "if file {} should be recomputed", fileName);
        try {
            if (fileModifiedManager.wasFileModified(fileName, outputDirectory)) {
                logger.info("isRecomputeRequired() : file {} didn't exist "
                        + "or was modified and should be computed", fileName);
                return true;
            }

            if (dependencies.containsKey(fileName)) {
                for (String dependency : dependencies.get(fileName)) {
                    if (wasDependencyModified(dependency)) {
                        logger.info("isRecomputeRequired() : file {} is dependant on file {} which"
                                + " was modified so it should be recomputed",
                                fileName, dependency);
                        return true;
                    }
                }
            }
            logger.info("isRecomputeRequired() : file {} nor any or it's dependencies "
                    + "was modified. It does not need computing.", fileName);
            return false;

        } catch (IOException e) {
            logger.error("isRecomputeRequired() : exception when determining if file {} "
                    + "should be recomputed ", fileName, e);
            throw e;
        }
    }

    /**
     * Mainly for testing purposes.
     *
     * @return the dependencies in String format.
     */
    public String dependenciesToString() {
        return dependencies.toString();
    }

    /**
     * Mainly for testing purposes.
     * Deletes a dependency from the runtime dependency map.
     *
     * @param path path of the file to delete from the dependencies.
     */
    public void deleteFileFromDependencies(String path) {

        if (this.dependencies.containsKey(path)) {

            this.dependencies.remove(path);
            logger.info(" deleteFileFromDependencies() : deleted {} "
                    + "and it's dependency from the object memory", path);
        }
    }

    /**
     * Writes the dependencies in a file.
     *
     * @throws IOException when writing in file.
     */
    public void writeDependenciesInFile() throws IOException {

        try {
            StringBuilder stringBuilder = new StringBuilder();

            constructDependenciesString(stringBuilder);
            constructLastTimeModifiesString(stringBuilder);

            logger.info("writeDependenciesInFile() : attempt"
                    + " to write the following dependency string {}",
                    stringBuilder.toString());
            fileWriter.write(outputDirectory
                    + SiteStructureVariable.DEPENDENCIES_FILE, stringBuilder.toString());

        } catch (IOException e) {
            logger.error("DependencyManager : Error while writing file {} ",
                     SiteStructureVariable.DEPENDENCIES_FILE);
            throw e;
        }

    }

    /**
     * Constructs the string to write of dependencies and their last time modified.
     *
     * @param stringBuilder the string builder to use.
     * @throws IOException when getting lastTimeModified of file.
     */
    private void constructLastTimeModifiesString(StringBuilder stringBuilder) throws IOException {

        for (Map.Entry<String, LocalDateTime> entry : lastModifiedDependencies.entrySet()) {

            stringBuilder.append("\"");
            stringBuilder.append(entry.getKey());
            stringBuilder.append("\" = \"");
            File dependencyFile = new File(entry.getKey());
            FileTime lastModifiedTime = Files.getLastModifiedTime(
                    Path.of(templateDirectory + dependencyFile.getName()));
            Instant instant = lastModifiedTime.toInstant();
            LocalDateTime localDateTime = LocalDateTime
                    .ofInstant(instant, SiteStructureVariable.zoneOffsetId);
            stringBuilder.append(localDateTime);
            stringBuilder.append("\"\n");
        }
    }

    private void constructDependenciesString(StringBuilder stringBuilder) {

        for (Map.Entry<String, ArrayList<String>> entry : dependencies.entrySet()) {

            stringBuilder.append("\"");
            stringBuilder.append(entry.getKey());
            stringBuilder.append("\" = [ ");
            ArrayList<String> arrayList = entry.getValue();

            for (int i = 0; i < arrayList.size(); i++) {

                stringBuilder.append("\"");
                stringBuilder.append(arrayList.get(i));
                stringBuilder.append("\"");

                if (i != arrayList.size() - 1) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(" ");
            }
            stringBuilder.append("]\n");
        }
    }

    /**
     * Process parsed toml map of dependencies and insert it into dependencies argument.
     *
     * @param dependenciesToml tomlDependencies to convert into dependencies.
     */
    private void convertTomlInDependency(Map<String, TomlValueTypeWrapper> dependenciesToml)
            throws BadDependencyTomlFormatException {

        for (Map.Entry<String, TomlValueTypeWrapper> entry : dependenciesToml.entrySet()) {

            String key = entry.getKey().substring(1, entry.getKey().length() - 1);
            logger.info("convertTomlInDependency : currently converting for entry {}", key);

            if (key.endsWith("md")) {

                convertMdEntryToToml(entry, key);
            } else {
                if (entry.getValue() instanceof TomlStringWrapper tomlStringWrapper) {
                    lastModifiedDependencies.put(key,
                            LocalDateTime.parse(tomlStringWrapper.toString()));
                } else {
                    logger.error("convertTomlInDependency() expected"
                            + " toml String but it wasn't {}", entry.getValue().toString());
                    throw new BadDependencyTomlFormatException(
                            "expected toml string but it wasn't");
                }
            }
        }

    }

    /**
     * Converts a parsed toml map entry ending with .md to the dependency instance variable format.
     *
     * @param entry entry to convert.
     * @param key key to add the dependency in the dependencies map.
     *
     * @throws  BadDependencyTomlFormatException when converting.
     */
    private void convertMdEntryToToml(Map.Entry<String,
            TomlValueTypeWrapper> entry, String key)
            throws BadDependencyTomlFormatException {

        logger.info("convertTomlInDependency : key ends with .md");

        if (entry.getValue() instanceof TomlArrayWrapper tomlArrayWrapper) {

            ArrayList<String> dependenciesList = tomlArrayToArrayConverter(tomlArrayWrapper);
            if (!dependencies.containsKey(key)) {
                dependencies.put(key, dependenciesList);
            } else {
                logger.error("convertTomlInDependency() : excepted only one "
                        + "toml variable per file but found more than"
                        + " one current dependency state : {}", dependenciesToString());
                throw new BadDependencyTomlFormatException("Excepted only one toml variable "
                        + "per file but found more than one");
            }
        } else {
            logger.error("convertTomlInDependency() : "
                    + "excepted toml array for value or variables "
                    + "in dependency file but found another type");
            throw new BadDependencyTomlFormatException(
                    "Excepted toml array for value or variables in "
                            + "dependency file but found another type");
        }
    }

    /**
     * Converts the content of a TomlArrayWrapper of only strings into a Java ArrayList.
     *
     * @param tomlArrayWrapper Toml Array to convert into java ArrayList.
     * @return ArrayList of String.
     */
    private ArrayList<String> tomlArrayToArrayConverter(TomlArrayWrapper tomlArrayWrapper)
            throws BadDependencyTomlFormatException {
        ArrayList<String> dependenciesList = new ArrayList<>();
        Iterator<TomlValueTypeWrapper> iterator = tomlArrayWrapper.iterator();

        while (iterator.hasNext()) {

            TomlValueTypeWrapper rawTypeNext = iterator.next();

            if (rawTypeNext instanceof TomlStringWrapper) {

                logger.info("tomlArrayToArrayConverter() : converting dependency"
                        + " to toml for dependency {}", rawTypeNext.toString());
                dependenciesList.add(rawTypeNext.toString());
            } else {
                logger.error("tomlArrayToArrayConverter() : expected a toml "
                        + "string inside the array of dependencies");
                throw new BadDependencyTomlFormatException("Expected an array of string for"
                        + " dependency toml file but found another type");
            }
        }
        return dependenciesList;
    }

    /**
     * Returns true is there is already a Map entry in dependencies for a specific file.
     *
     * @param path file we want to know if it's already in the dependencies.
     * @return Boolean.
     */
    public boolean hasDependency(String path) {
        return this.dependencies.containsKey(path);
    }

    /**
     * Process all dependencies of fileName recursively and adds them to the variable.
     *
     * @param templateMetadata string of the context to use for dependency determining.
     * @param fileName name of the md file studied.
     */
    public void determineDependencies(String fileName, String templateMetadata) {

        try {
            logger.info("determineDependencies() : determining dependencies for file {}", fileName);

            Stack<String> stack = new Stack<>();

            if (!"".equals(templateMetadata)) {
                this.addDependency(fileName, templateDirectory
                        + templateMetadata);
                stack.add(templateDirectory + templateMetadata);
                if (!this.lastModifiedDependencies.containsKey(
                        templateDirectory + templateMetadata)) {
                    FileTime fileTime = Files.getLastModifiedTime(
                            Path.of(templateDirectory + templateMetadata));
                    Instant instant = fileTime.toInstant();
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(
                            instant, SiteStructureVariable.zoneOffsetId);
                    lastModifiedDependencies.put(templateDirectory
                            + templateMetadata, localDateTime);
                }
            }
            while (!stack.empty()) {

                processStack(fileName, stack);
            }
        } catch (IOException e) {
            logger.error("determineDependencies() : Error while reading "
                    + "templates for file {} to determine dependencies", fileName);
        }
    }

    /**
     * Process the stack of dependencies to determine all dependencies without using recursion.
     *
     * @param fileName name of the file that we are determining the dependencies of.
     * @param stack the stack.
     * @throws IOException when getting lastModifiedTime.
     */
    private void processStack(String fileName, Stack<String> stack) throws IOException {

        String currentDependencyPath = stack.pop();
        String buffer = fileReader.read(currentDependencyPath);
        ArrayList<String> resolvedDependencies = findDependenciesForBuffer(buffer);

        for (String dependency : resolvedDependencies) {
            this.addDependency(fileName, templateDirectory + dependency);
            stack.add(templateDirectory + dependency);
            if (!this.lastModifiedDependencies.containsKey(dependency)) {
                FileTime fileTime = Files.getLastModifiedTime(Path.of(templateDirectory
                        + dependency));
                Instant instant = fileTime.toInstant();
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant,
                        SiteStructureVariable.zoneOffsetId);
                lastModifiedDependencies.put(templateDirectory + dependency, localDateTime);
            }
        }
    }

    /**
     * Reads a file content and find all included files in it.
     *
     * @param buffer content to parse with regex.
     * @return ArrayList of String.
     */
    private ArrayList<String> findDependenciesForBuffer(String buffer) {

        Pattern pattern = Pattern.compile("\\{%[ \t]*include[ \t]*\"(.*?)\"[ \t]*%}");
        Matcher matcher = pattern.matcher(buffer);

        ArrayList<String> resolvedDependencies = new ArrayList<>();

        while (matcher.find()) {
            logger.info("findDependenciesForBuffer() : found"
                    + " dependency {} ", matcher.group(1));
            resolvedDependencies.add(matcher.group(1));
        }

        return resolvedDependencies;
    }


    /**
     * Returns true if a dependency was modified since last build.
     *
     * @param dependencyName name of the dependency we are interrogating.
     * @return boolean.
     * @throws IOException when getting lastTimeModified.
     */
    private boolean wasDependencyModified(String dependencyName) throws IOException {

        try {

            logger.info("wasDependencyModified :  determining if file {} "
                    + "was modified since last build ",  dependencyName);
            logger.info("wasDependencyModified :  current lastModified status : {} ",
                    lastModifiedDependencies.toString());

            if (lastModifiedDependencies.containsKey(dependencyName)) {

                FileTime currentLastModified = Files.getLastModifiedTime(Path.of(dependencyName));
                LocalDateTime ldt = lastModifiedDependencies.get(dependencyName);
                Instant instant = ldt.toInstant(SiteStructureVariable
                        .zoneOffsetId.getRules().getOffset(ldt));
                FileTime previousLastModified = FileTime.from(instant);
                return  currentLastModified.compareTo(previousLastModified) > 0;

            } else {
                return true;
            }

        } catch (IOException e) {
            logger.error("wasDependencyModified() : Error while getting last modified time");
            throw e;
        }
    }

    /**
     * Adds an entry in lastTimeModifiedDependencies Map.
     * Mainly for testing purposes.
     *
     * @param key key to add in map.
     * @param value value to put in map.
     */
    public void addLastTimeModifiedEntry(String key, LocalDateTime value) {
        this.lastModifiedDependencies.put(key, value);
    }

    /**
     * Removes an entry in lastTimeModifiedDependencies Map.
     * Mainly for testing purposes.
     *
     * @param key key to add in map.
     */
    public void deleteEntryLastTimeModifiedEntry(String key) {
        this.lastModifiedDependencies.remove(key);
    }

    /**
     * Returns instance outputDirectory.
     *
     * @return String.
     */
    public String getOutputDirectory() {
        return  this.outputDirectory;
    }

}
