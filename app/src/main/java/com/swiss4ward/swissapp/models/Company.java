package com.swiss4ward.swissapp.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Company {

    private String name, catchPhrase, bs;

    public Company(JSONObject ob) {
        try {
            this.name = ob.getString("name");
            this.catchPhrase = ob.getString("catchPhrase");
            this.bs = ob.getString("bs");
        }catch (JSONException je){
            Log.e("JSONException", "JSONException");
        }
    }

    public Company(String name, String catchPhrase, String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public void setCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }
}
