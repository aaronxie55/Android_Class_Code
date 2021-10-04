package com.demo.irewards.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Login implements Serializable {

    private String password;
    private String studentId;
    private String username;

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setStudentId(String studentId){
        this.studentId = studentId;
    }
    public String getStudentId(){
        return this.studentId;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }


    public Login() {}

    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public Login(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        password = jsonObject.optString("password");
        studentId = jsonObject.optString("studentId");
        username = jsonObject.optString("username");
    }

    /**
     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password);
            jsonObject.put("studentId", studentId);
            jsonObject.put("username", username);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

}
