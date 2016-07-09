package com.namonamo.app.content;

public class SpecialOfferItem {
	private int image_id;
	private String title;
	private String summery;
	private String detail;

	public SpecialOfferItem(int image_id, String title, String summery,
			String detail) {
		this.image_id = image_id;
		this.title = title;
		this.summery = summery;
		this.detail = detail;
	}

	public int getImage_id() {
		return image_id;
	}

	public String getSummery() {
		return summery;
	}

	public String getDetail() {
		return detail;
	}

	public String getTitle() {
		return title;
	}
}
