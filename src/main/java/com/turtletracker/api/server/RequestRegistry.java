/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server;

import com.turtletracker.api.server.handlers.BadRequestHandler;
import com.sun.net.httpserver.HttpExchange;
import com.turtletracker.api.server.handlers.csv.GetNestCSVHandler;
import com.turtletracker.api.server.handlers.photo.DeletePhotoHandler;
import com.turtletracker.api.server.handlers.photo.GetPhotoHandler;
import com.turtletracker.api.server.handlers.photo.UploadPhotoHandler;
import com.turtletracker.api.server.handlers.nest.DeleteNestHandler;
import com.turtletracker.api.server.handlers.nest.GetNestFamilyHandler;
import com.turtletracker.api.server.handlers.nest.GetNestHandler;
import com.turtletracker.api.server.handlers.nest.QueryNestsHandler;
import com.turtletracker.api.server.handlers.nest.RegisterNestHandler;
import com.turtletracker.api.server.handlers.nest.UpdateNestHandler;
import com.turtletracker.api.server.handlers.user.AuthUserHandler;
import com.turtletracker.api.server.handlers.user.GetUserHandler;
import com.turtletracker.api.server.handlers.user.RegisterUserHandler;
import com.turtletracker.api.server.handlers.user.UpdateUserHandler;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iyousuf
 */
public class RequestRegistry {

//    private static final Logger logger = Logger.getLogger(RequestRegistry.class.getName());

    private Map<Pair<String, String>, Handler> handlers = new HashMap<>();

    public RequestRegistry() {
        addHandler(null, new BadRequestHandler());

        addHandler(new Pair("photo", "POST"), new UploadPhotoHandler());
        addHandler(new Pair("photo", "GET"), new GetPhotoHandler());
        addHandler(new Pair("photo", "DELETE"), new DeletePhotoHandler());

        addHandler(new Pair("nest", "POST"), new RegisterNestHandler());
        addHandler(new Pair("nest", "PUT"), new UpdateNestHandler());
        addHandler(new Pair("nest", "GET"), new GetNestHandler());
        addHandler(new Pair("nest", "DELETE"), new DeleteNestHandler());

        addHandler(new Pair("nests", "GET"), new GetNestFamilyHandler());
        addHandler(new Pair("nests", "POST"), new QueryNestsHandler());

        addHandler(new Pair("user", "POST"), new RegisterUserHandler());
        addHandler(new Pair("user", "PUT"), new UpdateUserHandler());
        addHandler(new Pair("user", "GET"), new GetUserHandler());

        addHandler(new Pair("auth", "GET"), new AuthUserHandler());

        addHandler(new Pair("csv", "GET"), new GetNestCSVHandler());

    }

    protected void addHandler(Pair<String, String> op, Handler h) {
        handlers.put(op, h);
    }

    public void handle(Pair<String, String> id, HttpExchange he, String[] path) throws Exception {
        Handler handler = handlers.get(id);
        if (handler == null) {
//            logger.log(Level.SEVERE, "Missing handler: {0}", id);
            handler = handlers.get(null);
        }
        if (!handler.validate(he.getRequestHeaders(), path)) {
            he.getResponseHeaders().set("WWW-Authenticate", "Basic realm=\"TurtleAPI\"");
            handler.sendResponse(he, 401, "{\"err\":\"Unauthorized\"}");
//            logger.log(Level.SEVERE, "Unauthorized");
            return;
        }
        try (OutputStream out = he.getResponseBody()) {
            try (InputStream in = he.getRequestBody()) {
                handler.handle(he, in, out, path);
            }
        }
    }
}
