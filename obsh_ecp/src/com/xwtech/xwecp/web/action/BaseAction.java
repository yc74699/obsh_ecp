package com.xwtech.xwecp.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

@SuppressWarnings("serial")
public class BaseAction extends HttpServlet {

	protected final String charset = "GBK";
	protected BossTeletextUtil bossTeletextUtil;
	protected ApplicationContext springCtx ;
	
	public BaseAction()
	{
		springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil)(springCtx.getBean("bossTeletextUtil"));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		service(req, resp);
	}
	
}
