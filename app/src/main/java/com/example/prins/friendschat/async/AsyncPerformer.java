package com.example.prins.friendschat.async;

import android.os.AsyncTask;


/**
 * Created by prins on 5/20/2017.
 */
public class AsyncPerformer extends AsyncTask {
    //===================================================================
    private final AsyncAction actionToPerform;
    private final OnRequestCompletedListener callback;
    //===================================================================
    public AsyncPerformer(AsyncAction actionToPerform, OnRequestCompletedListener callback){
        this.actionToPerform = actionToPerform;
        this.callback = callback;
    }//end constructor
    //===================================================================
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            return actionToPerform.run();
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }//end doInBackground
    //===================================================================
    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        if(result instanceof Exception) {
            callback.onFail((Exception) result);
            return;
        }

        callback.onSuccess(result);
    }
    //===================================================================

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        callback.onProgress((Integer) values[0]);
    }

    //===================================================================
}//end AsyncPerformer
