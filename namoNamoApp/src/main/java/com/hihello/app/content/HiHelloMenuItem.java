package com.hihello.app.content;

public class HiHelloMenuItem {

	private String menuText;
	private int menu_image;

	public HiHelloMenuItem(String menu_text, int menu_id) {
		this.menuText = menu_text;
		this.menu_image = menu_id;
	}

	public void setMenuText(String menuText) {
		this.menuText = menuText;
	}

	public String getMenuText() {
		return menuText;
	}

	public void setMenu_image(int menu_image) {
		this.menu_image = menu_image;
	}

	public int getMenu_image() {
		return menu_image;
	}
}