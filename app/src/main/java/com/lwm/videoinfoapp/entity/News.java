package com.lwm.videoinfoapp.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class News {
    private String reason;
    @SerializedName("error_code")
    private int errorCode;
    private Result result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public  class Result {
        String stat;
        NewsInfo[] data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public NewsInfo[] getData() {
            return data;
        }

        public void setData(NewsInfo[] data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "stat='" + stat + '\'' +
                    ", data=" + Arrays.toString(data) +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "News{" +
                "reason='" + reason + '\'' +
                ", errorCode=" + errorCode +
                ", result=" + result +
                '}';
    }
}