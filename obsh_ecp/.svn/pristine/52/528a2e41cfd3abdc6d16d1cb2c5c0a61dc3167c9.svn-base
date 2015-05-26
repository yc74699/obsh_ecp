/*
 * 文 件 名:  ParseRedisXml.java
 * 版   权:  xwtec
 * 描     述:  <描述>
 * 修 改 人:  xwtec
 * 修改时间:  Apr 21, 2014
 * 修改内容:  <修改内容>
 */
package com.xwtech.xwecp.Jedis;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 解析Redis配置文件
 * @author  zhuxiaobo
 * @version  [版本号, Apr 21, 2014]
 * @since  [产品/模块版本]
 */
public class ParseRedisXml
{
    private static final int INIT_LENGTH_FILEPATH = 50;
    
    /**
     * 解析redis配置文件
     * @param file redis配置文件
     * @return 配置信息
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String,String>> parseXml(File file)
    {
        //初始化redis服务器信息
        List<Map<String,String>> list = new ArrayList<Map<String,String>>(INIT_LENGTH_FILEPATH);
        Map<String,String> map = null;
        SAXReader saxReader = new SAXReader();
        Document doc;
        try
        {
            doc = saxReader.read(file);
            Element root = doc.getRootElement();
            List<Element> childList = root.elements();
            for (int i = 0; i < childList.size(); i++)
            {
                Element element = (Element) childList.get(i);
                Element host = element.element("host");
                Element port = element.element("port");
                Element password = element.element("password");
                map = new HashMap<String,String>();
                map.put("host", host.getTextTrim());
                map.put("port", port.getTextTrim());
                if (null != password)
                {
                    map.put("password", password.getTextTrim());
                }
                list.add(map);
            }
            System.out.println("<" + file.getName() + "文件解析成功>");
        }
        catch (DocumentException e)
        {
            System.out.println("<" + file.getName() + "文件解析失败>");
            e.printStackTrace();
        }
        return list;
    }
}