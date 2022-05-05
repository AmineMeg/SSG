package ssg.httpserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.Generated;
import ssg.buildpage.BuildPage;
import ssg.config.SiteStructureVariable;
import ssg.exceptions.HttpConfigurationException;
import ssg.httpserver.httpserverconfig.Configuration;
import ssg.httpserver.httpserverconfig.ConfigurationManager;
import ssg.ioc.Container;

/**
 * Driver class for the Http Server, it permits serving html that were generated.
 */
@SuppressFBWarnings
@Generated
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.GuardLogStatement",
    "PMD.AvoidCatchingGenericException"})
public class MyHttpServer {

    /**
     * Logger.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Server instance.
     */
    private HttpServer server;


    /**
     * Start the HTTP server.
     *
     * @param customPort customPort provide by user input.
     * @param customDir customDir provide by user input.
     * @param inputRoot inputRoot provide by user input.
     * @throws HttpConfigurationException if the configuration was wrong.
     * @throws IOException if there was a problem when creating the server.
     */
    public void startServer(String customPort, String customDir, String inputRoot)
            throws HttpConfigurationException, IOException {

        String configFilePath = "app/src/main/resources/http.json";
        ConfigurationManager.getInstance().loadConfigurationFile(configFilePath);
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        checkArguments(customPort, customDir, inputRoot, conf);

        logger.info("Server starting...");
        logger.info("listening on Port: " + conf.getPort());
        logger.info("Server currently running on: http://localhost:" + conf.getPort() + "/");

        server = HttpServer.create(new InetSocketAddress(conf.getPort()), 0);
        server.createContext("/", new MenuHandler());
        server.createContext("/view", new FileHandler(conf.getWebroot()));
        server.createContext("/edit", new EditHandler(conf.getInputRoot()));
        server.createContext("/save", new SaveHandler(conf.getInputRoot(), conf.getWebroot()));
        server.start();
    }

    private void checkArguments(String customPort,
                                String customDir,
                                String inputRoot,
                                Configuration conf) {
        if (!"".equals(customPort)) {
            try {
                int customPortFormat = Integer.parseInt(customPort);
                if (customPortFormat > 1023 && customPortFormat < 65536) {
                    conf.setPort(customPortFormat);
                } else {
                    logger.error("The port provided have to be between 1024 and 65535, "
                            + "the default port have been reset");
                }
            } catch (NumberFormatException e) {
                logger.error(
                        "The port provided have to be a numeric, the default port have been reset",
                        e
                );
            }
        }
        if (!"".equals(customDir)) {
            conf.setWebroot(customDir);
        }
        conf.setInputRoot(inputRoot);
    }

    /**
     * Stop the server.
     */
    public void stop() {
        server.stop(0);
    }

    /**
     * Immutable Record handling menu.
     */
    @Generated
    record MenuHandler() implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Headers h = exchange.getResponseHeaders();
            // Could be more clever about the content type based on the filename here.
            h.add("Content-Type", "text/html");
            OutputStream out = exchange.getResponseBody();

            exchange.sendResponseHeaders(200, 0);
            out.write("""
                <!DOCTYPE html>
                <html lang="en">
                  <head>
                    <title>ssg</title>
                  </head>
                  <body>
                      <h1> Welcome to ssg ! </h1>
                      <h2> You may add /view/path/to/html/file to the url to view your html page
                      <br>
                      or <br>
                      You also can add /edit/path/to/html/file to the url to edit your markdown
                      corresponding to the html if you have chosen to build a website. </h2>
                  </body>
                </html>
                """.getBytes());
            out.flush();
            out.close();
        }
    }

    /**
     * Immutable Record handling the file.
     *
     * @param baseDir directory of the files that we want to serve.
     */
    @Generated
    record FileHandler(String baseDir) implements HttpHandler {


        /**
         * Handle the given request and generate an appropriate response.
         * See {@link HttpExchange} for a description of the steps
         * involved in handling an exchange.
         *
         * @param exchange the exchange containing the request from the
         *                 client and used to send the response.
         * @throws NullPointerException if exchange is {@code null}.
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            String uriFormated = uri.toString().replace("/view", "");
            File path = new File(baseDir, uriFormated);
            Headers h = exchange.getResponseHeaders();
            if (uriFormated.endsWith(".html")) {
                handleHtmlFile(exchange, uriFormated, path, h);
            } else {
                handleOtherFile(exchange, path);
            }
        }

        private void handleOtherFile(HttpExchange exchange, File path) throws IOException {
            OutputStream out = exchange.getResponseBody();

            if (path.exists()) {
                exchange.sendResponseHeaders(200, 0);
                Files.copy(path.toPath(), out);
                out.close();
            } else {
                logger.error("File not found: " + path.getAbsolutePath());

                exchange.sendResponseHeaders(404, 0);
                out.write("404 File not found.".getBytes());
            }
            out.flush();
            out.close();
        }

        private void handleHtmlFile(HttpExchange exchange, String uriFormated, File path, Headers h)
                throws IOException {
            // Could be more clever about the content type based on the filename here.
            h.add("Content-Type", "text/html");
            OutputStream out = exchange.getResponseBody();
            if (path.exists()) {
                try {
                    Configuration conf = ConfigurationManager
                            .getInstance()
                            .getCurrentConfiguration();
                    String button = "";
                    if (!"./".equals(conf.getInputRoot())) {
                        button = "<button type=\"button\"><a href=\"http://localhost:"
                                + conf.getPort()
                                + "/edit" + uriFormated + "\""
                                + "> Edit page </a></button>";
                    }
                    String response = button + Files.readString(path.toPath());
                    exchange.sendResponseHeaders(200, 0);
                    out.write(response.getBytes());
                } catch (HttpConfigurationException e) {
                    logger.error("couldn't get http configuration", e);
                }
            } else {
                logger.error("File not found: " + path.getAbsolutePath());

                exchange.sendResponseHeaders(404, 0);
                out.write("404 File not found.".getBytes());
            }
            out.flush();
            out.close();
        }
    }

    /**
     * Immutable Record serving the ssg editor.
     */
    @Generated
    record EditHandler(String inputDir) implements HttpHandler {

        /**
         * Handle the given request and generate an appropriate response.
         * See {@link HttpExchange} for a description of the steps
         * involved in handling an exchange.
         *
         * @param exchange the exchange containing the request from the
         *                 client and used to send the response.
         * @throws NullPointerException if exchange is {@code null}.
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            String formatedFileName = uri.toString()
                    .replace("/edit", "")
                    .replace(".html", ".md");
            File file = new File(inputDir, formatedFileName);
            Path path = Path.of(inputDir + formatedFileName);

            Headers h = exchange.getResponseHeaders();
            // Could be more clever about the content type based on the filename here.
            h.add("Content-Type", "text/html");

            OutputStream out = exchange.getResponseBody();

            if (file.exists()) {
                try {
                    sendEditorForm(exchange, uri, path, out);
                } catch (HttpConfigurationException e) {
                    logger.error("couldn't get http configuration", e);
                }
            } else {
                logger.error("File not found: " + file.getAbsolutePath());
                exchange.sendResponseHeaders(404, 0);
                out.write("404 File not found.".getBytes());
            }
            out.flush();
            out.close();
        }

        private void sendEditorForm(HttpExchange exchange, URI uri, Path path, OutputStream out)
                throws HttpConfigurationException, IOException {
            Configuration conf = ConfigurationManager
                    .getInstance()
                    .getCurrentConfiguration();
            String formSubmit =  "http://localhost:" + conf.getPort() + uri.toString()
                    .replace("/edit", "/save")
                    .replace(".html", ".md");
            String htmlEditor = Files.readString(
                    Path.of("app/src/main/resources/frontend/mdeditor.html")
            );
            String htmlEditorAfterUpdate = htmlEditor
                    .replace("http://localhost:8080/",
                            formSubmit)
                    .replace("Enter text here...", Files.readString(path));
            exchange.sendResponseHeaders(200, 0);
            out.write(htmlEditorAfterUpdate.getBytes());
        }
    }

    /**
     * Immutable Record serving to save editedFile.
     */
    @Generated
    record SaveHandler(String inputDir, String outputDir) implements HttpHandler {

        /**
         * Handle the given request and generate an appropriate response.
         * See {@link HttpExchange} for a description of the steps
         * involved in handling an exchange.
         *
         * @param exchange the exchange containing the request from the
         *                 client and used to send the response.
         * @throws NullPointerException if exchange is {@code null}.
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                URI uri = exchange.getRequestURI();
                InputStream inputStream = exchange.getRequestBody();
                ObjectMapper mapper = new ObjectMapper();
                Map jsonMap = mapper.readValue(inputStream, Map.class);
                String formatedFileName = uri.toString()
                        .replace("/save/", "");
                File file = new File(inputDir, formatedFileName);
                Path path = Path.of(inputDir + formatedFileName);
                if (file.exists()) {
                    saveAndBuildNewContentToFile(jsonMap, formatedFileName, path);
                    exchange.sendResponseHeaders(200, 0);
                } else {
                    logger.error("File not found: " + file.getAbsolutePath());
                    exchange.sendResponseHeaders(404, 0);
                }
                inputStream.close();
            }
        }

        private void saveAndBuildNewContentToFile(Map jsonMap, String formatedFileName, Path path)
                throws IOException {
            Files.writeString(path, jsonMap.get("content").toString());
            BuildPage buildPage = Container.container.getInstance(BuildPage.class);
            try {
                buildPage.run(inputDir + formatedFileName,
                        outputDir + SiteStructureVariable.CONTENTS);
            } catch (Exception e) {
                logger.error("run(): There was an issue during the conversion ", e);
            }
        }
    }
}
