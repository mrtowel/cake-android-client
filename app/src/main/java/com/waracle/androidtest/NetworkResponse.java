package com.waracle.androidtest;


/**
 * Represents network response.
 */
public final class NetworkResponse {

    private int responseCode;
    private byte[] body;
    private String contentType;

    public NetworkResponse(final int responseCode, final byte[] body, final String contentType) {
        this.responseCode = responseCode;
        this.body = body;
        this.contentType = contentType;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public byte[] getBody() {
        return body;
    }

    public String getContentType() {
        return contentType;
    }

}
