package com.xwtech.xwecp.util;

import java.math.BigDecimal;
import java.util.List;

/**
 * 常用工具类(金额转换，列表判断)
 * @author chenxiaoming
 * @date 2013-01-01
 *
 */
public class CommonUtil {

	/**
	 * 判断list是否为空
	 * @param list
	 * @return
	 */
	public static boolean isNull(List list)
	{

        if (list != null && list.size() > 0)
        {
            return false;
        }
        
        return true;
	}
	
	/**
	 * 金额转换 分-> 元
	 * @param money
	 * @return
	 */
	public static String minute2YUAN(String money){
		if(null ==money || "".equals(money)) return money;
		BigDecimal big=new BigDecimal(money);
		big=big.divide(new BigDecimal("100"));
		String re=big.toString();
		int d=re.indexOf(".");
		if(d> -1){
			if(d==re.length()-1)re+="00";
			else if(d==re.length()-2)re+="0";
		}
		else re+=".00";
		return re;
	}
	
	/*
	 * 金额转换 元 -> 分
	 */
	public static String yuan2Minute(String money){
		if(null ==money || "".equals(money)) return money;
		BigDecimal big=new BigDecimal(money);
		big=big.multiply(new BigDecimal("100"));
		String res=big.toString();
		if(res.indexOf(".") == -1)
		{
			res+=".0";
		}
		return res.substring(0,res.indexOf("."));
	}
	/*
	 * 二分查找
	 */
	public static boolean secondSearch(List<String> phoneList,long phone)
	{
		int low = 0;   
		int high = phoneList.size()-1;   
		while(low <= high)
		{   
		   int middle = (low + high)/2;
		        
		    if(phone == Long.parseLong(phoneList.get(middle)))
		    {   
		        return true;
		    }
		    else if(phone < Long.parseLong(phoneList.get(middle)))
		    {   
		        high = middle - 1;
		    }
		    else
		    {   
		        low = middle + 1;
		    }  
		}  
		return false;
	}
	
	public static void main(String[] args)
	{
		System.out.println(minute2YUAN("23480"));
	}
}
