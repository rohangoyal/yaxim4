package com.hihello.app.content;



public class InviteContact implements Comparable<InviteContact>  {
	String name;
	String phoneNo;
	String photoUri;

	public InviteContact(String name, String phoneNo, String photoUri) {
		super();
		this.name = name;
		this.phoneNo = phoneNo;
		this.photoUri = photoUri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPhotoUri() {
		return photoUri;
	}

	public void setPhotoUri(String photoUri) {
		this.photoUri = photoUri;
	}

	@Override
	public int compareTo(InviteContact another) {
		// TODO Auto-generated method stub
		return name.compareTo(another.getName());
	}

}
