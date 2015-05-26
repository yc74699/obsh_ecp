
package com.xwtech.xwecp.communication.ws.lableInfoClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BodyReq complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BodyReq">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.serial.service.nubass.newland.com/}wsBean">
 *       &lt;sequence>
 *         &lt;element name="ReqData" type="{http://service.serial.service.nubass.newland.com/}ReqData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BodyReq", propOrder = {
    "reqData"
})
public class BodyReq
    extends WsBean
{

    @XmlElement(name = "ReqData")
    protected ReqData reqData;

    /**
     * Gets the value of the reqData property.
     * 
     * @return
     *     possible object is
     *     {@link ReqData }
     *     
     */
    public ReqData getReqData() {
        return reqData;
    }

    /**
     * Sets the value of the reqData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReqData }
     *     
     */
    public void setReqData(ReqData value) {
        this.reqData = value;
    }

}
