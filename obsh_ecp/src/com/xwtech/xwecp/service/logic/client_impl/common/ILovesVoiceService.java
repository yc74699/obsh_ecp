package com.xwtech.xwecp.service.logic.client_impl.common;

import com.xwtech.xwecp.service.logic.LIException;
import com.xwtech.xwecp.service.logic.pojo.DEL040057Result;

public interface ILovesVoiceService {
	
   public DEL040057Result orderLovesVoice(String phoneNum,String id,String mPhoneNum) throws LIException;
   
   public DEL040057Result cancelLovesVoice(String phoneNum,String id,String chooseFlag) throws LIException;
   
}
