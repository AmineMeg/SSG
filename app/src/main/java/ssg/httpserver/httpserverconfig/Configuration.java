package ssg.httpserver.httpserverconfig;

/**
 * Http server configuration.
 */
@SuppressWarnings("PMD.DataClass")
public class Configuration {

    /**
     * Port of the server.
     */
    private int port;

    /**
     * Root Directory.
     */
    private String webroot;

    /**
     * input Directory.
     */
    private String inputRoot;

    /**
     * port getter.
     *
     * @return the current port.
     */
    public int getPort() {
        return port;
    }

    /**
     * port setter.
     *
     * @param port custom port.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Webroot getter.
     *
     * @return current webRoot.
     */
    public String getWebroot() {
        return webroot;
    }

    /**
     * Webroot setter.
     *
     * @param webroot represent the root directory that serve files.
     */
    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }

    /**
     * inputRoot getter.
     *
     */
    public String getInputRoot() {
        return inputRoot;
    }

    /**
     * inputRoot setter.
     *
     * @param inputRoot represent the directory the directory where files come from.
     */
    public void setInputRoot(String inputRoot) {
        this.inputRoot = inputRoot;
    }
}
