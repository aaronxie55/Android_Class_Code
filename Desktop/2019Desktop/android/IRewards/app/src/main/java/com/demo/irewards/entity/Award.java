package com.demo.irewards.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Award {

    private Source source;
    private Target target;

    public Award() {}

    public void setSource(Source source){
        this.source = source;
    }
    public Source getSource(){
        return this.source;
    }
    public void setTarget(Target target){
        this.target = target;
    }
    public Target getTarget(){
        return this.target;
    }


    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public Award(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        source = new Source(jsonObject.optJSONObject("source"));
        target = new Target(jsonObject.optJSONObject("target"));
    }

    /**
     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("source", source.toJsonObject());
            jsonObject.put("target", target.toJsonObject());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static class Target{

        private String date;
        private String name;
        private String notes;
        private String studentId;
        private String username;
        private int value;

        public void setDate(String date){
            this.date = date;
        }
        public String getDate(){
            return this.date;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setNotes(String notes){
            this.notes = notes;
        }
        public String getNotes(){
            return this.notes;
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
        public void setValue(int value){
            this.value = value;
        }
        public int getValue(){
            return this.value;
        }

        public Target() {
        }

        /**
         * Instantiate the instance using the passed jsonObject to set the properties values
         */
        public Target(JSONObject jsonObject){
            if(jsonObject == null){
                return;
            }
            date = jsonObject.optString("date");
            name = jsonObject.optString("name");
            notes = jsonObject.optString("notes");
            studentId = jsonObject.optString("studentId");
            username = jsonObject.optString("username");
            value = jsonObject.optInt("value");
        }

        /**
         * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
         */
        public JSONObject toJsonObject()
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("date", date);
                jsonObject.put("name", name);
                jsonObject.put("notes", notes);
                jsonObject.put("studentId", studentId);
                jsonObject.put("username", username);
                jsonObject.put("value", value);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return jsonObject;
        }

    }

    public static class Source{

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


        public Source() {
        }

        /**
         * Instantiate the instance using the passed jsonObject to set the properties values
         */
        public Source(JSONObject jsonObject){
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
}
