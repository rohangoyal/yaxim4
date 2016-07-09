/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\New folder\\yaxim4\\namoNamoApp\\src\\main\\aidl\\com\\namonamo\\androidclient\\service\\IXMPPChatService.aidl
 */
package com.namonamo.androidclient.service;
public interface IXMPPChatService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.namonamo.androidclient.service.IXMPPChatService
{
private static final java.lang.String DESCRIPTOR = "com.namonamo.androidclient.service.IXMPPChatService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.namonamo.androidclient.service.IXMPPChatService interface,
 * generating a proxy if needed.
 */
public static com.namonamo.androidclient.service.IXMPPChatService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.namonamo.androidclient.service.IXMPPChatService))) {
return ((com.namonamo.androidclient.service.IXMPPChatService)iin);
}
return new com.namonamo.androidclient.service.IXMPPChatService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_sendMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.sendMessage(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_sendPingMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sendPingMessage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isAuthenticated:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isAuthenticated();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_clearNotifications:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.clearNotifications(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_saveMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.saveMessage(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_setCurrentJID:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setCurrentJID(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.namonamo.androidclient.service.IXMPPChatService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void sendMessage(java.lang.String user, java.lang.String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(user);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_sendMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sendPingMessage(java.lang.String user) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(user);
mRemote.transact(Stub.TRANSACTION_sendPingMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isAuthenticated() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isAuthenticated, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void clearNotifications(java.lang.String Jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(Jid);
mRemote.transact(Stub.TRANSACTION_clearNotifications, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void saveMessage(java.lang.String user, java.lang.String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(user);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_saveMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setCurrentJID(java.lang.String user) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(user);
mRemote.transact(Stub.TRANSACTION_setCurrentJID, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_sendMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_sendPingMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isAuthenticated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_clearNotifications = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_saveMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_setCurrentJID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void sendMessage(java.lang.String user, java.lang.String message) throws android.os.RemoteException;
public void sendPingMessage(java.lang.String user) throws android.os.RemoteException;
public boolean isAuthenticated() throws android.os.RemoteException;
public void clearNotifications(java.lang.String Jid) throws android.os.RemoteException;
public void saveMessage(java.lang.String user, java.lang.String message) throws android.os.RemoteException;
public void setCurrentJID(java.lang.String user) throws android.os.RemoteException;
}