package ssg.dependencymanager;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.tomlvaluetypewrapper.TomlArrayWrapper;

/**
 * Implementation of DependencyManager.
 */
public class DependencyManagerImplementation implements DependencyManager {

    /**
     * Logger.
     */
    //private static final Logger logger = LogManager.getLogger();

    /**
     * Map of each file to compute and their respective dependencies in TOML format.
     */
    //private Map<String, TomlArrayWrapper> dependencies;


    /**
     * Adds dependencyName as a dependency for fileName.
     *
     * @param fileName       name of the file whose dependency we are listing
     * @param dependencyName name of the file which is a dependency
     */
    @Override
    public void addDependency(String fileName, String dependencyName) {

    }

    /**
     * Reads the dependency from the file were they were previously written if it exists.
     */
    @Override
    public void readDependenciesFromFile() {

    }

    /**
     * Reads the dependency from the file were they were previously written if it exists.
     *
     * @param fileName the file that we will analyze to determine if it should be recomputed
     * @return true if the file should be recomputed otherwise false
     */
    @Override
    public boolean isRecomputeRequired(String fileName) {
        return false;
    }

    /**
     * Writes the dependencies in a file.
     */
    @Override
    public void writeDependenciesInFile() {

    }
}
