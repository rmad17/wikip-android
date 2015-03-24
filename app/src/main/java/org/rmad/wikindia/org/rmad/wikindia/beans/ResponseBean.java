package org.rmad.wikindia.org.rmad.wikindia.beans;

import java.util.ArrayList;

/**
 * Created by rmad on 23/3/15.
 */
public class ResponseBean {
    String status;
    String message;
    ArrayList<WikiDataBean> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<WikiDataBean> getResults() {
        return results;
    }

    public void setResults(ArrayList<WikiDataBean> results) {
        this.results = results;
    }
}
