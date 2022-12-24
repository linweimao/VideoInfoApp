package com.lwm.videoinfoapp.entity;

public class LoginResponse {

    /**
     * msg : success
     * code : 0
     * expire : 604800
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4IiwiaWF0IjoxNjcxODkzNjY2LCJleHAiOjE2NzI0OTg0NjZ9.qEIWDPjwaME1w8CH6FO1zptK_xjaF6WZaaxdlRWwRXzVeXhmjMZfcsH6Dmd58gXQE1HeEoqVgunzOxAnVb62_A
     */

    private String msg;
    private int code;
    private int expire;
    private String token;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
