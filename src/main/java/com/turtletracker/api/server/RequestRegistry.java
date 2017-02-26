/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server;

import com.turtletracker.api.server.handlers.BadRequestHandler;
import com.sun.net.httpserver.HttpExchange;
import com.turtletracker.api.server.handlers.InvalidPathException;
import com.turtletracker.api.server.handlers.photo.DeletePhotoHandler;
import com.turtletracker.api.server.handlers.photo.GetPhotoHandler;
import com.turtletracker.api.server.handlers.photo.UploadPhotoHandler;
import com.turtletracker.api.server.handlers.nest.DeleteNestHandler;
import com.turtletracker.api.server.handlers.nest.GetNestGroupHandler;
import com.turtletracker.api.server.handlers.nest.GetNestHandler;
import com.turtletracker.api.server.handlers.nest.RegisterNestHandler;
import com.turtletracker.api.server.handlers.nest.UpdateNestHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author iyousuf
 */
public class RequestRegistry {

    private static final Logger logger = Logger.getLogger(RequestRegistry.class.getName());

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

        addHandler(new Pair("nests", "GET"), new GetNestGroupHandler());

    }

    protected void addHandler(Pair<String, String> op, Handler h) {
        handlers.put(op, h);
    }

    public void handle(Pair<String, String> id, HttpExchange he, String[] path) throws Exception {
        Handler handler = handlers.get(id);
        if (handler == null) {
            logger.log(Level.SEVERE, "Missing handler: {0}", id);
            handler = handlers.get(null);
        }
        if (!handler.validate(he.getRequestHeaders(), path)) {
            throw new InvalidPathException("Unauthorized...");
        }
        try (OutputStream out = he.getResponseBody()) {
            try (InputStream in = he.getRequestBody()) {
                handler.handle(he, in, out, path);
            }
        }
    }
}