/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server.handlers.nest;

import com.turtletracker.api.server.handlers.*;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class NestNotFoundException extends Exception {

    public NestNotFoundException(String string) {
        super("{\"err\":\"Nest: " + string + " not found...\"}");
    }

}
