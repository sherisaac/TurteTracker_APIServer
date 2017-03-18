/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server;

/**
 *
 * @author iyousuf
 */
public enum Opcodes {
    registerNest(0),
    updateNest(100),
    getNest(110),
    deleteNest(120),
    uploadPhoto(200),
    getPhoto(210),
    deletePhoto(220);

    private final int code;

    private Opcodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
