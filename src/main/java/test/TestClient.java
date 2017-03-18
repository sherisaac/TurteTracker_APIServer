/*
 * Copyright Accusoft 2017
 */
package test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author iyousuf
 */
public class TestClient {

    public static final String ip = "127.0.0.1:8888";

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

//        String userId = registerUser("ikeotl");
//        String nestId = registerNest("testFam");
//        String photoId = "mtmz0bygb8ulcit";
//        getNest(nestId);
//        getNests("general");
//        updateNest(nestId);
//        deleteNest(nestId);
//        getNest(nestId);
//        System.out.println(uploadPhoto("photos/test.jpg"));
//        deletePhoto(photoId);
//        updateUser("ikeotl");
queryNests("admin");
        System.out.println("time: " + (System.currentTimeMillis() - startTime));
    }

    private static void getNests(String family) throws Exception {
        URL url = new URI("http", ip, "/api/v1/nests/" + family, null, null).toURL();
        Map<String, String> headers = new HashMap<>();
        String base = Base64.getEncoder().encodeToString("admin:p@ssword".getBytes("UTF-8"));
        headers.put("Authorization", "Basic " + base);
        Response res = new Response();
        System.out.println(readString(doGet(url, headers, res)));
    }

    private static void getNest(String id) throws Exception {
        URL url = new URI("http", ip, "/api/v1/nest/" + id, null, null).toURL();
        Map<String, String> headers = new HashMap<>();
        String base = Base64.getEncoder().encodeToString("admin:p@ssword".getBytes("UTF-8"));
        headers.put("Authorization", "Basic " + base);
        Response res = new Response();
        System.out.println(readString(doGet(url, headers, res)));
    }

    private static void updateUser(String username) throws Exception {
        URL url = new URI("http", ip, "/api/v1/user/" + username, null, null).toURL();
        Map<String, String> headers = new HashMap<>();
        String base = Base64.getEncoder().encodeToString("ikeotl:abc".getBytes("UTF-8"));
        headers.put("Authorization", "Basic " + base);
        Response res = new Response();
        JSONObject json = new JSONObject();

        json.put("password", "abc");
        json.put("userId", "100"); // wont actually update this one

        json.put("firstname", "ISAAV");
        System.out.println(readString(doPut(url, headers, json.toString().getBytes(), res)));
    }

    private static String registerUser(String username) throws Exception {
        URL url = new URI("http", ip, "/api/v1/user", null, null).toURL();
        Map<String, String> headers = new HashMap<>();
        String base = Base64.getEncoder().encodeToString("admin:p@ssword".getBytes("UTF-8"));
        headers.put("Authorization", "Basic " + base);
        Response res = new Response();
        JSONObject json = new JSONObject();

        json.put("username", username);
        json.put("password", "abc");
        json.put("firstName", "isaac");
        json.put("lastName", "boop");
        json.put("role", 10);

        JSONObject o = new JSONObject(readString(doPost(url, headers, json.toString().getBytes(), res)));
        System.out.println(o.toString());
        return o.getString("userId");
    }

    private static void queryNests(String query) throws Exception {
        URL url = new URI("http", ip, "/api/v1/nests", null, null).toURL();
        Map<String, String> headers = new HashMap<>();
        String base = Base64.getEncoder().encodeToString("admin:p@ssword".getBytes("UTF-8"));
        headers.put("Authorization", "Basic " + base);
        Response res = new Response();
        JSONObject json = new JSONObject();

        json.put("username", "ikeotl");

        JSONObject o = new JSONObject(readString(doPost(url, headers, json.toString().getBytes(), res)));
        System.out.println(o.toString());
    }

    private static String registerNest(String family) throws Exception {
        URL url = new URI("http", ip, "/api/v1/nest", null, null).toURL();
        Map<String, String> headers = new HashMap<>();
        String base = Base64.getEncoder().encodeToString("admin:p@ssword".getBytes("UTF-8"));
        headers.put("Authorization", "Basic " + base);
        Response res = new Response();
        JSONObject json = new JSONObject();

        JSONObject location = new JSONObject();
        location.put("longitude", 41.2222);
        location.put("latitude", 1.2222);
        json.put("location", location);
        json.put("notes", "THIS IS A NOTEEEEEE!@#@###$$#ESDF");

//        JSONArray photos = new JSONArray();
//        photos.put("blk1eetc9wzn8fh");
//        photos.put("7zzimw4oqw8h223");
//        photos.put("71739y7sy8d0wh6");
//        photos.put("kn4dm1qkw2lgbhy");
//        json.put("photos", photos);
        json.put("family", family);

        JSONObject o = new JSONObject(readString(doPost(url, headers, json.toString().getBytes(), res)));
        return o.getString("nestId");
    }

    private static void updateNest(String nestId) throws Exception {
        URL url = new URI("http", ip, "/api/v1/nest/" + nestId, null, null).toURL();
        Map<String, String> headers = new HashMap<>();
        String base = Base64.getEncoder().encodeToString("admin:p@ssword".getBytes("UTF-8"));
        headers.put("Authorization", "Basic " + base);
        Response res = new Response();
        JSONObject json = new JSONObject();

        JSONObject location = new JSONObject();
        location.put("longitude", 23.2222);
        location.put("latitude", 14515.2222);
        json.put("location", location);

        JSONArray photos = new JSONArray();
        photos.put("blk1eetc9wzn8fh");
        photos.put("7zzimw4oqw8h223");
        photos.put("71739y7sy8d0wh6");
        photos.put("kn4dm1qkw2lgbhy");
        json.put("photos", photos);

        json.put("groupId", 0);

        System.out.println(readString(doPut(url, headers, json.toString().getBytes(), res)));
    }

    private static void deleteNest(String id) throws Exception {
        URL url = new URI("http", ip, "/api/v1/nest/" + id, null, null).toURL();
        Map<String, String> headers = new HashMap<>();
        String base = Base64.getEncoder().encodeToString("admin:p@ssword".getBytes("UTF-8"));
        headers.put("Authorization", "Basic " + base);
        Response res = new Response();
        System.out.println(readString(doDelete(url, headers, res)));
    }

    private static String uploadPhoto(String fileName) throws Exception {
        URL url = new URI("http", ip, "/api/v1/photo", null, null).toURL();
        Map<String, String> headers = new HashMap<>();
        String base = Base64.getEncoder().encodeToString("admin:p@ssword".getBytes("UTF-8"));
        headers.put("Authorization", "Basic " + base);

        FileInputStream in = new FileInputStream(fileName);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int r;
        final byte[] buf = new byte[1024];
        while ((r = in.read(buf)) != -1) {
            out.write(buf, 0, r);
        }
        out.flush();

        Response res = new Response();

        JSONObject o = new JSONObject(readString(doPost(url, headers, out.toByteArray(), res)));
        return o.getString("photoId");
    }

    private static void getPhotos(int loops) throws Exception {
        URL url = new URI("http", ip, "/api/v1/photo/1", null, null).toURL();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("apiKey", "xwv6pr3iyc7mie16dou03zt7ww00820ei2p8ofzluh4r1ul6qff5jt08arftax60bsfl3xqt289");
        for (int i = 0; i < loops; i++) {
            Response res = new Response();
            System.out.println(readString(doGet(url, res)));
        }
    }

    private static void deletePhoto(String id) throws Exception {
        URL url = new URI("http", ip, "/api/v1/photo/" + id, null, null).toURL();
        Map<String, String> headers = new HashMap<>();
        String base = Base64.getEncoder().encodeToString("admin:p@ssword".getBytes("UTF-8"));
        headers.put("Authorization", "Basic " + base);
        Response res = new Response();
        System.out.println(readString(doDelete(url, headers, res)));
    }

    private static InputStream doPost(URL url, byte[] data, Response res) throws IOException {
        return doPost(url, null, data, res);
    }

    private static InputStream doPost(URL url, Map<String, String> headers, byte[] data, Response res) throws IOException {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setRequestProperty("Content-Type", "application/json");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        http.setDoOutput(true);

        http.getOutputStream().write(data);
        http.getOutputStream().flush();

        int resCode = (http.getResponseCode());
        res.setCode(resCode);

        return (resCode < HttpURLConnection.HTTP_BAD_REQUEST)
                ? http.getInputStream() : http.getErrorStream();
    }

    private static InputStream doPut(URL url, byte[] data, Response res) throws IOException {
        return doPost(url, null, data, res);
    }

    private static InputStream doPut(URL url, Map<String, String> headers, byte[] data, Response res) throws IOException {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("PUT");
        http.setRequestProperty("Content-Type", "application/json");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        http.setDoOutput(true);

        http.getOutputStream().write(data);
        http.getOutputStream().flush();

        int resCode = (http.getResponseCode());
        res.setCode(resCode);

        return (resCode < HttpURLConnection.HTTP_BAD_REQUEST)
                ? http.getInputStream() : http.getErrorStream();
    }

    private static InputStream doGet(URL url, Response res) throws IOException {
        return doGet(url, null, res);
    }

    private static InputStream doGet(URL url, Map<String, String> headers, Response res) throws IOException {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        int resCode = (http.getResponseCode());
        res.setCode(resCode);

        return (resCode < HttpURLConnection.HTTP_BAD_REQUEST)
                ? http.getInputStream() : http.getErrorStream();
    }

    private static InputStream doDelete(URL url, Response res) throws IOException {
        return doGet(url, null, res);
    }

    private static InputStream doDelete(URL url, Map<String, String> headers, Response res) throws IOException {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("DELETE");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        int resCode = (http.getResponseCode());
        res.setCode(resCode);

        return (resCode < HttpURLConnection.HTTP_BAD_REQUEST)
                ? http.getInputStream() : http.getErrorStream();
    }

    protected static String readString(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int r;
        final byte[] buf = new byte[1024];
        while ((r = in.read(buf)) != -1) {
            out.write(buf, 0, r);
        }
        out.flush();
        return new String(out.toByteArray());
    }
}
