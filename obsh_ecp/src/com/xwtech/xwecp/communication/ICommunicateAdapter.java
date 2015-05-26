package com.xwtech.xwecp.communication;


import org.apache.log4j.Logger;


public interface ICommunicateAdapter
{
	ICommunicator findCommunicatorForRequest(Object request);
}
