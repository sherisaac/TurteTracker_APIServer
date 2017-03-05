/*
 * Copyright TurtleTracker 2017
 */
package test;

import java.util.Random;

/**
 *
 * @author KudoD
 */
public class APIKeyGen {

    public static void main(String[] args) {
        String s = "Basic 09090909";
        System.out.println(s.substring(6));
        Random r = new Random();
        String c = "abcdefghijklmnopqrstuvwxyz0123456789";
        char[] out = new char[75];
        for (int i = 0; i < 75; i++) {
            out[i] = c.charAt(r.nextInt(c.length()));
        }
        System.out.println(new String(out));
    }

}
