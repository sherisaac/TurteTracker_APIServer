/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author iyousuf
 */
public class Server implements HttpHandler {

    private static final Logger logger = LogManager.getLogger("Server");

    private final RequestRegistry requestHandlers = new RequestRegistry();

    private final JSONObject config;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            logger.info("Starting server...");

            JSONObject config = new JSONObject(new String(Files.readAllBytes(Paths.get("config.json"))));

            startServer(HttpServer.create(new InetSocketAddress(config.getInt("httpPort")), 0), config);

            if (config.getBoolean("useHTTPS")) {
                startServer(setUpHttps(config), config);
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

    private static void startServer(HttpServer server, JSONObject config) throws NoSuchAlgorithmException {
        server.createContext("/api", new Server(config));
//        server.createContext("/" + config.getString("sslHost"), new HTTPSConfirmHandler(config.getString("sslTarget")));
        int poolSize = Runtime.getRuntime().availableProcessors() + 1;
        server.setExecutor(Executors.newFixedThreadPool(poolSize));
//        server.setExecutor(null);
        server.start();
    }

    private static HttpServer setUpHttps(JSONObject config) throws Exception {
        HttpServer server = HttpsServer.create(new InetSocketAddress(config.getInt("httpsPort")), 0);
        SSLContext sslContext = SSLContext.getInstance("TLS");

        // initialise the keystore
        char[] password = "84267139".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream("www_turtledev_org.jks");
        ks.load(fis, password);
        // setup the key manager factory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);

        // setup the trust manager factory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        // setup the HTTPS context and parameters
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        ((HttpsServer) server).setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                try {
                    // initialise the SSL context
                    SSLContext c = SSLContext.getDefault();
                    SSLEngine engine = c.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());

                    // get the default parameters
                    SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                    params.setSSLParameters(defaultSSLParameters);

                } catch (NoSuchAlgorithmException ex) {
                    System.out.println("Failed to create HTTPS port");
                }
            }
        });
        return server;
    }

    public Server(JSONObject config) throws NoSuchAlgorithmException {
        this.config = config;
        DatabaseConnection.setUp(config);

    }

    @Override
    public void handle(HttpExchange he) {
        try {
            logger.info(he.getRequestMethod() + ": " + he.toString());
            he.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            he.getResponseHeaders().set("Access-Control-Max-Age", "1800");
            he.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            he.getResponseHeaders().set("Access-Control-Allow-Headers", "Authorization, Content-type");

            String method = he.getRequestMethod().toUpperCase();
            if (method.equals("OPTIONS")) {
                String s = "Good to go buddy!";
                he.sendResponseHeaders(200, s.length());
                he.getResponseBody().write(s.getBytes());
                he.close();
                return;
            }

            String[] path = he.getRequestURI().getPath().toLowerCase().substring(1).split("/");

            //only V1 for now
            requestHandlers.handle(new Pair(path[2], method), he, path);

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            try {
                String s = "Server error...";
                he.sendResponseHeaders(500, s.length());
                he.getResponseBody().write(s.getBytes());
            } catch (IOException ex1) {
                logger.error(ex.getMessage());
            }
        }
        he.close();
    }

}
