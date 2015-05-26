package com.xwtech.xwecp.test;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lilystudio.smarty4j.Context;
import org.lilystudio.smarty4j.Engine;
import org.lilystudio.smarty4j.Template;

import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.teletext.BossTeletextUtil;


public class SmartyTest
{
	private static final Logger logger = Logger.getLogger(SmartyTest.class);
	
	public static void main(String args[] )throws Exception
	{

//		Engine engine = new Engine();//加载模板引擎
//		Template template = engine.getTemplate("${aaa}");
//		Context context = new Context(); // 生成数据容器对象
////		这里需要设置数据的值
//		context.set("aaa",123);
//		ByteArrayOutputStream out = new ByteArrayOutputStream(); //设置接收模板数据的输出流
//		template.merge(context, out); // 处理生成结果
	    BossTeletextUtil bossTeletextUtil= new BossTeletextUtil();
	    String cmd="0001";
	    List<RequestParameter> param=new ArrayList();
	    RequestParameter requestParameter=new RequestParameter();
	    requestParameter.setParameterName("刘军");
	    param.add(requestParameter);
	    bossTeletextUtil.mergeTeletext(cmd, param);
	    ////////////////////////////////////////////
//	    bossTeletextUtil.getTeletextTemplate(cmd);

		Engine engine = new Engine();//加载模板引擎
		//engine.setLeftDelimiter("${");
		//engine.setRightDelimiter("}");
		//Template template = engine.getTemplate("${aaa}");
		Template template = new Template(engine, "$aaa");
		Context context = new Context(); // 生成数据容器对象
//		这里需要设置数据的值
		context.set("aaa",123);
		ByteArrayOutputStream out = new ByteArrayOutputStream(); //设置接收模板数据的输出流
		template.merge(context, out); // 处理生成结果
		System.out.println(new String(out.toByteArray()));

	}
}
