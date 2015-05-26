package com.xwtech.xwecp.web.serverHttp;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import org.dom4j.Document;
import org.dom4j.Element;
public class TableServerInfoSync {
	public String execute(Document doc) throws ServletException, IOException {
		List<Element> mallList = doc.selectNodes("/result");
		Iterator<Element> it = mallList.iterator();
		String resultCode = "";
		String resultMsg = "";
		String retJson = "";
		while (it.hasNext())
		{
			Element element = (Element) it.next();
			retJson = element.element("resultObj")== null ? "" : element.element("resultObj").getStringValue();
			resultCode = element.element("resultCode")== null ? "" : element.element("resultCode").getStringValue();
			resultMsg = element.element("resultMsg")== null ? "" : element.element("resultMsg").getStringValue();
		}
		
		return retJson;
	}
}
