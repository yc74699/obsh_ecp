package com.xwtech.xwecp.test;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.xwtech.xwecp.msg.MessageHead;
import com.xwtech.xwecp.msg.ServiceMessage;
import com.xwtech.xwecp.service.ServiceExecutor;

public class ServiceExecutorDAOImplTest extends TestCase {

    public ApplicationContext ctx;
    public ServiceExecutor serviceExecutor;
    protected void setUp() throws Exception {
        String paths[] = { "/WebRoot/WEB-INF/cfg/applicationContext-xwecp.xml",
                "/WebRoot/WEB-INF/cfg/applicationContext-datasource.xml",
                "/WebRoot/WEB-INF/cfg/controllers.xml",
                "/WebRoot/WEB-INF/cfg/applicationContext-initialization.xml" };
        try {
            ctx = new FileSystemXmlApplicationContext(paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        serviceExecutor = (ServiceExecutor) ctx.getBean("serviceExecutor");
    }

    public void testGetServiceConfig() {
    	ServiceMessage msg = new ServiceMessage();
    	MessageHead head = new MessageHead();
    	head.setUser("15952015568");
    	head.setChannel("1");
    	head.setPwd("11");
    	msg.setHead(head);
    	//System.out.println(serviceExecutor.checkPrivilege(msg, "4"));
    }

}
