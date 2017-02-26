/*
 * Copyright TurtleTracker 2017
 */
package com.turtletracker.api.server;

/**
 *
 * @author Ike [Admin@KudoDev.com]
 */
public class Pair<L, R> {

    private L l;
    private R r;

    public Pair(L l, R r) {
        this.l = l;
        this.r = r;
    }

    public L getLeft() {
        return l;
    }

    public R getRight() {
        return r;
    }

    @Override
    public int hashCode() {
        int hashFirst = l != null ? l.hashCode() : 0;
        int hashSecond = r != null ? r.hashCode() : 0;
        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return l.equals(pair.l) && r.equals(pair.r);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + l + ", " + r + ")";
    }
}
