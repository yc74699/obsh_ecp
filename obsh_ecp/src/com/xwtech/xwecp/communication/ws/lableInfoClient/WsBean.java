
package com.xwtech.xwecp.communication.ws.lableInfoClient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wsBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsBean")
@XmlSeeAlso({
    User.class,
    HeaderReq.class,
    HeaderResp.class,
    ReqData.class,
    BodyResp.class,
    TokenInfo.class,
    IdentityInfo.class,
    ResponseObject.class,
    RespData.class,
    RequestObject.class,
    BodyReq.class,
    System.class,
    Route.class
})
public class WsBean {


}
