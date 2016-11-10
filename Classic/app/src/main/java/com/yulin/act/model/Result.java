package com.yulin.act.model;

public class Result {

    public static final short RESULT_OK = 0;
    public static final short RESULT_FAIL = -1;

    private int mResultCode = -1;
    private String mResultMsg;

    public Result(int resultCode) {
        mResultCode = resultCode;
    }

    public Result(int resultCode, String resultMsg) {
        mResultCode = resultCode;
        mResultMsg = resultMsg;
    }

    public int getResultCode() {
        return mResultCode;
    }

    public void setResultCode(int mResultCode) {
        this.mResultCode = mResultCode;
    }

    public String getResultMsg() {
        return mResultMsg;
    }

    public void setResultMsg(String mResultMsg) {
        this.mResultMsg = mResultMsg;
    }

}
