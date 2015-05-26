
package com.xwtech.xwecp.communication.ws.gsmClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HeaderResp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HeaderResp">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.serial.service.nubass.newland.com/}wsBean">
 *       &lt;sequence>
 *         &lt;element name="RespResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RespTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RespCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RespDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TokenCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="svcReqTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="svcRespTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sqlReqTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sqlRespTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HeaderResp", propOrder = {
    "respResult",
    "respTime",
    "respCode",
    "respDesc",
    "tokenCode",
    "svcReqTime",
    "svcRespTime",
    "sqlReqTime",
    "sqlRespTime"
})
public class HeaderResp
    extends WsBean
{

    @XmlElement(name = "RespResult")
    protected String respResult;
    @XmlElement(name = "RespTime")
    protected String respTime;
    @XmlElement(name = "RespCode")
    protected String respCode;
    @XmlElement(name = "RespDesc")
    protected String respDesc;
    @XmlElement(name = "TokenCode")
    protected String tokenCode;
    protected String svcReqTime;
    protected String svcRespTime;
    protected String sqlReqTime;
    protected String sqlRespTime;

    /**
     * Gets the value of the respResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRespResult() {
        return respResult;
    }

    /**
     * Sets the value of the respResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRespResult(String value) {
        this.respResult = value;
    }

    /**
     * Gets the value of the respTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRespTime() {
        return respTime;
    }

    /**
     * Sets the value of the respTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRespTime(String value) {
        this.respTime = value;
    }

    /**
     * Gets the value of the respCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRespCode() {
        return respCode;
    }

    /**
     * Sets the value of the respCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRespCode(String value) {
        this.respCode = value;
    }

    /**
     * Gets the value of the respDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRespDesc() {
        return respDesc;
    }

    /**
     * Sets the value of the respDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRespDesc(String value) {
        this.respDesc = value;
    }

    /**
     * Gets the value of the tokenCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokenCode() {
        return tokenCode;
    }

    /**
     * Sets the value of the tokenCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokenCode(String value) {
        this.tokenCode = value;
    }

    /**
     * Gets the value of the svcReqTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvcReqTime() {
        return svcReqTime;
    }

    /**
     * Sets the value of the svcReqTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvcReqTime(String value) {
        this.svcReqTime = value;
    }

    /**
     * Gets the value of the svcRespTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvcRespTime() {
        return svcRespTime;
    }

    /**
     * Sets the value of the svcRespTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvcRespTime(String value) {
        this.svcRespTime = value;
    }

    /**
     * Gets the value of the sqlReqTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSqlReqTime() {
        return sqlReqTime;
    }

    /**
     * Sets the value of the sqlReqTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSqlReqTime(String value) {
        this.sqlReqTime = value;
    }

    /**
     * Gets the value of the sqlRespTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSqlRespTime() {
        return sqlRespTime;
    }

    /**
     * Sets the value of the sqlRespTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSqlRespTime(String value) {
        this.sqlRespTime = value;
    }

}
