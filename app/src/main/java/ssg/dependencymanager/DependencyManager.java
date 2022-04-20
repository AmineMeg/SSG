package ssg.dependencymanager;

/**
 * Interface that determines dependencies and if a file should be recomputed.
 */
public interface DependencyManager {

    /**
     * Adds dependencyName as a dependency for fileName.
     *
     *  @param fileName name of the file whose dependency we are listing
     *  @param dependencyName name of the file which is a dependency
     */
    void addDependency(String fileName, String dependencyName);

    /**
     * Reads the dependency from the file were they were previously written if it exists.
     */
    void readDependenciesFromFile();

    /**
     * Reads the dependency from the file were they were previously written if it exists.
     *
     * @param fileName the file that we will analyze to determine if it should be recomputed
     * @return true if the file should be recomputed otherwise false
     */
    boolean isRecomputeRequired(String fileName);

    /**
     * Writes the dependencies in a file.
     */
    void writeDependenciesInFile();
}
