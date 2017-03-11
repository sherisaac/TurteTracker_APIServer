/*
 * Copyright Accusoft 2017
 */
package test;

/**
 *
 * @author iyousuf
 */
public class Response {

    private int code = -1;
    private String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

}
