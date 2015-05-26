
package com.xwtech.xwecp.communication.ws.lableInfoClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for User complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="User">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.serial.service.nubass.newland.com/}wsBean">
 *       &lt;sequence>
 *         &lt;element name="IdentityInfo" type="{http://service.serial.service.nubass.newland.com/}IdentityInfo" minOccurs="0"/>
 *         &lt;element name="TokenInfo" type="{http://service.serial.service.nubass.newland.com/}TokenInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "User", propOrder = {
    "identityInfo",
    "tokenInfo"
})
public class User
    extends WsBean
{

    @XmlElement(name = "IdentityInfo")
    protected IdentityInfo identityInfo;
    @XmlElement(name = "TokenInfo")
    protected TokenInfo tokenInfo;

    /**
     * Gets the value of the identityInfo property.
     * 
     * @return
     *     possible object is
     *     {@link IdentityInfo }
     *     
     */
    public IdentityInfo getIdentityInfo() {
        return identityInfo;
    }

    /**
     * Sets the value of the identityInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentityInfo }
     *     
     */
    public void setIdentityInfo(IdentityInfo value) {
        this.identityInfo = value;
    }

    /**
     * Gets the value of the tokenInfo property.
     * 
     * @return
     *     possible object is
     *     {@link TokenInfo }
     *     
     */
    public TokenInfo getTokenInfo() {
        return tokenInfo;
    }

    /**
     * Sets the value of the tokenInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TokenInfo }
     *     
     */
    public void setTokenInfo(TokenInfo value) {
        this.tokenInfo = value;
    }

}
