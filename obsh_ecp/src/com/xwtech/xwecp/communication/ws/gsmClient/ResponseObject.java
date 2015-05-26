
package com.xwtech.xwecp.communication.ws.gsmClient;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * <p>Java class for ResponseObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseObject">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.serial.service.nubass.newland.com/}wsBean">
 *       &lt;sequence>
 *         &lt;element name="HeaderResp" type="{http://service.serial.service.nubass.newland.com/}HeaderResp" minOccurs="0"/>
 *         &lt;element name="BodyResp" type="{http://service.serial.service.nubass.newland.com/}BodyResp" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseObject", propOrder = {
    "headerResp",
    "bodyResp"
})
public class ResponseObject
    extends WsBean
{

    @XmlElement(name = "HeaderResp")
    protected HeaderResp headerResp;
    @XmlElement(name = "BodyResp")
    protected BodyResp bodyResp;

    /**
     * Gets the value of the headerResp property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderResp }
     *     
     */
    public HeaderResp getHeaderResp() {
        return headerResp;
    }

    /**
     * Sets the value of the headerResp property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderResp }
     *     
     */
    public void setHeaderResp(HeaderResp value) {
        this.headerResp = value;
    }

    /**
     * Gets the value of the bodyResp property.
     * 
     * @return
     *     possible object is
     *     {@link BodyResp }
     *     
     */
    public BodyResp getBodyResp() {
        return bodyResp;
    }

    /**
     * Sets the value of the bodyResp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BodyResp }
     *     
     */
    public void setBodyResp(BodyResp value) {
        this.bodyResp = value;
    }
    public List parseToBean(Class T) throws Exception {
    	String respObjectString=this.getBodyResp().getRespData().getRespObjectString();
    	Map<String, Method> methodsMap=new HashMap<String, Method>();
    	if (T!=null) {
			Method[] methods=T.getMethods();
			for (Method method:methods) {
				String methodName=method.getName();
				if (methodName.startsWith("set") && methodName.length() > 3) {
					if (method.getParameterTypes().length == 1) {
						methodsMap.put(methodName.substring(3, 4).toLowerCase()+methodName.substring(4), method);
				    }
				}
			}
		}else{
			throw new Exception("找不到对象");
		}
    	if (respObjectString==null||respObjectString.equals("")) {
			throw new Exception("返回值为空");
		}
    	try{
    		List list=new ArrayList();
    		Document document=DocumentHelper.parseText(respObjectString);
    		List<Element> rows = document.getRootElement().elements("row");
    		for (int i = 0; i < rows.size(); i++) {
    			Object serialInfo=T.newInstance();
    			List<Element> row = rows.get(i).elements();
    			for (Element element : row) {
        			String name = element.getName();
        			Object value = element.getText();
        			if (methodsMap.containsKey(name)) {
        				methodsMap.get(name).invoke(serialInfo, value);
        			}
				}
    			list.add(serialInfo);
			}
    		return list;
    	} catch (Exception e) {
    		throw new Exception("解析异常："+e.getMessage());
		}
    }
}
