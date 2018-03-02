package com.example.prins.friendschat.async;

/**
 * Created by prins on 5/20/2017.
 */
public interface OnRequestCompletedListener<T> {

    public void onSuccess(T response);

    public void onFail(Exception ex);

    public void onProgress(Integer progress);

}//end OnRequestCompletedListener
