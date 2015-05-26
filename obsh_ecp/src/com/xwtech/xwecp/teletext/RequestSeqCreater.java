package com.xwtech.xwecp.teletext;

public final class RequestSeqCreater
{
	private static int seq = 0;
	
	private static StringBuilder sb = new StringBuilder();
	
	public static synchronized String getRequestSeq(int clientId) {
		if (seq < 0x00ffffff) {
			seq++;
		} else {
			seq = 0;
		}
		sb.delete(0, sb.length());
		sb.append(clientId);
		sb.append("_");
		sb.append(seq);
		return sb.toString();
	}
}
