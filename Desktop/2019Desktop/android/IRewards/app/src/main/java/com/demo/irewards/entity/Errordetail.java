package com.demo.irewards.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Errordetail {

    private String message;
    private String status;
    private String[] subErrors;
    private String timestamp;

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setSubErrors(String[] subErrors){
        this.subErrors = subErrors;
    }
    public String[] getSubErrors(){
        return this.subErrors;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
    public String getTimestamp(){
        return this.timestamp;
    }


    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public Errordetail(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        message = jsonObject.optString("message");
        status = jsonObject.optString("status");
        JSONArray subErrorsJsonArray = jsonObject.optJSONArray("rewards");
        if(subErrorsJsonArray != null){
            ArrayList<Reward> rewardsArrayList = new ArrayList<>();
            for (int i = 0; i < subErrorsJsonArray.length(); i++) {
                JSONObject rewardsObject = subErrorsJsonArray.optJSONObject(i);
                rewardsArrayList.add(new Reward(rewardsObject));
            }
            subErrors = (String[]) rewardsArrayList.toArray();
        }
        timestamp = jsonObject.optString("timestamp");
    }

    /**
     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", message);
            jsonObject.put("status", status);
            jsonObject.put("subErrors", subErrors);
            jsonObject.put("timestamp", timestamp);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

}
