package com.assignment.newsgateway.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Article implements Serializable {
    private String author;
    private String content;
    private String description;
    private String publishedAt;
    private Source source;
    private String title;
    private String url;
    private String urlToImage;


    /**
     * Instantiate the instance using the passed jsonObject to set the properties values
     */
    public Article(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        author = jsonObject.optString("author");
        content = jsonObject.optString("content");
        description = jsonObject.optString("description");
        publishedAt = jsonObject.optString("publishedAt");
        source = new Source(jsonObject.optJSONObject("source"));
        title = jsonObject.optString("title");
        url = jsonObject.optString("url");
        urlToImage = jsonObject.optString("urlToImage");
    }

    /**
     * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
     */
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("author", author);
            jsonObject.put("content", content);
            jsonObject.put("description", description);
            jsonObject.put("publishedAt", publishedAt);
            jsonObject.put("source", source.toJsonObject());
            jsonObject.put("title", title);
            jsonObject.put("url", url);
            jsonObject.put("urlToImage", urlToImage);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}
