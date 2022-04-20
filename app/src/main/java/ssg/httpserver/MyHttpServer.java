package ssg.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ssg.Generated;
import ssg.exceptions.HttpConfigurationException;
import ssg.httpserver.httpserverconfig.Configuration;
import ssg.httpserver.httpserverconfig.ConfigurationManager;

/**
 * Driver class for the Http Server, it permits serving html that were generated.
 */
@SuppressFBWarnings
@Generated
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.GuardLogStatement"})
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
     * @param customPort customPort provide by user input
     * @throws HttpConfigurationException if the configuration was wrong
     * @throws IOException if there was a problem when creating the server.
     */
    public void startServer(String customPort) throws HttpConfigurationException, IOException {
        logger.info("Server starting...");

        String configFilePath = "app/src/main/resources/http.json";
        ConfigurationManager.getInstance().loadConfigurationFile(configFilePath);
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        if (!"".equals(customPort)) {
            conf.setPort(Integer.parseInt(customPort));
        }

        logger.info("using Port: " + conf.getPort());
        logger.info("using WebRoot: " + conf.getWebroot());
        logger.info("index on: http://localhost:8080/content/index.html");

        server = HttpServer.create(new InetSocketAddress(conf.getPort()), 0);
        server.createContext("/", new FileHandler(conf.getWebroot()));
        server.start();
    }

    /**
     * Stop the server.
     */
    public void stop() {
        server.stop(0);
    }

    /**
     * Immutable Record handling the file.
     *
     * @param baseDir directory of the files that we want to serve
     */
    @Generated
    record FileHandler(String baseDir) implements HttpHandler {


        /**
         * Handle the given request and generate an appropriate response.
         * See {@link HttpExchange} for a description of the steps
         * involved in handling an exchange.
         *
         * @param exchange the exchange containing the request from the
         *                 client and used to send the response
         * @throws NullPointerException if exchange is {@code null}
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI uri = exchange.getRequestURI();
            File path = new File(baseDir, uri.toString());

            Headers h = exchange.getResponseHeaders();
            // Could be more clever about the content type based on the filename here.
            h.add("Content-Type", "text/html");

            OutputStream out = exchange.getResponseBody();

            if (path.exists()) {
                exchange.sendResponseHeaders(200, path.length());
                out.write(Files.readAllBytes(path.toPath()));
            } else {
                logger.error("File not found: " + path.getAbsolutePath());

                exchange.sendResponseHeaders(404, 0);
                out.write("404 File not found.".getBytes());
            }

            out.close();
        }
    }
}
