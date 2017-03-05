/*
 * Copyright TurtleTracker 2017
 */
package test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author KudoD
 */
public class SHA256Maker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String s = getPassHash("p@ssword");
        System.out.println(s);
        System.out.println(s.length());
    }

    private static String getPassHash(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(pass.getBytes("UTF-8"));
        byte[] b = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            hexString.append(Integer.toHexString(0xFF & b[i]));
        }
        return hexString.toString();
    }
}
