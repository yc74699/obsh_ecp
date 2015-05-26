package com.xwtech.xwecp.communication;


import org.apache.log4j.Logger;

public interface IRemote
{
	Object callRemote(IStreamableMessage request) throws CommunicateException;
	//QRY050015 超时，单独设置超时时间而用
	Object callRemote2(IStreamableMessage request) throws CommunicateException;
	Object callJsonRemote(IStreamableMessage request) throws CommunicateException;
}
