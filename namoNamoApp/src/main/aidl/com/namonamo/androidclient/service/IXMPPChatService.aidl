package com.namonamo.androidclient.service;

interface IXMPPChatService {
	void sendMessage(String user, String message);
	void sendPingMessage(String user);
	boolean isAuthenticated();
	void clearNotifications(String Jid);
	void saveMessage(String user, String message);
	void setCurrentJID(String user);
}