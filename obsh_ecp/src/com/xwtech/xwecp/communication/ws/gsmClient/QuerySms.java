
package com.xwtech.xwecp.communication.ws.gsmClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for querySms complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="querySms">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="message" type="{http://service.serial.service.nubass.newland.com/}RequestObject" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "querySms", propOrder = {
    "message"
},namespace="http://namespace.thats.not.the.same.as.the.generated")
public class QuerySms {

    protected RequestObject message;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link RequestObject }
     *     
     */
    public RequestObject getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestObject }
     *     
     */
    public void setMessage(RequestObject value) {
        this.message = value;
    }

}
