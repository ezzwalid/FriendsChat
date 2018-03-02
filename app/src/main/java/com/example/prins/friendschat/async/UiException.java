package com.example.prins.friendschat.async;

/**
 * Created by prins on 5/20/2017.
 */
public class UiException extends Exception {
    private String message;

    public UiException(String detailMessage) {
        super(detailMessage);
        this.message = detailMessage;
    }

    public UiException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.message = detailMessage;
    }

    public UiException(Throwable throwable) {
        super(throwable);
        this.message = throwable.getMessage();
    }
}
