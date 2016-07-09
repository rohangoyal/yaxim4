package com.hihello.app.content;

import org.json.JSONObject;

public class RewardItem implements Comparable<RewardItem> {

	private String id;
	private String users_id;
	private int reward;
	private String mobile_number;
	private String type;
	private String firstname;
	private String pic_url;

	public RewardItem(JSONObject json) {
		try {
			setId(json.optString("id"));
			setUsers_id(json.optString("user_id"));
			setMobile_number(json.optString("mobile_number"));
			setType(json.optString("type"));
			if (json.has("reward"))
				setReward(json.optInt("reward"));
			if (json.has("daily_reward"))
				setReward(json.optInt("daily_reward"));
			setFirstname(json.optString("firstname"));
			setPic_url(json.optString("pic_url"));
		} catch (Exception x) {

		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setUsers_id(String users_id) {
		this.users_id = users_id;
	}

	public String getUsers_id() {
		return users_id;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public int getReward() {
		return reward;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getPic_url() {
		return pic_url;
	}

	@Override
	public int compareTo(RewardItem another) {
		return another.reward - reward;

	}

}
