/*
 * Copyright 2008 XWTECH INC. All Rights reserved
 * XWTECH INC.
 * 创建日期: 2008/1/23
 * 创建人：  赵明远
 * 修改日志：
 *
 */
package com.xwtech.xwecp.util;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.lang.ArrayUtils;

/**
 * 功能名: DES加密
 * <br>
 * 功能説明:实现DES加密功能
 * @author  赵明远
 */
public class DESEncrypt {
	
    byte[] bytekey = null;//{0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3,0x4e,(byte)0xdd,(byte)0x3b,(byte)0x51,0x24,0x36,(byte)0xa8,(byte)0x28,0x0b,0x33,(byte)0xe7,(byte)0xb2,0x51,0x0d,0x75,(byte)0xc3};
    
    public DESEncrypt(byte[] bytekey) {
    	this.bytekey = bytekey;
    }
    
    public byte[] doEncrypt(byte[] plainText) {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        //加密后的字符串
        byte[] encryptedData = null;
        try{
	        // 从原始密匙数据创建DESKeySpec对象
        	DESedeKeySpec dks = new DESedeKeySpec(bytekey);
	        
	        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
	        //一个SecretKey对象
	        
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
	        SecretKey key = keyFactory.generateSecret(dks);
	        // Cipher对象实际完成加密操作
	        Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
	        // 用密匙初始化Cipher对象
	        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
	        // 现在，获取数据并加密
	        byte data[] = plainText;/* 用某种方法获取数据 */
	        	// 正式执行加密操作
	        
	        //data长度不够8的整数倍，后面补零
		    int a = (8 - (data.length % 8));
		        
	        if (a > 0) {		        
		        byte[] arr = new byte[a];
	
		        while(a != 0){
		        	arr[--a] = (byte)0;
		        }
		        
		        data = ArrayUtils.addAll(data, arr);
	        }
	        
	        encryptedData = cipher.doFinal(data);
	        
        }
        catch(Exception e){
        	//throw new BaseException();
        }
        return encryptedData;
    }
    
    public String toHexString(byte[] value) {
        String newString = "";
        for (int i = 0; i < value.length; i++) {
            byte b = value[i];
            String str = Integer.toHexString(b);
            if (str.length() > 2) {
                str = str.substring(str.length() - 2);
            }
            if (str.length() < 2) {
                str = "0" + str;
            }
            newString += str;
        }
        return newString.toUpperCase();
    }
    
    /**
     * 对字符串进行DESede加密
     * @param val
     * @param bytekey
     * @return
     */
    public static String desString(String val, byte[] bytekey) {
    	DESEncrypt des = new DESEncrypt(bytekey);
		byte[] result = des.doEncrypt(val.getBytes());
		return des.toHexString(result);
    }
    
    public static void main(String[] args) {
    	
    	//DESEncrypt a = new DESEncrypt(com.xwtech.jsbo.constant.Constants.BOSS_SECRET_KEY);
    	
    	//System.out.println(DESEncrypt.desString("134139", Constants.BOSS_SECRET_KEY));
    }

}
