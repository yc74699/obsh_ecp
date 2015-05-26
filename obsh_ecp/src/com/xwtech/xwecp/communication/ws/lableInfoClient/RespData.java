
package com.xwtech.xwecp.communication.ws.lableInfoClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RespData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RespData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.serial.service.nubass.newland.com/}wsBean">
 *       &lt;sequence>
 *         &lt;element name="respObjectString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalCount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="respDataType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespData", propOrder = {
    "respObjectString",
    "totalCount",
    "respDataType"
})
public class RespData
    extends WsBean
{

    protected String respObjectString;
    protected String totalCount;
    protected String respDataType;

    /**
     * Gets the value of the respObjectString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRespObjectString() {
        return respObjectString;
    }

    /**
     * Sets the value of the respObjectString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRespObjectString(String value) {
        this.respObjectString = value;
    }

    /**
     * Gets the value of the totalCount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalCount() {
        return totalCount;
    }

    /**
     * Sets the value of the totalCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalCount(String value) {
        this.totalCount = value;
    }

    /**
     * Gets the value of the respDataType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRespDataType() {
        return respDataType;
    }

    /**
     * Sets the value of the respDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRespDataType(String value) {
        this.respDataType = value;
    }

}
