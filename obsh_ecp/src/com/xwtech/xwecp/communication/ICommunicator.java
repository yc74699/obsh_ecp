package com.xwtech.xwecp.communication;


public interface ICommunicator
{
	byte[] send(byte[] data, String channel) throws CommunicateException;
	
	String send(String data, String channel) throws CommunicateException;
	
	String send2CRM(String data, String channel) throws CommunicateException;
	
	String send3CRM(String data, String channel) throws CommunicateException;
}