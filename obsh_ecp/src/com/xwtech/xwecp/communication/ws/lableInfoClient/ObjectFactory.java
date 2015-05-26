
package com.xwtech.xwecp.communication.ws.lableInfoClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.wsclient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QueryLable_QNAME = new QName("http://service.labservice.service.nubass.newland.com/", "queryLable");
    private final static QName _QueryLableResponse_QNAME = new QName("http://service.labservice.service.nubass.newland.com/", "queryLableResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.wsclient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResponseObject }
     * 
     */
    public ResponseObject createResponseObject() {
        return new ResponseObject();
    }

    /**
     * Create an instance of {@link HeaderResp }
     * 
     */
    public HeaderResp createHeaderResp() {
        return new HeaderResp();
    }

    /**
     * Create an instance of {@link WsBean }
     * 
     */
    public WsBean createWsBean() {
        return new WsBean();
    }

    /**
     * Create an instance of {@link BodyResp }
     * 
     */
    public BodyResp createBodyResp() {
        return new BodyResp();
    }

    /**
     * Create an instance of {@link RespData }
     * 
     */
    public RespData createRespData() {
        return new RespData();
    }

    /**
     * Create an instance of {@link Route }
     * 
     */
    public Route createRoute() {
        return new Route();
    }

    /**
     * Create an instance of {@link BodyReq }
     * 
     */
    public BodyReq createBodyReq() {
        return new BodyReq();
    }

    /**
     * Create an instance of {@link RequestObject }
     * 
     */
    public RequestObject createRequestObject() {
        return new RequestObject();
    }

    /**
     * Create an instance of {@link TokenInfo }
     * 
     */
    public TokenInfo createTokenInfo() {
        return new TokenInfo();
    }

    /**
     * Create an instance of {@link System }
     * 
     */
    public System createSystem() {
        return new System();
    }

    /**
     * Create an instance of {@link HeaderReq }
     * 
     */
    public HeaderReq createHeaderReq() {
        return new HeaderReq();
    }

    /**
     * Create an instance of {@link ReqData }
     * 
     */
    public ReqData createReqData() {
        return new ReqData();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link QueryLable }
     * 
     */
    public QueryLable createQueryLable() {
        return new QueryLable();
    }

    /**
     * Create an instance of {@link QueryLableResponse }
     * 
     */
    public QueryLableResponse createQueryLableResponse() {
        return new QueryLableResponse();
    }

    /**
     * Create an instance of {@link IdentityInfo }
     * 
     */
    public IdentityInfo createIdentityInfo() {
        return new IdentityInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryLable }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.labservice.service.nubass.newland.com/", name = "queryLable")
    public JAXBElement<QueryLable> createQueryLable(QueryLable value) {
        return new JAXBElement<QueryLable>(_QueryLable_QNAME, QueryLable.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryLableResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.labservice.service.nubass.newland.com/", name = "queryLableResponse")
    public JAXBElement<QueryLableResponse> createQueryLableResponse(QueryLableResponse value) {
        return new JAXBElement<QueryLableResponse>(_QueryLableResponse_QNAME, QueryLableResponse.class, null, value);
    }

}
