package com.demo.irewards.entity;
import org.json.*;

import java.io.Serializable;
import java.util.*;


public class Profile implements Serializable, Comparable {

	private boolean admin;
	private String department;
	private String firstName;
	private String imageBytes;
	private String lastName;
	private String location;
	private String oldPassword;
	private String password;
	private int pointsToAward;
	private String position;
	private List<Reward> rewards;
	private String story;
	private String studentId;
	private String username;

	public void setAdmin(boolean admin){
		this.admin = admin;
	}
	public boolean isAdmin(){
		return this.admin;
	}
	public void setDepartment(String department){
		this.department = department;
	}
	public String getDepartment(){
		return this.department;
	}
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	public String getFirstName(){
		return this.firstName;
	}
	public void setImageBytes(String imageBytes){
		this.imageBytes = imageBytes;
	}
	public String getImageBytes(){
		return this.imageBytes;
	}
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	public String getLastName(){
		return this.lastName;
	}
	public void setLocation(String location){
		this.location = location;
	}
	public String getLocation(){
		return this.location;
	}
	public void setOldPassword(String oldPassword){
		this.oldPassword = oldPassword;
	}
	public String getOldPassword(){
		return this.oldPassword;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getPassword(){
		return this.password;
	}
	public void setPointsToAward(int pointsToAward){
		this.pointsToAward = pointsToAward;
	}
	public int getPointsToAward(){
		return this.pointsToAward;
	}
	public void setPosition(String position){
		this.position = position;
	}
	public String getPosition(){
		return this.position;
	}
	public List<Reward> getRewards() {
		return rewards;
	}
	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
	}
	public void setStory(String story){
		this.story = story;
	}
	public String getStory(){
		return this.story;
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


	public Profile() {}
	public int awardedRewards() {
		if (rewards == null) return 0;
		int total = 0;
		for (int i = 0; i < rewards.size(); i++) {
			total += rewards.get(i).getValue();
		}
		return total;
	}

	/**
	 * Instantiate the instance using the passed jsonObject to set the properties values
	 */
	public Profile(JSONObject jsonObject){
		if(jsonObject == null){
			return;
		}
		admin = jsonObject.optBoolean("admin");
		department = jsonObject.optString("department");
		firstName = jsonObject.optString("firstName");
		imageBytes = jsonObject.optString("imageBytes");
		lastName = jsonObject.optString("lastName");
		location = jsonObject.optString("location");
		oldPassword = jsonObject.optString("oldPassword");
		password = jsonObject.optString("password");
		pointsToAward = jsonObject.optInt("pointsToAward");
		position = jsonObject.optString("position");
		story = jsonObject.optString("story");
		studentId = jsonObject.optString("studentId");
		username = jsonObject.optString("username");
		JSONArray rewardsJsonArray = jsonObject.optJSONArray("rewards");
		rewards = new ArrayList<>();
		if(rewardsJsonArray != null){
			for (int i = 0; i < rewardsJsonArray.length(); i++) {
				JSONObject rewardsObject = rewardsJsonArray.optJSONObject(i);
				rewards.add(new Reward(rewardsObject));
			}
		}
	}

	/**
	 * Returns all the available property values in the form of JSONObject instance where the key is the approperiate json key and the value is the value of the corresponding field
	 */
	public JSONObject toJsonObject()
	{
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("admin", admin);
			jsonObject.put("department", department);
			jsonObject.put("firstName", firstName);
			jsonObject.put("imageBytes", imageBytes);
			jsonObject.put("lastName", lastName);
			jsonObject.put("location", location);
			jsonObject.put("oldPassword", oldPassword);
			jsonObject.put("password", password);
			jsonObject.put("pointsToAward", pointsToAward);
			jsonObject.put("position", position);
			jsonObject.put("story", story);
			jsonObject.put("studentId", studentId);
			jsonObject.put("username", username);
			if(rewards != null && rewards.size() > 0){
				JSONArray rewardsJsonArray = new JSONArray();
				for(Reward rewardsElement : rewards){
					rewardsJsonArray.put(rewardsElement.toJsonObject());
				}
				jsonObject.put("rewards", rewardsJsonArray);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	@Override
	public int compareTo(Object o) {
		return ((Profile) o).awardedRewards() - awardedRewards();
	}
}