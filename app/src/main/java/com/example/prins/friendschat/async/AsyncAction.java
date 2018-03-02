package com.example.prins.friendschat.async;



/**
 * Created by prins on 5/20/2017.
 * T: return type of the action
 */

public interface AsyncAction <T> {
    Object run() throws UiException;
}//end AsyncAction
