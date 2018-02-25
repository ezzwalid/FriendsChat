package com.example.prins.friendschat.Dtos;

/**
 * Created by prins on 2/23/2018.
 */

public class Dialog {

    public static final String KEY = "Dialogs";
    public static final String sender_receiver_ids_key = "sender_receiver_ids";
    public static final String receiver_sender_ids_key = "receiver_sender_ids";


    private String id;
    private String sender_receiver_ids;
    private String receiver_sender_ids;
    private Msg message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_receiver_ids() {
        return sender_receiver_ids;
    }

    public void setSender_receiver_ids(String sender_receiver_ids) {
        this.sender_receiver_ids = sender_receiver_ids;
    }

    public String getReceiver_sender_ids() {
        return receiver_sender_ids;
    }

    public void setReceiver_sender_ids(String receiver_sender_ids) {
        this.receiver_sender_ids = receiver_sender_ids;
    }

    public Msg getMessage() {
        return message;
    }

    public void setMessage(Msg message) {
        this.message = message;
    }
}
