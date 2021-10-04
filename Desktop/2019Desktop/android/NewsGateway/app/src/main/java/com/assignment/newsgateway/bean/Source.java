package com.assignment.newsgateway.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Source implements Serializable {
    private String category;
    private String country;
    private String description;
    private String id;
    private String language;
    private String name;
    private String url;

    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public Source(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        category = jsonObject.optString("category");
        country = jsonObject.optString("country");
        description = jsonObject.optString("description");
        id = jsonObject.optString("id");
        language = jsonObject.optString("language");
        name = jsonObject.optString("name");
        url = jsonObject.optString("url");
    }

    /**
     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("category", category);
            jsonObject.put("country", country);
            jsonObject.put("description", description);
            jsonObject.put("id", id);
            jsonObject.put("language", language);
            jsonObject.put("name", name);
            jsonObject.put("url", url);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
