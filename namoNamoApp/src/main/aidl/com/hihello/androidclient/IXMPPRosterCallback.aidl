package com.hihello.androidclient;

/*
	IPC interface for XMPPService to send broadcasts to UI
*/

interface IXMPPRosterCallback {
	void connectionStateChanged(int connectionstate);
}
